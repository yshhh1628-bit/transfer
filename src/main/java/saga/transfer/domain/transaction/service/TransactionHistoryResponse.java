package saga.transfer.domain.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TransactionHistoryResponse(
        List<Item> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public record Item(
            Long id,
            String direction,
            String type,
            BigDecimal amount,
            Long fee,
            Long counterpartyAccountId,
            LocalDateTime createdAt
    ) {}
}