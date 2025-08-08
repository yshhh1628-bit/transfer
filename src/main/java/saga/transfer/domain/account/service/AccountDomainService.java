package saga.transfer.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.account.AccountNumberGenerator;
import saga.transfer.domain.account.AccountRepository;
import saga.transfer.domain.account.exception.AccountNotFoundException;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountDomainService {
    private final AccountCreatePolicy accountCreatePolicy;
    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public Account createAccount(String ownerName) {
        String accountNumber = accountNumberGenerator.generate();

        accountCreatePolicy.validateCreatable(accountNumber);

        Account account = Account.create(accountNumber, ownerName);
        return accountRepository.save(account);
    }

    private Account getOrThrow(Optional<Account> optional, Long accountId) {
        return optional.orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public Account findByIdWithLock(Long accountId) {
        return getOrThrow(accountRepository.findByIdForUpdate(accountId), accountId);
    }

    public void deleteAccount(Long accountId) {
        Account account = findByIdWithLock(accountId);
        account.delete();
    }
}
