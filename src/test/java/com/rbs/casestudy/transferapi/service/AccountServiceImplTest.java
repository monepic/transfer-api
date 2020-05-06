package com.rbs.casestudy.transferapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.repo.AccountRepository;
import com.rbs.casestudy.transferapi.requests.AccountOpenRequest;
import com.rbs.casestudy.transferapi.service.ex.ServiceException;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock private AccountRepository accountRepo;

    @InjectMocks AccountServiceImpl as;

    @Test
    public void testOpenAccThrowExIfAccExists() {
        when(accountRepo.existsById("exists"))
        .thenReturn(true);

        assertThrows(ServiceException.class, () ->
        as.openAccount(newAccountOpenRequest("exists", "100"))
                );
    }

    @Test
    public void returnsAccountIfAllOk() {
        when(accountRepo.existsById("acc"))
        .thenReturn(false);

        when(accountRepo.save(any()))
        .then(returnsFirstArg());

        Account a = as.openAccount(newAccountOpenRequest("acc", "100"));
        assertNotNull(a);
        assertEquals("acc", a.getAccountNumber());
        assertEquals(new BigDecimal("100"), a.getBalance());
    }

    private static AccountOpenRequest newAccountOpenRequest(String accountNumber, String openingBalance) {
        AccountOpenRequest r = new AccountOpenRequest();
        r.setAccountNumber(accountNumber);
        r.setOpeningBalance(new BigDecimal(openingBalance));
        return r;
    }
}
