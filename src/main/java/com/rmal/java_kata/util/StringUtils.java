package com.rmal.java_kata.util;

public interface StringUtils {

    static boolean containsIgnoreCase(String a, String b){
        return a.toLowerCase().contains(b.toLowerCase());
    }

}
