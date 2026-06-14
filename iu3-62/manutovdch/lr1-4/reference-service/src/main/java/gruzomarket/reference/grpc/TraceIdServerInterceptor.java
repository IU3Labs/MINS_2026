package gruzomarket.reference.grpc;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@GrpcGlobalServerInterceptor
public class TraceIdServerInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TraceIdServerInterceptor.class);
    private static final String TRACE_ID_KEY = "trace_id";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        Metadata.Key<String> key = Metadata.Key.of(TRACE_ID_KEY, Metadata.ASCII_STRING_MARSHALLER);
        final String traceId = headers.containsKey(key) ? headers.get(key) : "unknown";
        MDC.put(TRACE_ID_KEY, traceId);
        log.debug("TraceIdServerInterceptor: set trace_id='{}' from gRPC metadata", traceId);
        MDC.remove(TRACE_ID_KEY);

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            @Override
            public void onHalfClose() {
                MDC.put(TRACE_ID_KEY, traceId);
                try { super.onHalfClose(); } finally { MDC.remove(TRACE_ID_KEY); }
            }
            @Override
            public void onMessage(ReqT message) {
                MDC.put(TRACE_ID_KEY, traceId);
                try { super.onMessage(message); } finally { MDC.remove(TRACE_ID_KEY); }
            }
            @Override
            public void onCancel() {
                MDC.put(TRACE_ID_KEY, traceId);
                try { super.onCancel(); } finally { MDC.remove(TRACE_ID_KEY); }
            }
            @Override
            public void onComplete() {
                MDC.put(TRACE_ID_KEY, traceId);
                try { super.onComplete(); } finally { MDC.remove(TRACE_ID_KEY); }
            }
            @Override
            public void onReady() {
                MDC.put(TRACE_ID_KEY, traceId);
                try { super.onReady(); } finally { MDC.remove(TRACE_ID_KEY); }
            }
        };
    }
}
