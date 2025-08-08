package saga.transfer.api.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saga.transfer.api.account.controller.dto.AccountCreateRequest;
import saga.transfer.api.account.service.AccountCommandService;
import saga.transfer.api.account.service.dto.CreateAccountCommand;

@Tag(name = "Accounts", description = "계좌 등록/삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountCommandController {
    private final AccountCommandService service;

    @Operation(summary = "계좌 등록")
    @PostMapping
    public void createAccount(@RequestBody AccountCreateRequest request) {
        service.createAccount(new CreateAccountCommand(request.ownerName()));
    }

    @Operation(summary = "계좌 삭제")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        service.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
