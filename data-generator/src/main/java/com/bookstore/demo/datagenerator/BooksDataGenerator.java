package com.bookstore.demo.datagenerator;

import com.bookstore.demo.api.framework.ApiConfig;
import com.bookstore.demo.api.framework.dto.BookDto;
import com.bookstore.demo.api.framework.service.BooksService;
import com.bookstore.demo.utils.FakeData;
import com.bookstore.demo.utils.StringUtils;

public class BooksDataGenerator {

    public static BookDto generateAllFieldsBookData() {
        return BookDto.builder()
                .id(getNotExistingBookId())
                .title(FakeData.title())
                .description(FakeData.description())
                .pageCount(FakeData.pageCount())
                .excerpt(FakeData.excerpt())
                .publishDate(FakeData.publishDate())
                .build();
    }

    public static BookDto generateMaxStringLengthBookData() {
        return BookDto.builder()
                .id(getNotExistingBookId())
                .title(StringUtils.generateStringOfLength(255))
                .description(StringUtils.generateStringOfLength(255))
                .pageCount(FakeData.pageCount())
                .excerpt(StringUtils.generateStringOfLength(255))
                .publishDate(FakeData.publishDate())
                .build();
    }

    public static BookDto generateRequiredFieldsBookData() {
        return BookDto.builder()
                .id(getNotExistingBookId())
                .pageCount(FakeData.pageCount())
                .publishDate(FakeData.publishDate())
                .build();
    }

    public static int getNotExistingBookId() {
        return new BooksService(new ApiConfig()).getBookList().stream()
                .map(BookDto::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1000;
    }
}
