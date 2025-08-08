package saga.transfer.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ACCOUNT_ALREADY_EXISTS("A001", "이미 존재하는 계좌입니다.", HttpStatus.CONFLICT),
    ACCOUNT_NOT_FOUND("A002", "존재하지 않는 계좌입니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_ALREADY_DELETED("A003", "이미 삭제된 계좌입니다.", HttpStatus.BAD_REQUEST),
    DAILY_WITHDRAW_LIMIT_EXCEEDED("ACCT_004", "1일 출금 한도 초과", HttpStatus.BAD_REQUEST),
    DAILY_TRANSFER_LIMIT_EXCEEDED("A005", "1일 이체 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),

    ;
    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}