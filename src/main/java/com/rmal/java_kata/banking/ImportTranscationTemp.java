package com.rmal.java_kata.banking;

import org.springframework.util.StringUtils;

import static org.springframework.util.StringUtils.isEmpty;

public record ImportTranscationTemp(String date, String type, String amount, String description) {
    public ImportTranscationTemp {
        date = isEmpty(date) ? null : date;
        type = isEmpty(type) ? null : type;
        amount = isEmpty(amount) ? null : amount;
        description = isEmpty(description) ? null : description;
    }
}
