package com.rmal.java_kata.banking;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.springframework.util.CollectionUtils.isEmpty;


public class AccountStore {

    private static final Map<String, Account> USERNAME_TO_ACCOCUN_MAP = new ConcurrentHashMap<>();

    public static BigDecimal totalBalance(String username) {
        Account account = accountOf(username);
        return account.totalBalance();
    }

    public static void clear() {
        USERNAME_TO_ACCOCUN_MAP.clear();
    }

    public static void credit(String username, BigDecimal amountToBeAdd, String description) {
        Account account = accountOf(username);
        Transaction transaction = Transaction.ofCredit(amountToBeAdd, description);
        account.addTransaction(transaction);
        update(username, account);
    }

    public static Set<Transaction> transactions(String userName) {
        Account account = accountOf(userName);
        return account.getTransactions();
    }

    public static void spend(String userName, BigDecimal amount, String description) {
        checkForBalance(userName, amount);
        Account accountDetail = USERNAME_TO_ACCOCUN_MAP.get(userName);
        accountDetail.addTransaction(Transaction.ofSpend(amount, description));
        update(userName, accountDetail);
    }

    private static Account accountOf(String username) {
        return USERNAME_TO_ACCOCUN_MAP.getOrDefault(username, Account.of(username));
    }

    private static void update(String username, Account account) {
        USERNAME_TO_ACCOCUN_MAP.put(username, account);
    }

    private static void checkForBalance(String userName, BigDecimal amount) {
        if (userHasNotEnoughBalance(userName, amount)) {
            throw new RuntimeException("No sufficient balance for spend");
        }
    }

    private static boolean userHasNotEnoughBalance(String userName, BigDecimal amount) {
        return totalBalance(userName).compareTo(amount) < 0;
    }

    public static List<Transaction> findHighestTransaction(String userName) {
        Account account = AccountStore.accountOf(userName);
        return account.highestTransaction();
    }


    public static ImportTransactionsResult importTransactions(String username, Resource csvResource) {
        List<ImportTranscationTemp> importTranscationTempList = prepareImportTransactionTemps(csvResource);
        List<String> errors = TransactionValidator.validate(importTranscationTempList);
        if (hasValidationVoiltaion(errors)) {
            return ImportTransactionsResult.failed(errors);
        }
        Account account = accountOf(username);
        account.addTranscationInBatch(importTranscationTempList);
        updatedStore(username, account);
        return completedResponse(importTranscationTempList);
    }

    private static boolean hasValidationVoiltaion(List<String> errors) {
        return !isEmpty(errors);
    }

    private static void updatedStore(String username, Account account) {
        USERNAME_TO_ACCOCUN_MAP.putIfAbsent(username, account);
    }

    private static ImportTransactionsResult completedResponse(List<ImportTranscationTemp> importTranscationTempList) {
        return ImportTransactionsResult.completed("%s transactions imported successfully".formatted(importTranscationTempList.size()));
    }

    private static List<ImportTranscationTemp> prepareImportTransactionTemps(Resource csvResource) {
        try {
            return transcationTemps(csvResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static List<ImportTranscationTemp> transcationTemps(Resource csvResource) throws IOException {
        InputStream inputStream = csvResource.getInputStream();
        byte[] csvBytes = inputStream.readAllBytes();
        String csvLinesAsString = new String(csvBytes);
        String[] csvLinesAsArray = csvLinesAsString.split("\n");
        List<String> csvLines = Stream.of(csvLinesAsArray)
                .toList();
        return csvLines.stream()
                .skip(1)
                .map(AccountStore::parse)
                .toList();
    }

    private static ImportTranscationTemp parse(String csvLine) {
        String[] csvLineAsArray = csvLine.split(",");
        return new ImportTranscationTemp(
                csvLineAsArray.length > 0 ? csvLineAsArray[0] : "",
                csvLineAsArray.length > 1 ? csvLineAsArray[1] : "",
                csvLineAsArray.length > 2 ? csvLineAsArray[2] : "",
                csvLineAsArray.length > 3 ? csvLineAsArray[3] : ""
        );

    }
}
