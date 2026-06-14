package ru.iu3.lab4.coreservice.client;

import io.grpc.*;

import java.util.UUID;

/**
 * Клиентский перехватчик для передачи Trace ID в Reference Service
 */
public class TraceIdClientInterceptor implements ClientInterceptor {
    private static final Metadata.Key<String> TRACE_ID_KEY =
            Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = headers.get(TRACE_ID_KEY);
                if (traceId == null || traceId.isEmpty()) {
                    traceId = "trace-" + UUID.randomUUID().toString().substring(0, 8);
                    headers.put(TRACE_ID_KEY, traceId);
                }
                System.out.println("[Trace:" + traceId + "] Отправка запроса в Reference Service");
                super.start(responseListener, headers);
            }
        };
    }
}