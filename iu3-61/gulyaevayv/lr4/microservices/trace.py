import uuid


TRACE_METADATA_KEY = "trace-id"


def new_trace_id():
    return uuid.uuid4().hex


def metadata_with_trace(trace_id=None):
    return ((TRACE_METADATA_KEY, trace_id or new_trace_id()),)


def trace_id_from_context(context):
    metadata = dict(context.invocation_metadata())
    return metadata.get(TRACE_METADATA_KEY, new_trace_id())
