package com.rbs.casestudy.transferapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.rbs.casestudy.transferapi.model.Account;
import com.rbs.casestudy.transferapi.requests.AccountOpenRequest;
import com.rbs.casestudy.transferapi.requests.TransferRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate restTemplate;
    private String url(String suffix) {
        return "http://localhost:" + port + "/" + suffix;
    }
    @Test
    public void createAccountShouldCreateAnAccount() throws Exception {
        URI loc = restTemplate.postForLocation(url("accounts"), newAccount("test-account", "100"));
        assertNotNull(loc);
        assertTrue(loc.getPath().contains("test-account"));
    }

    @Test
    public void testE2eTransfer() throws Exception {
        URI srcLoc = restTemplate.postForLocation(url("accounts"), newAccount("source-account", "250"));
        URI destLoc = restTemplate.postForLocation(url("accounts"), newAccount("dest-account", "10.25"));
        assertNotNull(srcLoc);
        assertNotNull(destLoc);

        URI trnLoc = restTemplate.postForLocation(url("transfer"),
                newTransfer("source-account",  "dest-account", "125.10"));
        assertNotNull(trnLoc);

        Account src=restTemplate.getForObject(srcLoc, Account.class);
        assertNotNull(src);
        assertEquals(new BigDecimal("124.90"), src.getBalance());

        Account dest=restTemplate.getForObject(destLoc, Account.class);
        assertNotNull(dest);
        assertEquals(new BigDecimal("135.35"), dest.getBalance());
    }

    private static AccountOpenRequest newAccount(String accountNumber, String openingBalance) {
        AccountOpenRequest ao = new AccountOpenRequest();
        ao.setAccountNumber(accountNumber);
        ao.setOpeningBalance(new BigDecimal(openingBalance));
        return ao;
    }

    private static TransferRequest newTransfer(String src, String dest, String amount) {
        TransferRequest tr = new TransferRequest();
        tr.setSourceAccountNumber(src);
        tr.setDestinationAccountNumber(dest);
        tr.setAmount(new BigDecimal(amount));
        return tr;
    }
}