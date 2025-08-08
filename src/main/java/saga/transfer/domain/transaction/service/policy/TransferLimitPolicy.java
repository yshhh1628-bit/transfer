package saga.transfer.domain.transaction.service.policy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.transaction.Transaction;
import saga.transfer.domain.transaction.TransactionRepository;
import saga.transfer.domain.transaction.exception.DailyTransferLimitExceededException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransferLimitPolicy {
    private static final BigDecimal DAILY_LIMIT = BigDecimal.valueOf(3_000_000);
    private final TransactionRepository transactionRepository;

    public void validate(Account account, BigDecimal requestAmount, LocalDate now) {
        List<Transaction> todayTransfers = transactionRepository.findTransfersTodayWithLock(account.getId(), now);

        BigDecimal todayTotal = todayTransfers.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (todayTotal.add(requestAmount).compareTo(DAILY_LIMIT) > 0) {
            throw new DailyTransferLimitExceededException(account.getId());
        }
    }
}