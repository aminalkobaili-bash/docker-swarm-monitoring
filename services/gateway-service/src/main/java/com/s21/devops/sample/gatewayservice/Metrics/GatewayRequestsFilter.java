package com.s21.devops.sample.gatewayservice.Metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayRequestsFilter extends OncePerRequestFilter {
    private static final String GATEWAY_API_PREFIX = "/api/v1/gateway";

    private final Counter gatewayRequestsCounter;

    public GatewayRequestsFilter(MeterRegistry meterRegistry) {
        this.gatewayRequestsCounter = Counter.builder("gateway.requests")
                .description("Number of requests received by gateway")
                .register(meterRegistry);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(GATEWAY_API_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        gatewayRequestsCounter.increment();
        filterChain.doFilter(request, response);
    }
}
