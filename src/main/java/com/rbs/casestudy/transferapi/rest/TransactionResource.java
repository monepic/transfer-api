package com.rbs.casestudy.transferapi.rest;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.rbs.casestudy.transferapi.model.Transaction;
import com.rbs.casestudy.transferapi.requests.TransferRequest;
import com.rbs.casestudy.transferapi.service.TransactionService;

/**
 * Provides REST endpoints related to the {@link Transaction} entity 
 * and performing balance transfers
 * 
 * @author ed
 *
 */
@RestController
public class TransactionResource {

    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public Page<Transaction> listTransactions(Pageable page) {
        return transactionService.listTransactions(page);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable long id) {
        return ResponseEntity.of(transactionService.getTransaction(id));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody @Valid TransferRequest transferRequest, UriComponentsBuilder b) {

        Transaction t = transactionService.transfer(transferRequest);

        UriComponents uriComponents = 
                b.path("/transactions/{id}").buildAndExpand(t.getId());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
