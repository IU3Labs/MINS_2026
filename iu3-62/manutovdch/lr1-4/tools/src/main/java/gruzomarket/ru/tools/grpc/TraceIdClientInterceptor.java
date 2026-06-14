package gruzomarket.ru.tools.grpc;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@GrpcGlobalClientInterceptor
@Component
public class TraceIdClientInterceptor implements ClientInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TraceIdClientInterceptor.class);

    private static final Metadata.Key<String> TRACE_ID_KEY =
            Metadata.Key.of("trace_id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = MDC.get("trace_id");
                log.debug("TraceIdClientInterceptor: MDC trace_id={}", traceId);
                if (traceId != null && !traceId.isEmpty()) {
                    headers.put(TRACE_ID_KEY, traceId);
                    log.debug("TraceIdClientInterceptor: injected trace_id into gRPC metadata");
                }
                super.start(responseListener, headers);
            }
        };
    }
}
