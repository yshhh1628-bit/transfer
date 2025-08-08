package saga.transfer.domain.account.exception;

import org.springframework.http.HttpStatus;
import saga.transfer.exception.BusinessException;

public class InvalidAmountException extends BusinessException {
    public InvalidAmountException() {
        super("0원 이하 금액은 처리할 수 없습니다.",
                HttpStatus.BAD_REQUEST,
                "INVALID_AMOUNT");
    }
}