package com.rbs.casestudy.transferapi.rest;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.requests.AccountOpenRequest;
import com.rbs.casestudy.transferapi.service.AccountService;

/**
 * Provides REST endpoints related to the {@link Account} entity
 * @author ed
 */
@RestController
@RequestMapping("/accounts")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Page<Account> listAccounts(Pageable page) {
        return accountService.listAccounts(page);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.of(accountService.getAccount(accountNumber));
    }

    @PostMapping
    public ResponseEntity<?> openAccount(@RequestBody @Valid AccountOpenRequest acc, 
            UriComponentsBuilder b) {

        Account a = accountService.openAccount(acc);

        UriComponents uriComponents = 
                b.path("/accounts/{id}").buildAndExpand(a.getAccountNumber());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/{accountNumber}")
    public HttpStatus closeAccount(@PathVariable String accountNumber) {
        return HttpStatus.NOT_IMPLEMENTED;
    }
}
