package kz.inspiredsamat.qazpay.service.impl;

import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.AccountDTO;
import kz.inspiredsamat.qazpay.repository.AccountRepository;
import kz.inspiredsamat.qazpay.repository.UserRepository;
import kz.inspiredsamat.qazpay.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountDTO createNewAccount(Account newAccountBody) {
        User authorizedUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        newAccountBody.setAccountOwnerId(authorizedUser.getId());

        if (newAccountBody.getBalance() == null)
            newAccountBody.setBalance(0L);

        Account createdAccount = accountRepository.save(newAccountBody);
        log.info("Account with id {} and owner id {} was created", createdAccount.getId(), createdAccount.getAccountOwnerId());

        return entityToDTO(createdAccount);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            log.error("Account with id {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return entityToDTO(optionalAccount.get());
    }

    private AccountDTO entityToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setAccountOwnerId(account.getAccountOwnerId());
        accountDTO.setCardNumber(account.getCardNumber());
        accountDTO.setBalance(account.getBalance());

        return accountDTO;
    }
}
