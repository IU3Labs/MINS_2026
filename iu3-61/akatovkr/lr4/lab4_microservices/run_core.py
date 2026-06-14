from __future__ import annotations

import argparse

from lab4_microservices.core_service.server import serve


def main() -> None:
    parser = argparse.ArgumentParser(description="Run Core Service")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=50051)
    parser.add_argument("--reference-target", default="127.0.0.1:50052")
    parser.add_argument("--rule-set", default="sanpin", choices=["sanpin", "gost", "internal"])
    args = parser.parse_args()

    server = serve(
        host=args.host,
        port=args.port,
        reference_target=args.reference_target,
        rule_set=args.rule_set,
    )
    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        server.stop(grace=0)


if __name__ == "__main__":
    main()
