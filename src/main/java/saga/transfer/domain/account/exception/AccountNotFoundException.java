package saga.transfer.domain.account.exception;

import saga.transfer.exception.BusinessException;
import saga.transfer.exception.ErrorCode;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(Long accountId) {
        super(
                String.format("계좌(ID: %d)를 찾을 수 없습니다.", accountId),
                ErrorCode.ACCOUNT_NOT_FOUND.getStatus(),
                ErrorCode.ACCOUNT_NOT_FOUND.getCode()
        );
    }
}
