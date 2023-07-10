package kz.inspiredsamat.qazpay.service;

import kz.inspiredsamat.qazpay.model.Transfer;
import kz.inspiredsamat.qazpay.model.dto.TransferDTO;

public interface ITransferService {

    TransferDTO makeTransfer(Transfer transferBody);
    TransferDTO getTransferById(Long id);
}
