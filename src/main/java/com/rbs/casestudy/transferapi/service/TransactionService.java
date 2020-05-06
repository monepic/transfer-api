package com.rbs.casestudy.transferapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rbs.casestudy.transferapi.model.Transaction;
import com.rbs.casestudy.transferapi.requests.TransferRequest;

/**
 * 
 * @author ed
 *
 */
public interface TransactionService {

    Page<Transaction> listTransactions(Pageable p);
    Optional<Transaction> getTransaction(Long transactionId);
    Transaction transfer(TransferRequest transferRequest);
}
