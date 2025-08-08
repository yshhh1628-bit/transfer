package saga.transfer.api.transfer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.account.service.AccountDomainService;
import saga.transfer.domain.transaction.Transaction;
import saga.transfer.domain.transaction.service.TransactionDomainService;
import saga.transfer.domain.transaction.service.policy.TransferLimitPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class TransferApplicationService {

    private final AccountDomainService accountDomainService;
    private final TransferLimitPolicy transferLimitPolicy;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountDomainService.findByIdWithLock(fromAccountId);
        Account toAccount = accountDomainService.findByIdWithLock(toAccountId);

        BigDecimal fee = calculateFee(amount);

        transferLimitPolicy.validate(fromAccount, amount.add(fee), LocalDate.now());

        fromAccount.withdraw(amount.add(fee));

        toAccount.deposit(amount);

        transactionDomainService.save(
                Transaction.transfer(fromAccount, toAccount, amount, fee, LocalDateTime.now())
        );
    }

    private BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.01)).setScale(0, RoundingMode.DOWN);
    }
}
