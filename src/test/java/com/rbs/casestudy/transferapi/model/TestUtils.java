package com.rbs.casestudy.transferapi.model;

import java.math.BigDecimal;

public final class TestUtils {

    private TestUtils() {}

    public static final Account TEST_ACCOUNT_1 = new Account("one", new BigDecimal("200.00"));
    public static final String TEST_ACCOUNT_1_JSON = "{\"accountNumber\":\"one\",\"balance\":200.00}";

    public static final Account TEST_ACCOUNT_2 = new Account("two", new BigDecimal("100.41"));
    public static final String TEST_ACCOUNT_2_JSON = "{\"accountNumber\":\"two\",\"balance\":100.41}";

    public static final Transaction EXPECTED_TRANSACTION = 
            new Transaction(1L, TEST_ACCOUNT_1, TEST_ACCOUNT_2, new BigDecimal("100"));

    public static final Transaction TEST_TRANSACTION_1 = 
            new Transaction(1L, TEST_ACCOUNT_1, TEST_ACCOUNT_2,
                    new BigDecimal("58.30"));

    public static final String TEST_TRANSACTION_1_JSON = 
            "{\"id\":1,\"sourceAccount\":\"one\",\"destinationAccount\":\"two\",\"amount\":58.30,\"transactionTime\":null}";

    public static final String TR_NO_SOURCE_ACCOUNT = "{\"destinationAccountNumber\":\"two\",\"amount\":10}";
    public static final String TR_NO_SOURCE_ACCOUNT_RESPONSE = "{\"fieldErrors\":[{\"field\":\"sourceAccountNumber\",\"message\":\"must not be empty\",\"rejectedValue\":null}]}";

    public static final String OPEN_ACC_NO_ACCOUNT = "{\"openingBalance\":10}";
    public static final String OPEN_ACC_NO_ACCOUNT_RESPONSE ="{\"fieldErrors\":[{\"field\":\"accountNumber\",\"message\":\"must not be null\",\"rejectedValue\":null}]}";

    public static String transferRequest(String src, String dest, String amount) {
        return String.format("{\"sourceAccountNumber\":\"%s\",\"destinationAccountNumber\":\"%s\",\"amount\":%s}", src, dest, amount);
    }

    public static String openAccount(String accountNumber, String openingBalance) {
        return String.format("{\"accountNumber\":\"%s\",\"openingBalance\":%s}", accountNumber, openingBalance);
    }

    public static Account newAccount(String accountNumber, String openingBalance) {
        return new Account(accountNumber, new BigDecimal(openingBalance));
    }
}