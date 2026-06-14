package com.bank.core.trace;

import io.grpc.*;

public class TraceIdClientInterceptor implements ClientInterceptor {
    private final String traceId;

    public TraceIdClientInterceptor(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(TraceKeys.TRACE_ID_METADATA_KEY, traceId);
                super.start(responseListener, headers);
            }
        };
    }
}
