from __future__ import annotations

import argparse

from lab4_microservices.reference_service.server import serve


def main() -> None:
    parser = argparse.ArgumentParser(description="Run Reference Service")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=50052)
    args = parser.parse_args()

    server = serve(host=args.host, port=args.port)
    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        server.stop(grace=0)


if __name__ == "__main__":
    main()
