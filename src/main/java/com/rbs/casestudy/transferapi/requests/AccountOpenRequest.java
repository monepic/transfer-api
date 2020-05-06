package com.rbs.casestudy.transferapi.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbs.casestudy.transferapi.rest.serializers.MoneyDeserializer;

public class AccountOpenRequest {

    @NotNull
    @Pattern(regexp = "[0-9A-Za-z_-]+")
    private String accountNumber;

    @NotNull
    @Positive
    @JsonDeserialize(using=MoneyDeserializer.class)
    private BigDecimal openingBalance;

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountNumber() { return accountNumber; }

    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
    public BigDecimal getOpeningBalance() { return openingBalance; }

}
