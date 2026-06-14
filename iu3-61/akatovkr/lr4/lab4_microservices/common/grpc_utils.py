from __future__ import annotations

import grpc

from lab4_microservices.common.trace import TRACE_ID_HEADER, get_trace_id, new_trace_id, reset_trace_id, set_trace_id


def metadata_with_trace(trace_id: str | None = None) -> list[tuple[str, str]]:
    return [(TRACE_ID_HEADER, trace_id or get_trace_id() or new_trace_id())]


class TraceIdServerInterceptor(grpc.ServerInterceptor):
    def intercept_service(self, continuation, handler_call_details):
        handler = continuation(handler_call_details)
        if handler is None or handler.unary_unary is None:
            return handler

        metadata = dict(handler_call_details.invocation_metadata or ())
        trace_id = metadata.get(TRACE_ID_HEADER, new_trace_id())

        def traced_behavior(request, context):
            token = set_trace_id(trace_id)
            try:
                return handler.unary_unary(request, context)
            finally:
                reset_trace_id(token)

        return grpc.unary_unary_rpc_method_handler(
            traced_behavior,
            request_deserializer=handler.request_deserializer,
            response_serializer=handler.response_serializer,
        )
