package com.bookstore.demo.api.framework.config;

import org.aeonbits.owner.ConfigFactory;

public class ApiFrameworkPropertiesProvider {

    public static final ApiFrameworkProperties API_FRAMEWORK_PROPERTIES =
            ConfigFactory.create(ApiFrameworkProperties.class);

    private ApiFrameworkPropertiesProvider() {
    }
}
