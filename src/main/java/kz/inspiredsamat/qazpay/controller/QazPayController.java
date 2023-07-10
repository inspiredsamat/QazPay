package kz.inspiredsamat.qazpay.controller;

import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.Transfer;
import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.AccountDTO;
import kz.inspiredsamat.qazpay.model.dto.TransferDTO;
import kz.inspiredsamat.qazpay.model.dto.UserDTO;
import kz.inspiredsamat.qazpay.service.IAccountService;
import kz.inspiredsamat.qazpay.service.ITransferService;
import kz.inspiredsamat.qazpay.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/qazpay/api")
@RequiredArgsConstructor
public class QazPayController {

    private final IUserService userService;
    private final ITransferService transferService;
    private final IAccountService accountService;

    @PostMapping("/users/register")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody User newUserBody) {
        UserDTO createdUser = userService.createNewUser(newUserBody);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/" + createdUser.getId()).toUriString());
        return ResponseEntity.created(uri).body(createdUser);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferDTO> makeTransfer(@RequestBody Transfer transferBody) {
        TransferDTO savedTransfer = transferService.makeTransfer(transferBody);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/transfers/" + savedTransfer.getId()).toUriString());
        return ResponseEntity.created(uri).body(savedTransfer);
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createNewAccount(@RequestBody Account newAccountBody) {
        AccountDTO createdAccount = accountService.createNewAccount(newAccountBody);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/accounts/" + createdAccount.getId()).toUriString());
        return ResponseEntity.created(uri).body(createdAccount);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/transfers/{id}")
    public ResponseEntity<TransferDTO> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }
}