package saga.transfer.api.transfer;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.account.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("이체 동시성 테스트 (성공)")
@SpringBootTest
class TransferApplicationServiceSuccessTest {

    @Autowired
    private TransferApplicationService transferService;

    @Autowired
    private AccountRepository accountRepository;

    private Long fromAccountId;
    private Long toAccountId;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        Account from = Account.builder()
                .accountNumber("100-000-000")
                .ownerName("보내는사람")
                .balance(BigDecimal.valueOf(5_000_000))
                .deleted(false)
                .build();

        Account to = Account.builder()
                .accountNumber("200-000-000")
                .ownerName("받는사람")
                .balance(BigDecimal.ZERO)
                .deleted(false)
                .build();

        accountRepository.save(from);
        accountRepository.save(to);
        fromAccountId = from.getId();
        toAccountId = to.getId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 동시에_여러건_이체_정상() throws InterruptedException {
        int threadCount = 5;
        BigDecimal transferAmount = BigDecimal.valueOf(500_000);
        BigDecimal fee = transferAmount.multiply(BigDecimal.valueOf(0.01)).setScale(0, RoundingMode.DOWN);

        BigDecimal totalFee = fee.multiply(BigDecimal.valueOf(threadCount));
        BigDecimal totalTransfer = transferAmount.multiply(BigDecimal.valueOf(threadCount));

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            pool.execute(() -> {
                try {
                    transferService.transfer(fromAccountId, toAccountId, transferAmount);
                } catch (Exception e) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Account from = accountRepository.findById(fromAccountId).orElseThrow();
        Account to = accountRepository.findById(toAccountId).orElseThrow();

        assertThat(to.getBalance()).isEqualByComparingTo(totalTransfer);
        assertThat(from.getBalance()).isEqualByComparingTo(
                BigDecimal.valueOf(5_000_000).subtract(totalTransfer).subtract(totalFee)
        );
    }
}