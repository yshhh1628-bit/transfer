package saga.transfer.domain.transaction.exception;

import saga.transfer.exception.BusinessException;
import saga.transfer.exception.ErrorCode;

public class DailyWithdrawLimitExceededException extends BusinessException {
    public DailyWithdrawLimitExceededException(Long accountId) {
        super(
                String.format(
                        "계좌[%d]의 오늘 출금 누적 금액이 1일 한도를 초과했습니다.",
                        accountId
                ),
                ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED.getStatus(),
                ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED.getCode()
        );
    }
}