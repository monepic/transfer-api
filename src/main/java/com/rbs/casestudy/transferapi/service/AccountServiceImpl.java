package com.rbs.casestudy.transferapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.repo.AccountRepository;
import com.rbs.casestudy.transferapi.requests.AccountOpenRequest;
import com.rbs.casestudy.transferapi.service.ex.AccountCreationException;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) { 
        this.accountRepository = accountRepository;
    }

    @Override
    public Account openAccount(AccountOpenRequest req) {
        if(accountRepository.existsById(req.getAccountNumber())) {
            throw new AccountCreationException(String.format("Account \"%s\" already exists", req.getAccountNumber()));
        }

        Account na = new Account();
        na.setAccountNumber(req.getAccountNumber());
        na.setBalance(req.getOpeningBalance());
        return accountRepository.save(na);
    }

    @Override
    public Page<Account> listAccounts(Pageable page) {
        return accountRepository.findAll(page);
    }

    @Override
    public Optional<Account> getAccount(String accountId) {
        return accountRepository.findById(accountId);
    }
}
