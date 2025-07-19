package com.bookstore.demo.api.framework.service;

import com.bookstore.demo.api.framework.ApiConfig;
import com.bookstore.demo.api.framework.dto.AuthorDto;
import com.bookstore.demo.utils.constants.HttpStatuses;
import io.restassured.common.mapper.TypeRef;

import java.util.List;

public class AuthorsService extends BaseService {

    private final String AUTHORS_ID_URL = "/Authors/{id}";
    private final String AUTHORS_URL = "/Authors";
    private final String AUTHORS_BY_BOOK_ID_URL = "/Authors/authors/books/{bookId}";

    public AuthorsService(ApiConfig apiConfig) {
        super(apiConfig);
    }

    public AuthorDto getAuthor(int id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .get(AUTHORS_ID_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(AuthorDto.class);
    }

    public void getAuthorExceptional(long id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .get(AUTHORS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public void getAuthorExceptional(String id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .get(AUTHORS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public List<AuthorDto> getAuthorList() {
        response = createRequest()
                .when()
                    .get(AUTHORS_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(new TypeRef<List<AuthorDto>>() {});
    }

    public List<AuthorDto> getAuthorsByBookId(int bookId) {
        response = createRequest()
                .pathParam("bookId", bookId)
                .when()
                    .get(AUTHORS_BY_BOOK_ID_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(new TypeRef<List<AuthorDto>>() {});
    }

    public AuthorDto createAuthor(AuthorDto authorDto) {
        response = createRequest()
                .body(authorDto)
                .when()
                    .post(AUTHORS_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(AuthorDto.class);
    }

    public AuthorDto updateAuthor(AuthorDto authorDto, int id) {
        response = createRequest()
                .pathParam("id", id)
                .body(authorDto)
                .when()
                    .put(AUTHORS_ID_URL)
                .then()
                    .statusCode(HttpStatuses.OK_STATUS)
                .extract()
                    .response();
        return response.as(AuthorDto.class);
    }

    public void updateAuthorExceptional(AuthorDto authorDto, int id) {
        response = createRequest()
                .pathParam("id", id)
                .body(authorDto)
                .when()
                    .put(AUTHORS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public void deleteAuthor(long id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .delete(AUTHORS_ID_URL)
                .then()
                .extract()
                    .response();
    }

    public void deleteAuthor(String id) {
        response = createRequest()
                .pathParam("id", id)
                .when()
                    .delete(AUTHORS_ID_URL)
                .then()
                .extract()
                    .response();
    }
}
