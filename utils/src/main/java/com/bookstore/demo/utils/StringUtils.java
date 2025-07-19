package com.bookstore.demo.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {

    public static String generateStringOfLength(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }
}
