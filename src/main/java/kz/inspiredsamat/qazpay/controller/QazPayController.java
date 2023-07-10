package kz.inspiredsamat.qazpay.controller;

import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.Transfer;
import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.UserDTO;
import kz.inspiredsamat.qazpay.service.IAccountService;
import kz.inspiredsamat.qazpay.service.ITransferService;
import kz.inspiredsamat.qazpay.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/qazpay/api")
@RequiredArgsConstructor
public class QazPayController {

    private final IUserService userService;
    private final ITransferService transferService;
    private final IAccountService accountService;

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody User newUserBody) {
        UserDTO createdUser = userService.createNewUser(newUserBody);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/" + createdUser.getId()).toUriString());
        return ResponseEntity.created(uri).body(createdUser);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transfer> makeTransfer(@RequestBody Transfer transferBody) {
        return ResponseEntity.ok(transferService.makeTransfer(transferBody));
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createNewAccount(@RequestBody Account newAccountBody) {
        Account createdAccount = accountService.createNewAccount(newAccountBody);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/accounts/" + createdAccount.getId()).toUriString());
        return ResponseEntity.created(uri).body(newAccountBody);
    }
}