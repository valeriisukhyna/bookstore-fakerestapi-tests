package com.bookstore.demo.datagenerator;

import com.bookstore.demo.api.framework.ApiConfig;
import com.bookstore.demo.api.framework.dto.AuthorDto;
import com.bookstore.demo.api.framework.dto.BookDto;
import com.bookstore.demo.api.framework.service.AuthorsService;
import com.bookstore.demo.api.framework.service.BooksService;
import com.bookstore.demo.utils.FakeData;
import com.bookstore.demo.utils.StringUtils;

public class AuthorsDataGenerator {

    public static AuthorDto generateAllFieldsAuthorData() {
        return AuthorDto.builder()
                .id(getNotExistingAuthorId())
                .bookId(getExistingBookId())
                .firstName(FakeData.firstName())
                .lastName(FakeData.lastName())
                .build();
    }

    public static AuthorDto generateMaxLengthStringFieldsAuthorData() {
        return AuthorDto.builder()
                .id(getNotExistingAuthorId())
                .bookId(getExistingBookId())
                .firstName(StringUtils.generateStringOfLength(255))
                .lastName(StringUtils.generateStringOfLength(255))
                .build();
    }

    public static AuthorDto generateRequiredFieldsAuthorData() {
        return AuthorDto.builder()
                .id(getNotExistingAuthorId())
                .bookId(getExistingBookId())
                .build();
    }

    public static int getNotExistingAuthorId() {
        return new AuthorsService(new ApiConfig()).getAuthorList().stream()
                .mapToInt(AuthorDto::getId)
                .max()
                .orElse(0) + 1000;
    }

    public static int getExistingBookId() {
        return new BooksService(new ApiConfig()).getBookList().stream()
                .mapToInt(BookDto::getId)
                .findAny()
                .orElse(0);
    }
}
