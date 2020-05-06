package com.rbs.casestudy.transferapi.model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rbs.casestudy.transferapi.rest.serializers.TerseAccountSerializer;
import com.sun.istack.NotNull;

/**
 * 
 * @author ed
 *
 */
@Entity
@NamedEntityGraph(includeAllAttributes = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Account.class, optional = false)
    @JsonSerialize(using = TerseAccountSerializer.class)
    private Account sourceAccount, destinationAccount;

    @Column(scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant transactionTime;

    public Transaction() {}
    Transaction(Long id, Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        this.id = id;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public void setId(long id) { this.id = id; }
    public Long getId() { return this.id; }

    public void setSourceAccount(Account sourceAccount) { this.sourceAccount = sourceAccount; }
    public Account getSourceAccount() { return sourceAccount; }

    public void setDestinationAccount(Account destinationAccount) { this.destinationAccount = destinationAccount; }
    public Account getDestinationAccount() { return destinationAccount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getAmount() { return amount; }

    public void setTransactionTime(Instant transactionTime) { this.transactionTime = transactionTime; }
    public Instant getTransactionTime() { return transactionTime; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof Transaction)) { return false; }
        Transaction that = Transaction.class.cast(obj);
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override 
    public String toString() {
        return "Transaction{" +
                "id=" + id + 
                ", source=" + sourceAccount +
                ", destination=" + destinationAccount +
                ", amount=" + amount +
                ", t_time=" + transactionTime +
                "}";
    }
}
