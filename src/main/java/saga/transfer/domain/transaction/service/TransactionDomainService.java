package saga.transfer.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import saga.transfer.domain.transaction.Transaction;
import saga.transfer.domain.transaction.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionDomainService {
    private final TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
