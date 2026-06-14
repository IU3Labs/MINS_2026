package com.cinema.reference.server;

import io.grpc.*;
import org.slf4j.MDC;
import java.util.UUID;

public class TraceIdServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> TRACE_ID_HEADER =
            Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Извлекаем trace ID из заголовков
        String traceId = headers.get(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().substring(0, 8);
        }

        // Помещаем в MDC для логирования
        MDC.put("traceId", traceId);

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            @Override
            public void onComplete() {
                MDC.clear();
                super.onComplete();
            }

            @Override
            public void onCancel() {
                MDC.clear();
                super.onCancel();
            }
        };
    }
}