package saga.transfer.api.withdraw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saga.transfer.api.withdraw.controller.dto.WithdrawAccountRequest;
import saga.transfer.api.withdraw.service.WithdrawAccountService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class WithdrawAccountController {
    private final WithdrawAccountService service;

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Void> withdraw(
            @PathVariable Long accountId,
            @RequestBody WithdrawAccountRequest request
    ) {
        BigDecimal amount = request.amount();
        service.withdraw(accountId, amount);
        return ResponseEntity.ok().build();
    }
}
