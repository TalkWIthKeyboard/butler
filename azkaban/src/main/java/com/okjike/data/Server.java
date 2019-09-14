package com.okjike.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
