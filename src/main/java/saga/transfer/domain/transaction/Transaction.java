package saga.transfer.domain.transaction;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.common.BaseEntity;
import saga.transfer.domain.transaction.exception.InvalidTransactionAmountException;
import saga.transfer.domain.transaction.exception.InvalidTransactionFeeException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal fee;

    private Long relatedAccountId;

    public static Transaction transfer(Account from, Account to, BigDecimal amount, BigDecimal fee, LocalDateTime createdAt) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException();
        }
        if (fee == null || fee.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionFeeException();
        }

        return Transaction.builder()
                .account(from)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .fee(fee)
                .relatedAccountId(to.getId())
                .build();
    }

    public static Transaction withdraw(Account account, BigDecimal amount, LocalDateTime createdAt) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException();
        }

        return Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAW)
                .amount(amount)
                .fee(BigDecimal.ZERO)
                .build();
    }

}