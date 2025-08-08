package saga.transfer.domain.transaction.exception;

import org.springframework.http.HttpStatus;
import saga.transfer.exception.BusinessException;

public class InvalidTransactionAmountException extends BusinessException {
    public InvalidTransactionAmountException() {
        super("거래 금액이 0 이하입니다.", HttpStatus.BAD_REQUEST, "TX_INVALID_AMOUNT");
    }
}