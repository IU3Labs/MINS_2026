package ru.mins.parking.core.grpc;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import ru.mins.parking.core.util.TraceContext;

public class TraceClientInterceptor implements io.grpc.ClientInterceptor {
    private static final Metadata.Key<String> TRACE_ID_KEY =
            Metadata.Key.of("trace-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        ClientCall<ReqT, RespT> delegate = next.newCall(method, callOptions);
        return new ForwardingClientCall.SimpleForwardingClientCall<>(delegate) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(TRACE_ID_KEY, TraceContext.ensure());
                super.start(responseListener, headers);
            }
        };
    }
}
