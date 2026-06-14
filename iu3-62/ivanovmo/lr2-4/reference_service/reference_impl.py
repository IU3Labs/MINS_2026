import logging
import grpc
from concurrent import futures
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

import reference_pb2
import reference_pb2_grpc
from reference_service.models import TARIFFS, VEHICLE_TYPES, VALID_ADDRESSES

logger = logging.getLogger(__name__)

class ReferenceServicer(reference_pb2_grpc.ReferenceServiceServicer):
    def GetTariff(self, request, context):
        zone = request.zone
        tariff = TARIFFS.get(zone)
        if not tariff:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            context.set_details(f"Zone '{zone}' not found")
            return reference_pb2.TariffResponse()
        logger.info(f"Returning tariff for zone {zone}")
        return reference_pb2.TariffResponse(
            cost_per_km=tariff.cost_per_km,
            cost_per_kg=tariff.cost_per_kg,
            base_cost=tariff.base_cost
        )

    def ValidateAddress(self, request, context):
        addr = request.address.lower()
        is_valid = any(valid in addr for valid in VALID_ADDRESSES)
        message = "Address is valid" if is_valid else "Address not in service area"
        logger.info(f"Validate address '{request.address}': {is_valid}")
        return reference_pb2.ValidationResponse(is_valid=is_valid, message=message)

    def GetVehicleTypes(self, request, context):
        types = [
            reference_pb2.VehicleType(
                id=vt.id, name=vt.name, max_weight_kg=vt.max_weight_kg
            ) for vt in VEHICLE_TYPES
        ]
        return reference_pb2.VehicleTypesResponse(types=types)