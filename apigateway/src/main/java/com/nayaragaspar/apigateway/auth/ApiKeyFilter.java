package com.nayaragaspar.apigateway.auth;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class ApiKeyFilter implements GatewayFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    // TODO: get api-key in the secrets manager
    @Value("${http.auth-token}")
    private String secretApiKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("filter");
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeId = route != null ? route.getId() : null;
        List<String> apiKeyHeader = exchange.getRequest().getHeaders().get("x-api-key");

        if (apiKeyHeader == null || routeId == null || CollectionUtils.isEmpty(apiKeyHeader)
                || checkApikey(routeId, apiKeyHeader.get(0))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You are not authorized to access this resource!");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private boolean checkApikey(String routeId, String apikey) {
        // TODO: validate api-key acording to the routeId
        // [{"routeId": "custom", "key": "secret1"},{"routeId": "custom", "key": "secret2"}]
        if (apikey.equals(secretApiKey)) {
            return true;
        }

        return false;
    }

}
