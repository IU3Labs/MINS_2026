package org.example.infrastructure.grpc;

import io.grpc.Metadata;

public final class TraceHeaders {
    public static final String TRACE_ID_HEADER = "trace-id";
    public static final Metadata.Key<String> TRACE_ID_METADATA_KEY =
            Metadata.Key.of(TRACE_ID_HEADER, Metadata.ASCII_STRING_MARSHALLER);

    private TraceHeaders() {
    }
}
