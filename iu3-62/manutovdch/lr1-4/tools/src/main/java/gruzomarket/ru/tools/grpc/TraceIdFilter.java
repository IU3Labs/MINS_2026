package gruzomarket.ru.tools.grpc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class TraceIdFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TraceIdFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String traceId = null;
        if (request instanceof HttpServletRequest httpReq) {
            traceId = httpReq.getHeader(TRACE_ID_HEADER);
        }

        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }

        MDC.put("trace_id", traceId);
        if (request instanceof HttpServletRequest httpReq) {
            log.debug("TraceIdFilter: set trace_id={} for {} {}", traceId, httpReq.getMethod(), httpReq.getRequestURI());
        }
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("trace_id");
        }
    }
}
