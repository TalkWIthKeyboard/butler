package com.okjike.data.modules;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;

@Configuration
public class TemplateModule {

    private static final long RETRY_TIME_INTERVAL = 1_500L;
    private static final int MAX_ATTEMPTS = 3;
    private static final int READ_TIME_OUT = 3_000;
    private static final int CONNECT_TIME_OUT = 3_000;

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Get {

    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Post {

    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AllTypeGet {

    }

    @Bean
    public RetryTemplate constructRetryTemplate() {
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(RETRY_TIME_INTERVAL);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public RestTemplate constructRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return new RestTemplateBuilder()
            .additionalMessageConverters(m)
            .setConnectTimeout(READ_TIME_OUT)
            .setConnectTimeout(CONNECT_TIME_OUT)
            .build();
    }

    @Bean
    @Get
    public HttpHeaders constructGetHeaders() {
        return generateHeadersForGetRequest();
    }

    @Bean
    @AllTypeGet
    public HttpHeaders constructHTMLGetHeaders() {
        return generateHeadersForAllTypeGetRequest();
    }

    @Bean
    @Post
    public HttpHeaders constructPostHeaders() {
        HttpHeaders headers = generateHeadersForGetRequest();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders generateHeadersForGetRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(ImmutableList.of(MediaType.APPLICATION_JSON_UTF8));
        headers.setAcceptCharset(ImmutableList.of(StandardCharsets.UTF_8));
        return headers;
    }

    private HttpHeaders generateHeadersForAllTypeGetRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(ImmutableList.of(MediaType.ALL));
        headers.setAcceptCharset(ImmutableList.of(StandardCharsets.UTF_8));
        return headers;
    }
}
