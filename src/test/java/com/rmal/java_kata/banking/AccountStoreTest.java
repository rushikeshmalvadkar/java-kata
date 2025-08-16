package com.rmal.java_kata.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.rmal.java_kata.banking.AccountStore.findHighestTransaction;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountStoreTest {

    @BeforeEach
    void setUp() {
        AccountStore.clear();
    }

    @Test
    void should_show_total_balance_zero() {

        BigDecimal totalBalance = AccountStore.totalBalance("rushikesh");
        assertThat(totalBalance).isZero();
    }

    @Test
    void should_add_money() {


        AccountStore.credit("rushikesh", new BigDecimal("200"), "Through Birthday");
        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");
        BigDecimal totalBalance = AccountStore.totalBalance("rushikesh");
        Set<Transaction> transactions = AccountStore.transactions("rushikesh");
        assertThat(totalBalance).isEqualTo(new BigDecimal("400"));
        assertThat(transactions).hasSize(2);


    }

    @Test
    void should_spend_money() {


        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");
        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");

        AccountStore.spend("rushikesh", new BigDecimal("100"), "for khaman");
        BigDecimal totalBalance = AccountStore.totalBalance("rushikesh");
        assertThat(totalBalance).isEqualTo(new BigDecimal("300"));

    }

    @Test
    void should_throw_exception_when_try_to_spend_money_if_balance_not_available() {


        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");
        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");


        assertThatThrownBy(() -> AccountStore.spend("rushikesh", new BigDecimal("500"), "for khaman"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No sufficient balance for spend");
    }

    @Test
    void should_check_highest_transaction() {
        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");
        AccountStore.credit("rushikesh", new BigDecimal("200"), "Some description");
        AccountStore.spend("rushikesh",new BigDecimal("100"),"For shopping");
        AccountStore.spend("rushikesh",new BigDecimal("100"),"For Toy");
        AccountStore.spend("rushikesh",new BigDecimal("50"),"For Pen");
        List<Transaction> higestTransaction = findHighestTransaction("rushikesh");
        assertThat(higestTransaction).hasSize(2);

    }

    @Test
    void should_add_transactions_as_csv_file(){
        Resource csvResource = new ClassPathResource("data/valid-transactions.csv");
        ImportTransactionsResult result  = AccountStore.importTransactions("rushikesh",csvResource);
        assertThat(result.isCompleted()).isTrue();
        assertThat(result.message()).isEqualTo("5 transactions imported successfully");
        assertThat(result.errors()).isNull();
        assertThat(AccountStore.transactions("rushikesh")).hasSize(5);
    }

    @Test
    void should_return_errors_when_given_invalid_in_csv_file() {
        Resource csvResource = new ClassPathResource("data/invalid-transactions.csv");
        ImportTransactionsResult result  = AccountStore.importTransactions("rushikesh",csvResource);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.message()).isEqualTo("Validation violation");
        assertThat(result.errors()).containsExactly(
           "Row 2 - Missing Type.Missing Amount",
           "Row 3 - Invalid Date.Invalid Type",
           "Row 4 - Missing Date.Invalid Amount"
        );
    }
}