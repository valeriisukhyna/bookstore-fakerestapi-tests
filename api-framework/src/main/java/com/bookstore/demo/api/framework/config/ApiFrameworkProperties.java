package com.bookstore.demo.api.framework.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:com/bookstore/demo/api/framework/endpoint.properties"})
public interface ApiFrameworkProperties extends Config {
    @Key("base.url")
    String baseUrl();
    @Key("base.path")
    String basePath();
}
