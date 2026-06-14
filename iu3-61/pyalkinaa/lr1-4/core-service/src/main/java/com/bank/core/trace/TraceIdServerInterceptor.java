package com.bank.core.trace;

import io.grpc.*;

import java.util.UUID;

public class TraceIdServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String traceId = headers.get(TraceKeys.TRACE_ID_METADATA_KEY);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        Context context = Context.current().withValue(TraceKeys.TRACE_ID_CONTEXT_KEY, traceId);
        return Contexts.interceptCall(context, call, headers, next);
    }
}
