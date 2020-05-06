package com.rbs.casestudy.transferapi.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbs.casestudy.transferapi.rest.serializers.MoneyDeserializer;
import com.rbs.casestudy.transferapi.validators.SourceAndDestAreDifferent;
import com.sun.istack.NotNull;

@SourceAndDestAreDifferent
public class TransferRequest {

    @NotEmpty
    private String sourceAccountNumber, destinationAccountNumber;

    @NotNull
    @Positive
    @JsonDeserialize(using=MoneyDeserializer.class)
    private BigDecimal amount;

    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }

    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getAmount() { return amount; }

}
