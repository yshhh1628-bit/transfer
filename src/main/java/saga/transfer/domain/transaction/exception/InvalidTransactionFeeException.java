package saga.transfer.domain.transaction.exception;

import org.springframework.http.HttpStatus;
import saga.transfer.exception.BusinessException;

public class InvalidTransactionFeeException extends BusinessException {
    public InvalidTransactionFeeException() {
        super("거래 수수료가 0 미만입니다.", HttpStatus.BAD_REQUEST, "TX_INVALID_FEE");
    }
}