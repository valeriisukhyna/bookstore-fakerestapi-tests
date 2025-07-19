package com.bookstore.demo.api.framework;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingRequestFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        log.debug(requestSpec.getMethod() + " " + requestSpec.getURI()
                + " \n Request Body =>" + requestSpec.getBody()
                + " \n Response Status => " + response.getStatusLine()
                + " \n Response Body => " + response.getBody().asPrettyString());
        return response;
    }
}
