package saga.transfer.domain.transaction.exception;

import org.springframework.http.HttpStatus;
import saga.transfer.exception.BusinessException;

public class TransactionNotFoundException extends BusinessException {
    public TransactionNotFoundException(Long id) {
        super("거래를 찾을 수 없습니다. id=" + id, HttpStatus.NOT_FOUND, "TX_NOT_FOUND");
    }
}