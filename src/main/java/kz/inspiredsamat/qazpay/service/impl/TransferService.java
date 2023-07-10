package kz.inspiredsamat.qazpay.service.impl;

import kz.inspiredsamat.qazpay.exception.InsufficientFundsException;
import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.Transfer;
import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.TransferDTO;
import kz.inspiredsamat.qazpay.repository.AccountRepository;
import kz.inspiredsamat.qazpay.repository.TransferRepository;
import kz.inspiredsamat.qazpay.repository.UserRepository;
import kz.inspiredsamat.qazpay.service.ITransferService;
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
public class TransferService implements ITransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    @Override
    public TransferDTO makeTransfer(Transfer transferBody) {
        User authorizedUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        Optional<Account> fromAccountOptional = accountRepository.findById(transferBody.getFromAccountId());
        if (fromAccountOptional.isEmpty()) {
            log.error("From account with id {} does not exist", transferBody.getFromAccountId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<Account> toAccountOptional = accountRepository.findById(transferBody.getToAccountId());
        if (toAccountOptional.isEmpty()) {
            log.error("To account with id {} does not exist", transferBody.getToAccountId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Account from = fromAccountOptional.get();
        if (!from.getAccountOwnerId().equals(authorizedUser.getId())) {
            log.error("User with id {} is not authorized to make this transfer", authorizedUser.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (from.getBalance() < transferBody.getTransferAmount()) {
            log.error("Account with id {} does not have enough balance to make transfer", from.getId());
            throw new InsufficientFundsException("Not enough balance");
        }

        Account to = toAccountOptional.get();
        from.setBalance(from.getBalance() - transferBody.getTransferAmount());
        to.setBalance(to.getBalance() + transferBody.getTransferAmount());

        accountRepository.saveAndFlush(from);
        accountRepository.saveAndFlush(to);

        Transfer savedTransfer = transferRepository.save(transferBody);
        log.info("Transfer with id {} was successfully made", savedTransfer.getId());

        return entityToDTO(savedTransfer);
    }

    @Override
    public TransferDTO getTransferById(Long id) {
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);
        if (optionalTransfer.isEmpty()) {
            log.error("Transfer with id {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return entityToDTO(optionalTransfer.get());
    }

    private TransferDTO entityToDTO(Transfer transfer) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(transfer.getId());
        transferDTO.setTransferAmount(transfer.getTransferAmount());
        transferDTO.setFromAccountId(transfer.getFromAccountId());
        transferDTO.setToAccountId(transfer.getToAccountId());

        return transferDTO;
    }
}
