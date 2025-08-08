package saga.transfer.api.withdraw.controller.dto;

import java.math.BigDecimal;

public record WithdrawAccountRequest(BigDecimal amount) {}

