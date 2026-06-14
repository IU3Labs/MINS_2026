package ru.bmstu.travel.common;

import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import java.util.UUID;

public final class Tracing {
    public static final String TRACE_HEADER_NAME = "x-trace-id";
    public static final Metadata.Key<String> TRACE_HEADER =
            Metadata.Key.of(TRACE_HEADER_NAME, Metadata.ASCII_STRING_MARSHALLER);
    private static final Context.Key<String> TRACE_CONTEXT_KEY = Context.key("trace-id");

    private Tracing() {
    }

    public static String newTraceId() {
        return UUID.randomUUID().toString();
    }

    public static Metadata metadataWithTraceId(String traceId) {
        Metadata metadata = new Metadata();
        metadata.put(TRACE_HEADER, traceId);
        return metadata;
    }

    public static String currentTraceId() {
        String value = TRACE_CONTEXT_KEY.get();
        return value == null || value.isBlank() ? newTraceId() : value;
    }

    public static ServerInterceptor serverInterceptor() {
        return new ServerInterceptor() {
            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                    ServerCall<ReqT, RespT> call,
                    Metadata headers,
                    ServerCallHandler<ReqT, RespT> next
            ) {
                String traceId = headers.get(TRACE_HEADER);
                if (traceId == null || traceId.isBlank()) {
                    traceId = newTraceId();
                }
                Context context = Context.current().withValue(TRACE_CONTEXT_KEY, traceId);
                return ContextsCompat.interceptCall(context, call, headers, next);
            }
        };
    }
}
