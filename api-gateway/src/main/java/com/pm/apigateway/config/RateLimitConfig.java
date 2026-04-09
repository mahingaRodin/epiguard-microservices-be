package com.pm.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userEmail = exchange.getRequest().getHeaders().getFirst("X-User-Email");
            if (userEmail != null) {
                return Mono.just(userEmail);
            }
            //fallback when not logged in
            return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        };
    }
}
