package com.rmal.java_kata.banking;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.groupingBy;

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
                .filter(Transaction::isSpend)
                .map(Transaction::getAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    private BigDecimal totalDeposit() {
        return transactions.stream()
                .filter(Transaction::isCredit)
                .map(Transaction::getAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> highestTransaction(){
        Map<BigDecimal, List<Transaction>> amountToTransactionsMap = transactions.stream()
                .filter(Transaction::isSpend)
                .collect(groupingBy(Transaction::getAmount));
        // Entry -> key , value -> spend amount , list of transaction
        Comparator<Map.Entry<BigDecimal, List<Transaction>>> AMOUNT_NATURAL_ORDER_COMPARATOR =
                Map.Entry.comparingByKey();
        Map.Entry<BigDecimal, List<Transaction>> mostSpendTransactionMap = amountToTransactionsMap
                .entrySet()
                .stream()
                .max(AMOUNT_NATURAL_ORDER_COMPARATOR)
                .orElse(null);

        if (Objects.nonNull(mostSpendTransactionMap)) {
            return mostSpendTransactionMap.getValue();
        }
        return List.of();
    }

    public void addTranscationInBatch(List<ImportTranscationTemp> importTranscationTempList) {
        for (ImportTranscationTemp importTranscationTemp : importTranscationTempList) {
           Transaction transaction = Transaction.from(importTranscationTemp);
           addTransaction(transaction);
        }
    }
}
