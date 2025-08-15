package com.rmal.java_kata.banking;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.rmal.java_kata.banking.TransactionType.CREDIT;
import static com.rmal.java_kata.banking.TransactionType.SPEND;

@Getter
@Setter
public class Transaction {

    private TransactionType transcationType;
    private LocalDate translationDate;
    private BigDecimal amount;
    private String description;

    private Transaction (TransactionType transcationType, BigDecimal amount, String description){
        this.transcationType = transcationType;
        this.translationDate= LocalDate.now();
        this.amount = amount;
        this.description = description;
    }

    private static Transaction of(TransactionType transcationType, BigDecimal amount, String description){
        return new Transaction(transcationType, amount,description);
    }

    public static Transaction ofCredit(BigDecimal amount, String description){
        return of(CREDIT, amount,description);
    }

    public static Transaction ofSpend(BigDecimal amount, String description){
        return of(SPEND, amount,description);
    }


    public boolean isSpend() {
        return transcationType == SPEND;
    }

    public boolean isCredit() {
        return transcationType == CREDIT;
    }
}
