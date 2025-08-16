package com.rmal.java_kata.banking;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public enum TransactionType {
    CREDIT("Credit"),
    SPEND("Spend");

    private static final Map<String, TransactionType> TYPE_TO_TRANSACTION_TYPE_ENUM_MAP = Stream.of(TransactionType.values())
            .collect(toMap(TransactionType::type, Function.identity()));
    private final String type;

    public String type() {
        return type;
    }

    public static TransactionType from(String type){
        TransactionType transactionType = fromMap(type);
        throwIfTypeIsInvalid(transactionType);
        return transactionType;
    }

    private static void throwIfTypeIsInvalid(TransactionType transactionType) {
        if (isNull(transactionType)) {
            throw new RuntimeException("invalid transaction type");
        }
    }

    private static TransactionType fromMap(String type) {
        return TYPE_TO_TRANSACTION_TYPE_ENUM_MAP.get(type);
    }
}
