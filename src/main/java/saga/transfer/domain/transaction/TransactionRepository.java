package saga.transfer.domain.transaction;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.account.id = :accountId
              AND t.type = 'WITHDRAW'
              AND DATE(t.createdAt) = :date
    """)
    Long sumWithdrawAmountToday(@Param("accountId") Long accountId, @Param("date") LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.account.id = :accountId
      AND t.type = 'WITHDRAW'
      AND DATE(t.createdAt) = :date
""")
    List<Transaction> findWithdrawsTodayWithLock(@Param("accountId") Long accountId,
                                                 @Param("date") LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.account.id = :accountId
          AND t.type = 'TRANSFER'
          AND DATE(t.createdAt) = :date
    """)
    List<Transaction> findTransfersTodayWithLock(Long id, LocalDate now);

    @Query("""
        SELECT t
          FROM Transaction t
         WHERE t.account.id = :accountId
            OR (t.type = saga.transfer.domain.transaction.TransactionType.TRANSFER
                AND t.relatedAccountId = :accountId)
    """)
    Page<Transaction> findHistory(@Param("accountId") Long accountId, Pageable pageable);
}
