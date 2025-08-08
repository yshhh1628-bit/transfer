package saga.transfer.api.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saga.transfer.domain.transaction.service.TransactionHistoryResponse;
import saga.transfer.domain.transaction.service.TransactionQueryService;

@Tag(name = "Transactions", description = "계좌 거래내역 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionQueryController {
    private final TransactionQueryService transactionQueryService;

    @Operation(summary = "거래내역 조회 (최신순)")
    @GetMapping
    public TransactionHistoryResponse getHistory(
            @Parameter(description = "계좌 ID", example = "1")
            @PathVariable Long accountId,
            @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC)
            Pageable pageable
    ) {
        return transactionQueryService.getHistory(accountId, pageable);
    }
}