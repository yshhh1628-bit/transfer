package saga.transfer.api.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import saga.transfer.domain.account.Account;
import saga.transfer.domain.account.AccountRepository;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("이체 동시성 테스트 (실패 - 한도초과)")
class TransferApplicationServiceFailTest {

    @Autowired
    private TransferApplicationService transferService;

    @Autowired
    private AccountRepository accountRepository;

    private Long fromAccountId;
    private Long toAccountId;

    @BeforeEach
    void setUp() {
        Account from = Account.builder()
                .accountNumber("300-000-000")
                .ownerName("보내는사람")
                .balance(BigDecimal.valueOf(10_000_000))
                .deleted(false)
                .build();

        Account to = Account.builder()
                .accountNumber("400-000-000")
                .ownerName("받는사람")
                .balance(BigDecimal.ZERO)
                .deleted(false)
                .build();

        accountRepository.save(from);
        accountRepository.save(to);
        fromAccountId = from.getId();
        toAccountId = to.getId();
    }

    @Test
    void 한도초과_이체_실패_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            pool.execute(() -> {
                try {
                    transferService.transfer(fromAccountId, toAccountId, BigDecimal.valueOf(400_000));
                } catch (Exception e) {
                    System.out.println("이체 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Account from = accountRepository.findById(fromAccountId).orElseThrow();
        Account to = accountRepository.findById(toAccountId).orElseThrow();

        System.out.println("최종 보내는 계좌 잔액: " + from.getBalance());
        System.out.println("최종 받는 계좌 잔액: " + to.getBalance());

        assertThat(to.getBalance()).isLessThan(new BigDecimal("4000000"));
    }
}