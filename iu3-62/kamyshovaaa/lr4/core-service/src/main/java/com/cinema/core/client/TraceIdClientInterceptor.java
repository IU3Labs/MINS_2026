package com.cinema.core.client;

import io.grpc.*;

public class TraceIdClientInterceptor implements ClientInterceptor {

    private final String traceId;
    private static final Metadata.Key<String> TRACE_ID_HEADER =
            Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);

    public TraceIdClientInterceptor(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(TRACE_ID_HEADER, traceId);
                super.start(responseListener, headers);
            }
        };
    }
}