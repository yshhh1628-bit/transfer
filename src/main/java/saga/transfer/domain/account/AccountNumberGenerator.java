package saga.transfer.domain.account;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberGenerator {

    public String generate() {
        String prefix = "110";
        String randomPart = String.format("%08d", new Random().nextInt(100_000_000));
        return prefix + randomPart;
    }
}