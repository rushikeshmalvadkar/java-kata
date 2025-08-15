package com.rmal.java_kata.banking;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


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
}
