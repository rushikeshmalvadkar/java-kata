package com.rmal.java_kata.banking;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.math.BigDecimal.ZERO;

@Getter
public class Account {

    private final String username;
    private final Set<Transaction> transactions;

    public Account(String name){
        this.username = name;
        this.transactions = new LinkedHashSet<>();
    }
    public static Account of(String name){
        return new Account(name);
    }

    public  BigDecimal totalBalance() {
        return totalDeposit().subtract(totalWithDraw());
    }

    private BigDecimal totalWithDraw() {
        return transactions.stream()
                .filter(Transaction::isWithdraw)
                .map(Transaction::getAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    private BigDecimal totalDeposit() {
        return transactions.stream()
                .filter(Transaction::isDeposit)
                .map(Transaction::getAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

}
