package com.cinema.core.server;

import com.cinema.core.util.TraceIdContext;
import io.grpc.*;
import org.slf4j.MDC;

public class TraceIdServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> TRACE_ID_HEADER =
            Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Извлекаем trace ID из заголовков (если есть)
        String traceId = headers.get(TRACE_ID_HEADER);

        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdContext.generateShortUuid();
        }

        // Устанавливаем traceId в контекст
        TraceIdContext.setCurrentTraceId(traceId);

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            @Override
            public void onComplete() {
                TraceIdContext.clear();
                super.onComplete();
            }

            @Override
            public void onCancel() {
                TraceIdContext.clear();
                super.onCancel();
            }
        };
    }
}