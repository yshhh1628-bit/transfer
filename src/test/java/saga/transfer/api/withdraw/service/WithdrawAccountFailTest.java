package saga.transfer.api.withdraw.service;

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
@DisplayName("출금 동시성 실패 테스트")
class WithdrawAccountFailTest {
    @Autowired
    private WithdrawAccountService withdrawAccountService;

    @Autowired
    private AccountRepository accountRepository;

    private Long accountId;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .accountNumber("444-555-666")
                .ownerName("테스트 유저")
                .balance(BigDecimal.valueOf(2_000_000))
                .deleted(false)
                .build();
        accountRepository.save(account);
        accountId = account.getId();
    }

    @Test
    void 출금요청_10회_5회_실패() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        int[] successCount = {0};
        int[] failureCount = {0};

        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                try {
                    withdrawAccountService.withdraw(accountId, BigDecimal.valueOf(200_000));
                    synchronized (successCount) {
                        successCount[0]++;
                    }
                } catch (Exception e) {
                    synchronized (failureCount) {
                        failureCount[0]++;
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(successCount[0]).isEqualTo(5);
        assertThat(failureCount[0]).isEqualTo(5);

        Account account = accountRepository.findById(accountId).orElseThrow();
        assertThat(account.getBalance()).isEqualByComparingTo("1000000");
    }
}