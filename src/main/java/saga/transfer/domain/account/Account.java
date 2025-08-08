package saga.transfer.domain.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saga.transfer.domain.account.exception.InsufficientBalanceException;
import saga.transfer.domain.account.exception.InvalidAmountException;
import saga.transfer.domain.common.BaseEntity;
import saga.transfer.domain.common.BooleanToYNConverter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean deleted;

    public static Account create(String accountNumber, String ownerName) {
        return Account.builder()
                .accountNumber(accountNumber)
                .ownerName(ownerName)
                .balance(BigDecimal.ZERO)
                .deleted(false)
                .build();
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        this.balance = this.balance.subtract(amount);
    }

    public void delete() {
        this.deleted = true;
    }
}