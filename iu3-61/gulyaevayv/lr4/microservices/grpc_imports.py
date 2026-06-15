import sys
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parent.parent
GRPC_API_DIR = PROJECT_ROOT / "grpc_api"

if str(GRPC_API_DIR) not in sys.path:
    sys.path.insert(0, str(GRPC_API_DIR))
