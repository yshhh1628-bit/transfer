package saga.transfer.domain.account;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberGenerator {
    public String generate() {
        String randomPart = String.format("%08d", new Random().nextInt(100_000_000));
        return "TEST_" + randomPart;
    }
}