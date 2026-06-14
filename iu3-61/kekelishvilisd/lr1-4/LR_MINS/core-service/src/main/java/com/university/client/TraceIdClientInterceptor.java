package com.university.client;

import io.grpc.*;
import java.util.UUID;

/**
 * Клиентский перехватчик для передачи Trace ID в Reference Service
 */
public class TraceIdClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = headers.get(Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER));
                if (traceId == null || traceId.isEmpty()) {
                    traceId = "trace-" + UUID.randomUUID().toString().substring(0, 8);
                    Metadata.Key<String> key = Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);
                    headers.put(key, traceId);
                }

                System.out.println("[Trace:" + traceId + "] Отправка запроса в Reference Service");
                super.start(responseListener, headers);
            }
        };
    }
}
