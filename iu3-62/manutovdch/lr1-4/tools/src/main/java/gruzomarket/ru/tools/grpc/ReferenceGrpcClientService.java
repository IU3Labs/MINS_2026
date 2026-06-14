package gruzomarket.ru.tools.grpc;

import gruzomarket.reference.grpc.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
@Slf4j
public class ReferenceGrpcClientService {

    private final AtomicInteger failureCount = new AtomicInteger(0);
    private volatile boolean circuitOpen = false;
    private volatile long circuitOpenTime = 0;
    private static final int FAILURE_THRESHOLD = 3;
    private static final long CIRCUIT_TIMEOUT_MS = 30_000;

    @GrpcClient("reference-service")
    private ReferenceServiceGrpc.ReferenceServiceBlockingStub referenceStub;

    public Optional<ProductResponse> getProduct(Long id) {
        var resp = callWithTrace(() -> referenceStub.getProduct(
                GetProductRequest.newBuilder().setId(id).build()));
        if (resp.isPresent() && resp.get().getId() != 0) {
            return Optional.of(resp.get());
        }
        return Optional.empty();
    }

    public List<ProductResponse> getProductsByIds(List<Long> ids) {
        if (ids.isEmpty()) return List.of();
        var resp = callWithTrace(() -> referenceStub.getProductsByIds(
                GetProductsByIdsRequest.newBuilder().addAllIds(ids).build()));
        return resp.map(ProductListResponse::getProductsList).orElse(List.of());
    }

    public List<ProductResponse> searchProducts(String query) {
        var resp = callWithTrace(() -> referenceStub.searchProducts(
                SearchProductsRequest.newBuilder().setQuery(query).build()));
        return resp.map(ProductListResponse::getProductsList).orElse(List.of());
    }

    public boolean deductStock(Long productId, int quantity) {
        var resp = callWithTrace(() -> referenceStub.deductStock(
                DeductStockRequest.newBuilder()
                        .setProductId(productId).setQuantity(quantity).build()));
        return resp.map(DeductStockResponse::getSuccess).orElse(false);
    }

    public List<CategoryResponse> listCategories() {
        var resp = callWithTrace(() -> referenceStub.listCategories(
                ListCategoriesRequest.newBuilder().build()));
        return resp.map(CategoryListResponse::getCategoriesList).orElse(List.of());
    }

    public Optional<CategoryResponse> getCategory(Long id) {
        var resp = callWithTrace(() -> referenceStub.getCategory(
                GetCategoryRequest.newBuilder().setId(id).build()));
        if (resp.isPresent() && resp.get().getId() != 0) {
            return Optional.of(resp.get());
        }
        return Optional.empty();
    }

    public List<CategoryGroupResponse> listCategoryGroups() {
        var resp = callWithTrace(() -> referenceStub.listCategoryGroups(
                ListCategoryGroupsRequest.newBuilder().build()));
        return resp.map(CategoryGroupListResponse::getGroupsList).orElse(List.of());
    }

    public List<BrandResponse> listBrands() {
        var resp = callWithTrace(() -> referenceStub.listBrands(
                ListBrandsRequest.newBuilder().build()));
        return resp.map(BrandListResponse::getBrandsList).orElse(List.of());
    }

    public Optional<BrandResponse> getBrand(Long id) {
        var resp = callWithTrace(() -> referenceStub.getBrand(
                GetBrandRequest.newBuilder().setId(id).build()));
        if (resp.isPresent() && resp.get().getId() != 0) {
            return Optional.of(resp.get());
        }
        return Optional.empty();
    }

    public List<ProductResponse> getProductsByBrand(Long brandId) {
        var resp = callWithTrace(() -> referenceStub.getProductsByBrand(
                GetProductsByBrandRequest.newBuilder().setBrandId(brandId).build()));
        return resp.map(ProductListResponse::getProductsList).orElse(List.of());
    }

    public List<BrandResponse> getBrandsByProduct(Long productId) {
        var resp = callWithTrace(() -> referenceStub.getBrandsByProduct(
                GetBrandsByProductRequest.newBuilder().setProductId(productId).build()));
        return resp.map(BrandListResponse::getBrandsList).orElse(List.of());
    }

    public boolean existsBrandByName(String name) {
        return callWithTrace(() -> referenceStub.existsBrandByName(
                ExistsBrandByNameRequest.newBuilder().setName(name).build()))
                .map(ExistsResponse::getExists)
                .orElse(false);
    }

    public boolean existsProduct(Long id) {
        return callWithTrace(() -> referenceStub.existsProduct(
                ExistsProductRequest.newBuilder().setId(id).build()))
                .map(ExistsResponse::getExists)
                .orElse(false);
    }

    public Map<Long, Long> getProductCategoryIds(List<Long> productIds) {
        if (productIds.isEmpty()) return Map.of();
        return callWithTrace(() -> referenceStub.getProductCategoryIds(
                GetProductCategoryIdsRequest.newBuilder()
                        .addAllProductIds(productIds).build()))
                .map(ProductCategoryIdsResponse::getProductCategoryMap)
                .orElse(Map.of());
    }

    public boolean isHealthy() {
        return callWithTrace(() -> referenceStub.healthCheck(
                HealthCheckRequest.newBuilder().build()))
                .map(r -> "SERVING".equals(r.getStatus()))
                .orElse(false);
    }

    private <T> Optional<T> callWithTrace(Supplier<T> supplier) {
        if (circuitOpen) {
            if (System.currentTimeMillis() - circuitOpenTime > CIRCUIT_TIMEOUT_MS) {
                circuitOpen = false;
                failureCount.set(0);
                log.info("Circuit breaker reset for gRPC after timeout");
            } else {
                log.warn("Circuit breaker OPEN for gRPC, skipping call");
                return Optional.empty();
            }
        }

        String traceId = MDC.get("trace_id");
        try {
            T result = supplier.get();
            failureCount.set(0);
            return Optional.of(result);
        } catch (StatusRuntimeException e) {
            int failures = failureCount.incrementAndGet();
            log.error("gRPC call failed: {} - {}/{} failures", e.getStatus().getDescription(), failures, FAILURE_THRESHOLD);
            if (failures >= FAILURE_THRESHOLD) {
                circuitOpen = true;
                circuitOpenTime = System.currentTimeMillis();
                log.warn("CIRCUIT BREAKER OPENED for gRPC after {} failures", failures);
            }
            return Optional.empty();
        } catch (Exception e) {
            failureCount.incrementAndGet();
            log.error("gRPC call error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
