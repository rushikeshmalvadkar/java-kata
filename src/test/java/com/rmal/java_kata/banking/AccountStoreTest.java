package com.rmal.java_kata.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

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


}