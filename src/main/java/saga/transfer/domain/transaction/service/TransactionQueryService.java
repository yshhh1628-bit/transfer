package saga.transfer.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import saga.transfer.domain.transaction.Transaction;
import saga.transfer.domain.transaction.TransactionRepository;
import saga.transfer.domain.transaction.TransactionType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {
    private final TransactionRepository transactionRepository;

    public TransactionHistoryResponse getHistory(Long accountId, Pageable pageable) {
        Pageable sorted = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Transaction> page = transactionRepository.findHistory(accountId, sorted);

        List<TransactionHistoryResponse.Item> items = page.getContent().stream()
                .map(tx -> mapItem(tx, accountId))
                .toList();

        return new TransactionHistoryResponse(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private TransactionHistoryResponse.Item mapItem(Transaction t, Long me) {
        boolean iAmOwner = t.getAccount().getId().equals(me);

        String direction;
        Long counterpartyId = null;
        Long fee = t.getFee() == null ? 0L : t.getFee();

        if (t.getType() == TransactionType.TRANSFER) {
            if (iAmOwner) {
                direction = "OUT";
                counterpartyId = t.getRelatedAccountId();
            } else {
                direction = "IN";
                counterpartyId = t.getAccount().getId();
                fee = 0L;
            }
        } else if (t.getType() == TransactionType.WITHDRAW) {
            direction = "OUT";
        } else {
            direction = "IN";
        }

        return new TransactionHistoryResponse.Item(
                t.getId(),
                direction,
                t.getType().name(),
                t.getAmount(),
                fee,
                counterpartyId,
                t.getCreatedAt()
        );
    }
}