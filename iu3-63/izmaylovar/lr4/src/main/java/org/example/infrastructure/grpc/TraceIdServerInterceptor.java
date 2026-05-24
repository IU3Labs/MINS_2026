package org.example.infrastructure.grpc;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.example.infrastructure.trace.TraceContext;

import java.util.UUID;

public class TraceIdServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        String incomingTraceId = headers.get(TraceHeaders.TRACE_ID_METADATA_KEY);
        String traceId = incomingTraceId == null || incomingTraceId.isBlank()
                ? UUID.randomUUID().toString()
                : incomingTraceId;

        TraceContext.setTraceId(traceId);
        ServerCall.Listener<ReqT> delegate;
        try {
            delegate = next.startCall(call, headers);
        } finally {
            TraceContext.clear();
        }

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onMessage(ReqT message) {
                TraceContext.runWithTrace(traceId, () -> super.onMessage(message));
            }

            @Override
            public void onHalfClose() {
                TraceContext.runWithTrace(traceId, super::onHalfClose);
            }

            @Override
            public void onCancel() {
                TraceContext.runWithTrace(traceId, super::onCancel);
            }

            @Override
            public void onComplete() {
                TraceContext.runWithTrace(traceId, super::onComplete);
            }

            @Override
            public void onReady() {
                TraceContext.runWithTrace(traceId, super::onReady);
            }
        };
    }
}
