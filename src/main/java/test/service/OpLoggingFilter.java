package test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Integer.MIN_VALUE + 1000)
public class OpLoggingFilter implements WebFilter {
    private static Logger log = LoggerFactory.getLogger(OpLoggingFilter.class);

    @Override
    public Mono<Void> filter(
        ServerWebExchange exchange,
        WebFilterChain chain
    ) {
        log.info("processing start");
        var filter = chain.filter(exchange);
        exchange.getResponse().beforeCommit(() -> {
            log.info("processing end");
            return Mono.empty();
        });
        return filter;
    }
}
