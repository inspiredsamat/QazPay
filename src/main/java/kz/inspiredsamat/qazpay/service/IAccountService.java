package kz.inspiredsamat.qazpay.service;

import kz.inspiredsamat.qazpay.model.Account;

public interface IAccountService {

    Account createNewAccount(Account newAccountBody);
}
