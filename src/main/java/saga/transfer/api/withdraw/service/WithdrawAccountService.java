package saga.transfer.api.withdraw.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.account.service.AccountDomainService;
import saga.transfer.domain.transaction.Transaction;
import saga.transfer.domain.transaction.service.TransactionDomainService;
import saga.transfer.domain.transaction.service.policy.WithdrawLimitPolicy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawAccountService {
    private final AccountDomainService accountDomainService;
    private final WithdrawLimitPolicy withdrawLimitPolicy;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = accountDomainService.findByIdWithLock(accountId);

        withdrawLimitPolicy.validate(account, amount, LocalDate.now());

        account.withdraw(amount);
        transactionDomainService.save(Transaction.withdraw(account, amount, LocalDateTime.now()));
    }
}
