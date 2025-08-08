package saga.transfer.domain.account.exception;

import saga.transfer.exception.BusinessException;
import saga.transfer.exception.ErrorCode;

public class AccountAlreadyDeletedException extends BusinessException {
    public AccountAlreadyDeletedException() {
        super(
                ErrorCode.ACCOUNT_ALREADY_DELETED.getMessage(),
                ErrorCode.ACCOUNT_ALREADY_DELETED.getStatus(),
                ErrorCode.ACCOUNT_ALREADY_DELETED.getCode());
    }
}