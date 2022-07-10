package test.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.HashSet;
import java.util.Set;

public class TraceIdAssertion extends Filter<ILoggingEvent> {
    private Set<String> traceIds = new HashSet<>();

    @Override
    public FilterReply decide(ILoggingEvent event) {
        var traceId = event.getMDCPropertyMap().get("traceId");

        if ("processing start".equals(event.getMessage())) {
            traceIds.add(traceId);
        }

        if (traceId != null && !traceIds.contains(traceId)) {
            System.out.println("******** Unknown traceId: " + traceId);
        }

        if ("processing end".equals(event.getMessage())) {
            traceIds.remove(traceId);
        }

        return FilterReply.NEUTRAL;
    }
}
