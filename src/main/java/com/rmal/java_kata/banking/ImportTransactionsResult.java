package com.rmal.java_kata.banking;

import java.util.List;

import static com.rmal.java_kata.banking.ImportTransactionStatus.COM;
import static com.rmal.java_kata.banking.ImportTransactionStatus.ERR;

public record ImportTransactionsResult(String message, List<String> errors, ImportTransactionStatus status) {
    public static ImportTransactionsResult failed(List<String> errors) {
        return new ImportTransactionsResult("Validation violation",errors, ERR);
    }

    public boolean isCompleted(){
        return status == COM;
    }

    public boolean hasErrors(){
        return status == ERR;
    }

    public static ImportTransactionsResult completed(String message){
      return new ImportTransactionsResult(message,null, COM);
    }
}
