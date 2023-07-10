package kz.inspiredsamat.qazpay.service.impl;

import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.Transfer;
import kz.inspiredsamat.qazpay.repository.AccountRepository;
import kz.inspiredsamat.qazpay.repository.TransferRepository;
import kz.inspiredsamat.qazpay.service.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class TransferService implements ITransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Override
    public Transfer makeTransfer(Transfer transferBody) {
        Transfer savedTransfer = transferRepository.save(transferBody);

        Account from = accountRepository.findById(transferBody.getFromAccountId()).get();
        if (from.getBalance() < transferBody.getTransferAmount())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Account to = accountRepository.findById(transferBody.getToAccountId()).get();
        from.setBalance(from.getBalance() - transferBody.getTransferAmount());
        to.setBalance(to.getBalance() + transferBody.getTransferAmount());

        accountRepository.saveAndFlush(from);
        accountRepository.saveAndFlush(to);

        return savedTransfer;
    }
}
