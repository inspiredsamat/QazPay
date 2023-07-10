package kz.inspiredsamat.qazpay.service;

import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.dto.AccountDTO;

public interface IAccountService {

    AccountDTO createNewAccount(Account newAccountBody);

    AccountDTO getAccountById(Long id);
}
