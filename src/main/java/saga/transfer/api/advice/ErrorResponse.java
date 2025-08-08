package saga.transfer.api.advice;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String code,
    String message,
    int status
) {
    public static ErrorResponse of(String code, String message, int status) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .build();
    }
}
