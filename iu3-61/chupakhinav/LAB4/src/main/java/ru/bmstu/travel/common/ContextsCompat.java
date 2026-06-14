package ru.bmstu.travel.common;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;

final class ContextsCompat {
    private ContextsCompat() {
    }

    static <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            Context context,
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        return Contexts.interceptCall(context, call, headers, next);
    }
}
