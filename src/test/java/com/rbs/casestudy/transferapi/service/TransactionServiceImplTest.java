package com.rbs.casestudy.transferapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.model.TestUtils;
import com.rbs.casestudy.transferapi.model.Transaction;
import com.rbs.casestudy.transferapi.repo.AccountRepository;
import com.rbs.casestudy.transferapi.repo.TransactionRepository;
import com.rbs.casestudy.transferapi.requests.TransferRequest;
import com.rbs.casestudy.transferapi.service.ex.ServiceException;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock private AccountRepository accountRepo;
    @Mock private TransactionRepository transactionRepo;

    @InjectMocks TransactionServiceImpl ts;

    @Test
    public void testThrowsExIfsrcAccMissing() {
        when(accountRepo.findById("missing"))
        .thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () ->
        ts.transfer(newTransferRequest("missing", "destination", "100"))
                );
    }

    @Test
    public void testThrowsExIfdestAccMissing() {
        when(accountRepo.findById("missing"))
        .thenReturn(Optional.empty());
        when(accountRepo.findById("source"))
        .thenReturn(Optional.of(TestUtils.TEST_ACCOUNT_1));

        assertThrows(ServiceException.class, () ->
        ts.transfer(newTransferRequest("source", "missing", "100"))
                );
    }

    @Test
    public void testThrowsExIfInsufficientFunds() {
        when(accountRepo.findById("src"))
        .thenReturn(Optional.of(TestUtils.TEST_ACCOUNT_1));
        when(accountRepo.findById("dest"))
        .thenReturn(Optional.of(TestUtils.TEST_ACCOUNT_1));

        assertThrows(ServiceException.class, () ->
        ts.transfer(newTransferRequest("src", "dest", "200.01"))
                );
    }

    @Test
    public void transferWorksIfAllGood() {
        Account src = TestUtils.newAccount("src", "200"),
                dest = TestUtils.newAccount("dest", "100.41");

        when(accountRepo.findById("src"))
        .thenReturn(Optional.of(src));
        when(accountRepo.findById("dest"))
        .thenReturn(Optional.of(dest));

        ArgumentCaptor<Transaction> tCaptor = ArgumentCaptor.forClass(Transaction.class);

        //call the method
        ts.transfer(newTransferRequest("src", "dest", "100"));

        verify(transactionRepo, times(1)).save(tCaptor.capture());

        Transaction t = tCaptor.getValue();
        assertEquals( new BigDecimal("100"), t.getAmount());
        assertEquals(src, t.getSourceAccount());
        assertEquals(dest, t.getDestinationAccount());

        assertEquals(new BigDecimal("100"), src.getBalance());
        assertEquals(new BigDecimal("200.41"), dest.getBalance());
    }

    private static TransferRequest newTransferRequest(String src, String dest, String amount) {
        TransferRequest tr = new TransferRequest();
        tr.setSourceAccountNumber(src);
        tr.setDestinationAccountNumber(dest);
        tr.setAmount(new BigDecimal(amount));
        return tr;
    }
}
