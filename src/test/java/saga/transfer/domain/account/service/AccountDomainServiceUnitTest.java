package saga.transfer.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.account.AccountNumberGenerator;
import saga.transfer.domain.account.AccountRepository;
import saga.transfer.domain.account.exception.AccountNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class AccountDomainServiceUnitTest {
    @Mock
    private AccountCreatePolicy accountCreatePolicy;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private AccountDomainService service;

    private final String OWNER = "Help";
    private final String GENERATED_NO = "123-456-789";

    @BeforeEach
    void setUp() {
        when(accountNumberGenerator.generate()).thenReturn(GENERATED_NO);
    }

    @Test
    void createAccount() {
        // given
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> {
            Account a = inv.getArgument(0);
            return Account.builder()
                    .id(1L)
                    .ownerName(a.getOwnerName())
                    .accountNumber(a.getAccountNumber())
                    .balance(a.getBalance())
                    .deleted(a.getDeleted())
                    .build();
        });

        // when
        Account created = service.createAccount(OWNER);

        // then
        verify(accountNumberGenerator).generate();
        verify(accountCreatePolicy).validateCreatable(GENERATED_NO);
        verify(accountRepository).save(captor.capture());

        Account toSave = captor.getValue();
        assertThat(toSave.getOwnerName()).isEqualTo(OWNER);
        assertThat(toSave.getAccountNumber()).isEqualTo(GENERATED_NO);
        assertThat(toSave.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(toSave.getDeleted()).isFalse();

        assertThat(created.getId()).isEqualTo(1L);
    }

    @Test
    void findByIdWithLock_found_and_notFound() {
        // given
        Account account = createAccount(10L);
        when(accountRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(account));
        assertThat(service.findByIdWithLock(10L)).isSameAs(account);
        when(accountRepository.findByIdForUpdate(11L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.findByIdWithLock(11L))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void deleteAccount_success() {
        // given
        Account account = createAccount(100L);
        when(accountRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(account));

        // when
        service.deleteAccount(100L);

        // then
        assertThat(account.getDeleted()).isTrue();
    }

    private Account createAccount(Long id) {
        return Account.builder()
                .id(id)
                .ownerName(OWNER)
                .accountNumber(GENERATED_NO)
                .balance(BigDecimal.ZERO)
                .deleted(false)
                .build();
    }
}