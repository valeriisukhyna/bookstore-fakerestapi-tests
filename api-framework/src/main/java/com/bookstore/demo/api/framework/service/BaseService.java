package com.bookstore.demo.api.framework.service;

import com.bookstore.demo.api.framework.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import com.bookstore.demo.api.framework.LoggingRequestFilter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseService {

    private final String baseUrl;
    private final String basePath;
    protected Response response;

    protected BaseService(ApiConfig apiConfig) {
        this.baseUrl = apiConfig.getBaseUrl();
        this.basePath = apiConfig.getBasePath();
    }

    protected RequestSpecification createRequest() {
        return RestAssured.given()
                .filters(new LoggingRequestFilter(), new AllureRestAssured())
                .baseUri(baseUrl)
                .basePath(basePath)
                .contentType(ContentType.JSON);
    }

    public Integer getResponseStatusCode() {
        return response.statusCode();
    }
}
