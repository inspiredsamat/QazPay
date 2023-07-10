package kz.inspiredsamat.qazpay.service;

import kz.inspiredsamat.qazpay.model.Transfer;

public interface ITransferService {

    Transfer makeTransfer(Transfer transferBody);
}
