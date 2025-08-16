package com.rmal.java_kata.banking;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.isEmpty;

@RequiredArgsConstructor
public enum TransactionValidator {
    date("Date", true, "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$"),
    type("Type", true, "^(Credit|Spend)$"),
    amount("Amount", true, "^\\d+(\\.\\d{1,2})?$"),
    description("Description", false, null);

    public static final String DOT = ".";
    private final String headerName;
    private final boolean mandatory;
    private final String regex;


    public static List<String> validate(List<ImportTranscationTemp> transactionTemps) {
        List<String> errors = new ArrayList<>();
        for (int row = 0; row < transactionTemps.size(); row++) {
            ImportTranscationTemp transactionTemp = transactionTemps.get(row);
            String error = validate(transactionTemp);
            if (rowHasValidationErrors(error)) {
                errors.add(String.format("Row %s - %s", row + 1,error));
            }
        }
        return errors;
    }

    private static boolean rowHasValidationErrors(String error) {
        return !isEmpty(error);
    }

    private static String validate(ImportTranscationTemp transactionTemp) {
        List<String> rowLevelErrors = new ArrayList<>();
        List<TransactionValidator> propValidators = Stream.of(TransactionValidator.values())
                .toList();
        for (TransactionValidator propValidator : propValidators) {
            String propName = propValidator.name();
            String value = (String) findValue(transactionTemp, propName);
            if (mandatoryViolation(propValidator, value)) {
                rowLevelErrors.add("Missing %s".formatted(propValidator.headerName));        
            }
            if (regexVolitional(propValidator, value)) {
                rowLevelErrors.add("Invalid %s".formatted(propValidator.headerName));
            }
        }
        return String.join(DOT, rowLevelErrors);
    }

    private static boolean regexVolitional(TransactionValidator propValidator, String value) {
        return hasRegex(propValidator) &&
                nonNull(value) &&
                invalidByRegex(propValidator, value);
    }

    private static boolean mandatoryViolation(TransactionValidator propValidator, String value) {
        return propValidator.mandatory &&
                isNull(value);
    }

    private static boolean invalidByRegex(TransactionValidator propValidator, String value) {
        boolean isValid = Pattern.matches(propValidator.regex, value);
        return !isValid;
    }

    private static boolean hasRegex(TransactionValidator propValidator) {
        return !isEmpty(propValidator.regex);
    }

    private static Object findValue(Object object, String propName) {
        Class<?> aClass = object.getClass();
        try {
            Field declaredField = aClass.getDeclaredField(propName);
            declaredField.setAccessible(true);
            return declaredField.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
