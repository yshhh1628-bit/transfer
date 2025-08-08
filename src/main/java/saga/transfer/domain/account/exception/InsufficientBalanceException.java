package saga.transfer.domain.account.exception;

import org.springframework.http.HttpStatus;
import saga.transfer.exception.BusinessException;

public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException() {
        super("잔액이 부족합니다.",
                HttpStatus.BAD_REQUEST,
                "INSUFFICIENT_BALANCE");
    }
}