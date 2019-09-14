package com.okjike.data.configurations;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    private static final List<String> ALLOWED_HEADERS = ImmutableList.of(
        "x-requested-with", "authorization", "Content-Type", "Authorization", "credential",
        "X-XSRF-TOKEN");
    private static final List<HttpMethod> ALLOWED_METHODS = ImmutableList.of(
        HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS);
    private static final long MAX_AGE = 3600L;

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.setAccessControlAllowOrigin(request.getHeaders().getOrigin());
                headers.setAccessControlAllowCredentials(true);
                headers.setAccessControlAllowMethods(ALLOWED_METHODS);
                headers.setAccessControlMaxAge(MAX_AGE);
                headers.setAccessControlAllowHeaders(ALLOWED_HEADERS);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger resource
        registry.addResourceHandler("/swagger-ui.html**")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}