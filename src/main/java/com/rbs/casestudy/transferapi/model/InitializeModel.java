package com.rbs.casestudy.transferapi.model;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rbs.casestudy.transferapi.repo.AccountRepository;
import com.rbs.casestudy.transferapi.repo.TransactionRepository;

/**
 * This will initalize the datastore with some dummy data<br>
 * if you start the app with the spring profile
 * <b>test-data</b>
 * @author ed
 *
 */
@Component
//@Profile("test-data")
public class InitializeModel implements CommandLineRunner {

    @Autowired AccountRepository ar;
    @Autowired TransactionRepository tr;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Account first = new Account("account_1",new BigDecimal("2000.04"));
        Account second = new Account("account_2", new BigDecimal("3000.51"));
        ar.save(first);
        ar.save(second);

        tr.save(new Transaction(1L, first, second, new BigDecimal("10.02")));
    }
}
