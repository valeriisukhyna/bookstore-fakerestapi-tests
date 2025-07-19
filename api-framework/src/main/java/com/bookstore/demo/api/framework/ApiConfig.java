package com.bookstore.demo.api.framework;

import com.bookstore.demo.api.framework.config.ApiFrameworkPropertiesProvider;
import lombok.Data;

/** This class can also include authorization logic or request configuration for different environments. **/
@Data
public class ApiConfig {

    private final String baseUrl;
    private final String basePath;

    public ApiConfig() {
        this.baseUrl = ApiFrameworkPropertiesProvider.API_FRAMEWORK_PROPERTIES.baseUrl();
        this.basePath = ApiFrameworkPropertiesProvider.API_FRAMEWORK_PROPERTIES.basePath();
    }
}
