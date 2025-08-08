package saga.transfer.domain.transaction.service.policy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.transaction.Transaction;
import saga.transfer.domain.transaction.TransactionRepository;
import saga.transfer.domain.transaction.exception.DailyWithdrawLimitExceededException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WithdrawLimitPolicy {
    private static final BigDecimal DAILY_LIMIT = BigDecimal.valueOf(1_000_000);
    private final TransactionRepository transactionRepository;

    public void validate(Account account, BigDecimal requestAmount,  LocalDate date) {
        List<Transaction> todayWithdraws = transactionRepository
                .findWithdrawsTodayWithLock(account.getId(), date);

        BigDecimal todayTotal = todayWithdraws.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal newTotal = todayTotal.add(requestAmount);

        if (newTotal.compareTo(DAILY_LIMIT) > 0) {
            throw new DailyWithdrawLimitExceededException(account.getId());
        }
    }
}
