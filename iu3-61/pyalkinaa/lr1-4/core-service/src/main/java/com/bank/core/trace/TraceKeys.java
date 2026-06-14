package com.bank.core.trace;

import io.grpc.Context;
import io.grpc.Metadata;

public final class TraceKeys {
    public static final String TRACE_HEADER_NAME = "x-trace-id";
    public static final Metadata.Key<String> TRACE_ID_METADATA_KEY =
            Metadata.Key.of(TRACE_HEADER_NAME, Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> TRACE_ID_CONTEXT_KEY = Context.key("trace-id");

    private TraceKeys() {
    }
}
