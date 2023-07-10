package kz.inspiredsamat.qazpay.service.impl;

import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.repository.AccountRepository;
import kz.inspiredsamat.qazpay.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createNewAccount(Account newAccountBody) {
        return accountRepository.save(newAccountBody);
    }
}
