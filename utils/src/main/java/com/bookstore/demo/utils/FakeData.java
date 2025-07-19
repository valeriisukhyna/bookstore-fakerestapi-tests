package com.bookstore.demo.utils;

import com.github.javafaker.Faker;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FakeData {

    private static final Faker faker = new Faker();

    public static String firstName() {
        return faker.name().firstName();
    }

    public static String lastName() {
        return faker.name().lastName();
    }

    public static String title() {
        return faker.book().title();
    }

    public static String description() {
        return faker.lorem().sentence();
    }

    public static String excerpt() {
        return faker.lorem().paragraph();
    }

    public static Integer pageCount() {
        return faker.random().nextInt(10, 1000);
    }

    public static String publishDate() {
        Date date = faker.date().past(10000, TimeUnit.DAYS);
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(date.toInstant());
    }
}
