package saga.transfer.domain.transaction.exception;

import saga.transfer.exception.BusinessException;
import saga.transfer.exception.ErrorCode;

public class DailyTransferLimitExceededException extends BusinessException {
    public DailyTransferLimitExceededException(Long accountId) {
        super(
                String.format("계좌[%d]는 1일 이체 한도(₩3,000,000)를 초과했습니다.", accountId),
                ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED.getStatus(),
                ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED.getCode()
        );
    }
}
