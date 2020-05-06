package com.rbs.casestudy.transferapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.requests.AccountOpenRequest;

public interface AccountService {

    Account openAccount(AccountOpenRequest req);
    Page<Account> listAccounts(Pageable page);
    Optional<Account> getAccount(String accountNumber);
}
