package com.bookstore.demo.api.framework.service;

import com.bookstore.demo.api.framework.ApiConfig;
import com.bookstore.demo.api.framework.dto.BookDto;
import com.bookstore.demo.utils.constants.HttpStatuses;
import io.restassured.common.mapper.TypeRef;

import java.util.List;

public class BooksService extends BaseService {

    private final String BOOKS_ID_URL = "/Books/{id}";
    private final String BOOKS_URL = "/Books";

    public BooksService(ApiConfig apiConfig) {
        super(apiConfig);
    }

    public BookDto getBook(int id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .get(BOOKS_ID_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(BookDto.class);
    }

    public void getBookExceptional(long id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .get(BOOKS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public void getBookExceptional(String id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .get(BOOKS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public List<BookDto> getBookList() {
        response = createRequest()
                .when()
                    .get(BOOKS_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(new TypeRef<List<BookDto>>() {});
    }

    public BookDto createBook(BookDto bookDto) {
        response = createRequest()
                .body(bookDto)
                .when()
                    .post(BOOKS_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(BookDto.class);
    }

    public BookDto updateBook(BookDto bookDto, int id) {
        response = createRequest()
                .pathParam("id", id)
                .body(bookDto)
                .when()
                    .put(BOOKS_ID_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(BookDto.class);
    }

    public void updateBookExceptional(BookDto bookDto, Integer id) {
        response = createRequest()
                .pathParam("id", id)
                .body(bookDto)
                .when()
                    .put(BOOKS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public void deleteBook(long id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .delete(BOOKS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public void deleteBook(String id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .delete(BOOKS_ID_URL)
                .then()
                .extract()
                    .response();
    }
}
