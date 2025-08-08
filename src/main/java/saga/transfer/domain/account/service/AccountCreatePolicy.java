package saga.transfer.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import saga.transfer.domain.account.AccountRepository;
import saga.transfer.domain.account.exception.AccountAlreadyExistsException;

@Component
@RequiredArgsConstructor
public class AccountCreatePolicy {
    private final AccountRepository accountRepository;

    public void validateCreatable(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new AccountAlreadyExistsException();
        }
    }
}
