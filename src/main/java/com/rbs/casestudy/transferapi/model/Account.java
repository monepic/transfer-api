package com.rbs.casestudy.transferapi.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {

    @Id
    private String accountNumber;

    @Column(scale = 2, nullable = false)
    private BigDecimal balance;

    public Account() {}
    Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountNumber() { return accountNumber; }

    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getBalance() { return balance; }

    @JsonIgnore
    public BigDecimal getAvailableFunds() {
        return getBalance();
    }

    public void debit(BigDecimal amount) {
        setBalance(getBalance().subtract(amount));
    }

    public void credit(BigDecimal amount) {
        setBalance(getBalance().add(amount));
    }

    @Override
    public boolean equals(Object obj) { 
        if (this == obj) { return true; }
        if (!(obj instanceof Account)) { return false; }
        Account that = Account.class.cast(obj);
        return accountNumber == that.accountNumber;
    }

    @Override
    public int hashCode() {
        return accountNumber == null ? 0 : accountNumber.hashCode();
    }

    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber + 
                ", balance=" + balance +
                "}";
    }
}
