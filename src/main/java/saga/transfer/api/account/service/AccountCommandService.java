package saga.transfer.api.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saga.transfer.api.account.service.dto.CreateAccountCommand;
import saga.transfer.domain.account.service.AccountDomainService;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountCommandService {
    private final AccountDomainService accountDomainService;

    public void createAccount(CreateAccountCommand createAccountCommand) {
        accountDomainService.createAccount(createAccountCommand.ownerName());
    }

    public void deleteAccount(Long accountId) {
        accountDomainService.deleteAccount(accountId);
    }
}
