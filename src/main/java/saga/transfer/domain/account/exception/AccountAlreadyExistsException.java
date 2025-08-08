package saga.transfer.domain.account.exception;

import saga.transfer.exception.BusinessException;
import saga.transfer.exception.ErrorCode;

public class AccountAlreadyExistsException extends BusinessException {
    public AccountAlreadyExistsException() {
        super(
                ErrorCode.ACCOUNT_ALREADY_EXISTS.getMessage(),
                ErrorCode.ACCOUNT_ALREADY_EXISTS.getStatus(),
                ErrorCode.ACCOUNT_ALREADY_EXISTS.getCode()
        );
    }
}
