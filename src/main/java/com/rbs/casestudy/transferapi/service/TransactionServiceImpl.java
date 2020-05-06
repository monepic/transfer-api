package com.rbs.casestudy.transferapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.model.Transaction;
import com.rbs.casestudy.transferapi.repo.AccountRepository;
import com.rbs.casestudy.transferapi.repo.TransactionRepository;
import com.rbs.casestudy.transferapi.requests.TransferRequest;
import com.rbs.casestudy.transferapi.service.ex.TransferException;

/**
 * 
 * @author ed
 *
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<Transaction> listTransactions(Pageable p) {
        return transactionRepository.findAll(p);
    }

    @Override
    public Optional<Transaction> getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    @Transactional
    public Transaction transfer(TransferRequest tr) {

        Account source = accountRepository.findById(tr.getSourceAccountNumber())
                .orElseThrow(() -> new TransferException(
                        String.format("Source account \"%s\" invalid", tr.getSourceAccountNumber()))
                        );

        Account dest = accountRepository.findById(tr.getDestinationAccountNumber())
                .orElseThrow(() -> new TransferException(
                        String.format("Destination account \"%s\" invalid", tr.getSourceAccountNumber()))
                        );

        if(tr.getAmount().compareTo(source.getAvailableFunds()) > 0) {
            throw new TransferException("Source account has insufficient funds");
        }

        source.debit(tr.getAmount());
        dest.credit(tr.getAmount());

        Transaction nt = new Transaction();
        nt.setSourceAccount(source);
        nt.setDestinationAccount(dest);
        nt.setAmount(tr.getAmount());

        return transactionRepository.save(nt);
    }
}
