package gruzomarket.reference.grpc;

import gruzomarket.reference.entity.Brand;
import gruzomarket.reference.entity.Category;
import gruzomarket.reference.entity.Product;
import gruzomarket.reference.entity.ProductByBrand;
import gruzomarket.reference.entity.ProductImage;
import gruzomarket.reference.repository.BrandRepository;
import gruzomarket.reference.repository.CategoryRepository;
import gruzomarket.reference.repository.ProductByBrandRepository;
import gruzomarket.reference.repository.ProductImageRepository;
import gruzomarket.reference.repository.ProductRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService
@Transactional(readOnly = true)
public class ReferenceGrpcServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(ReferenceGrpcServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductByBrandRepository productByBrandRepository;
    private final ProductImageRepository productImageRepository;

    public ReferenceGrpcServiceImpl(ProductRepository productRepository,
                                    CategoryRepository categoryRepository,
                                    BrandRepository brandRepository,
                                    ProductByBrandRepository productByBrandRepository,
                                    ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.productByBrandRepository = productByBrandRepository;
        this.productImageRepository = productImageRepository;
    }

    // ---- Products ----

    @Override
    public void getProduct(GetProductRequest req, StreamObserver<ProductResponse> response) {
        log.info("gRPC getProduct: id={} [traceId={}]", req.getId(), MDC.get("traceId"));
        productRepository.findById(req.getId())
                .ifPresentOrElse(
                        p -> response.onNext(toProductResponse(p)),
                        () -> response.onNext(ProductResponse.newBuilder().build())
                );
        response.onCompleted();
    }

    @Override
    public void getProductsByIds(GetProductsByIdsRequest req, StreamObserver<ProductListResponse> response) {
        log.info("gRPC getProductsByIds: ids={}", req.getIdsList());
        List<Product> products = productRepository.findAllById(req.getIdsList());
        response.onNext(ProductListResponse.newBuilder()
                .addAllProducts(products.stream().map(this::toProductResponse).toList())
                .build());
        response.onCompleted();
    }

    @Override
    public void searchProducts(SearchProductsRequest req, StreamObserver<ProductListResponse> response) {
        log.info("gRPC searchProducts: query={}, MDC trace_id=[{}]", req.getQuery(), MDC.get("trace_id"));
        List<Product> products = productRepository.findByNameContainingIgnoreCase(req.getQuery());
        response.onNext(ProductListResponse.newBuilder()
                .addAllProducts(products.stream().map(this::toProductResponse).toList())
                .build());
        response.onCompleted();
    }

    @Override
    @Transactional
    public void deductStock(DeductStockRequest req, StreamObserver<DeductStockResponse> response) {
        log.info("gRPC deductStock: productId={}, qty={}", req.getProductId(), req.getQuantity());
        var opt = productRepository.findById(req.getProductId());
        if (opt.isEmpty()) {
            response.onNext(DeductStockResponse.newBuilder()
                    .setSuccess(false).setMessage("Product not found").build());
        } else {
            Product p = opt.get();
            int current = p.getQuantity() != null ? p.getQuantity() : 0;
            int remaining = Math.max(0, current - req.getQuantity());
            p.setQuantity(remaining);
            productRepository.save(p);
            response.onNext(DeductStockResponse.newBuilder()
                    .setSuccess(true).setMessage("Stock deducted to " + remaining).build());
        }
        response.onCompleted();
    }

    // ---- Categories ----

    @Override
    public void getCategory(GetCategoryRequest req, StreamObserver<CategoryResponse> response) {
        categoryRepository.findById(req.getId())
                .ifPresentOrElse(
                        c -> response.onNext(toCategoryResponse(c)),
                        () -> response.onNext(CategoryResponse.newBuilder().build())
                );
        response.onCompleted();
    }

    @Override
    public void listCategories(ListCategoriesRequest req, StreamObserver<CategoryListResponse> response) {
        List<Category> categories = categoryRepository.findAll();
        response.onNext(CategoryListResponse.newBuilder()
                .addAllCategories(categories.stream().map(this::toCategoryResponse).toList())
                .build());
        response.onCompleted();
    }

    @Override
    public void listCategoryGroups(ListCategoryGroupsRequest req, StreamObserver<CategoryGroupListResponse> response) {
        Map<Long, CategoryResponse> all = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, this::toCategoryResponse));

        List<CategoryGroupResponse> groups = new ArrayList<>();

        groups.add(buildGroup("Двигатель и силовая установка",
                List.of(22L, 8L, 41L, 13L, 23L, 2L, 34L, 32L, 38L), all));
        groups.add(buildGroup("Трансмиссия и привод",
                List.of(33L, 17L, 7L, 27L, 6L, 16L, 40L, 42L), all));
        groups.add(buildGroup("Ходовая часть и подвеска",
                List.of(1L, 25L, 30L, 37L, 3L, 21L, 12L), all));
        groups.add(buildGroup("Рулевое управление",
                List.of(29L, 19L), all));
        groups.add(buildGroup("Тормозная система",
                List.of(35L, 5L), all));
        groups.add(buildGroup("Кузов и электроника",
                List.of(14L, 18L, 44L, 4L), all));
        groups.add(buildGroup("Вспомогательные системы",
                List.of(36L, 20L, 43L, 39L, 10L), all));
        groups.add(buildGroup("Ремонт и прочее",
                List.of(28L, 24L, 31L, 15L, 11L, 9L), all));

        response.onNext(CategoryGroupListResponse.newBuilder().addAllGroups(groups).build());
        response.onCompleted();
    }

    // ---- Brands ----

    @Override
    public void getBrand(GetBrandRequest req, StreamObserver<BrandResponse> response) {
        brandRepository.findById(req.getId())
                .ifPresentOrElse(
                        b -> response.onNext(toBrandResponse(b)),
                        () -> response.onNext(BrandResponse.newBuilder().build())
                );
        response.onCompleted();
    }

    @Override
    public void listBrands(ListBrandsRequest req, StreamObserver<BrandListResponse> response) {
        List<Brand> brands = brandRepository.findAll();
        response.onNext(BrandListResponse.newBuilder()
                .addAllBrands(brands.stream().map(this::toBrandResponse).toList())
                .build());
        response.onCompleted();
    }

    @Override
    public void existsBrandByName(ExistsBrandByNameRequest req, StreamObserver<ExistsResponse> response) {
        boolean exists = brandRepository.existsByNameIgnoreCase(req.getName());
        response.onNext(ExistsResponse.newBuilder().setExists(exists).build());
        response.onCompleted();
    }

    // ---- Product-Brand relations ----

    @Override
    public void getBrandsByProduct(GetBrandsByProductRequest req, StreamObserver<BrandListResponse> response) {
        List<ProductByBrand> links = productByBrandRepository.findByProductId(req.getProductId());
        List<Brand> brands = links.stream().map(ProductByBrand::getBrand).toList();
        response.onNext(BrandListResponse.newBuilder()
                .addAllBrands(brands.stream().map(this::toBrandResponse).toList())
                .build());
        response.onCompleted();
    }

    @Override
    public void getProductsByBrand(GetProductsByBrandRequest req, StreamObserver<ProductListResponse> response) {
        List<ProductByBrand> links = productByBrandRepository.findByBrandId(req.getBrandId());
        List<Product> products = links.stream().map(ProductByBrand::getProduct).toList();
        response.onNext(ProductListResponse.newBuilder()
                .addAllProducts(products.stream().map(this::toProductResponse).toList())
                .build());
        response.onCompleted();
    }

    // ---- Validation ----

    @Override
    public void existsProduct(ExistsProductRequest req, StreamObserver<ExistsResponse> response) {
        boolean exists = productRepository.existsById(req.getId());
        response.onNext(ExistsResponse.newBuilder().setExists(exists).build());
        response.onCompleted();
    }

    @Override
    public void getProductCategoryIds(GetProductCategoryIdsRequest req, StreamObserver<ProductCategoryIdsResponse> response) {
        Map<Long, Long> map = new HashMap<>();
        productRepository.findAllById(req.getProductIdsList()).forEach(p -> {
            if (p.getCategory() != null) {
                map.put(p.getId(), p.getCategory().getId());
            }
        });
        response.onNext(ProductCategoryIdsResponse.newBuilder().putAllProductCategoryMap(map).build());
        response.onCompleted();
    }

    // ---- Health ----

    @Override
    public void healthCheck(HealthCheckRequest req, StreamObserver<HealthCheckResponse> response) {
        response.onNext(HealthCheckResponse.newBuilder().setStatus("SERVING").build());
        response.onCompleted();
    }

    // ---- Mappers ----

    private ProductResponse toProductResponse(Product p) {
        var builder = ProductResponse.newBuilder()
                .setId(p.getId())
                .setName(p.getName() != null ? p.getName() : "")
                .setArticle(p.getArticle() != null ? p.getArticle() : "")
                .setDescription(p.getDescription() != null ? p.getDescription() : "")
                .setPrice(p.getPrice() != null ? p.getPrice().doubleValue() : 0.0)
                .setQuantity(p.getQuantity() != null ? p.getQuantity() : 0)
                .setOriginalAuto(p.getOriginalAuto() != null ? p.getOriginalAuto() : "")
                .setIsVisible(p.getIsVisible() != null ? p.getIsVisible() : true)
                .setImageUrl(p.getImageUrl() != null ? p.getImageUrl() : "");

        if (p.getCategory() != null) {
            builder.setCategoryId(p.getCategory().getId());
            builder.setCategoryName(p.getCategory().getName() != null ? p.getCategory().getName() : "");
        }

        List<ProductImage> images = productImageRepository.findByProductId(p.getId());
        for (ProductImage img : images) {
            if (img.getImageUrl() != null) {
                builder.addAdditionalImageUrls(img.getImageUrl());
            }
        }

        return builder.build();
    }

    private CategoryResponse toCategoryResponse(Category c) {
        var builder = CategoryResponse.newBuilder()
                .setId(c.getId())
                .setName(c.getName() != null ? c.getName() : "")
                .setProductCount(c.getProductCount() != null ? c.getProductCount() : 0)
                .setImageUrl(c.getImageUrl() != null ? c.getImageUrl() : "");
        if (c.getParent() != null) {
            builder.setParentId(c.getParent().getId());
        }
        return builder.build();
    }

    private BrandResponse toBrandResponse(Brand b) {
        return BrandResponse.newBuilder()
                .setId(b.getId())
                .setName(b.getName() != null ? b.getName() : "")
                .setProductCount(b.getProductCount() != null ? b.getProductCount() : 0)
                .build();
    }

    private CategoryGroupResponse buildGroup(String name, List<Long> ids, Map<Long, CategoryResponse> all) {
        return CategoryGroupResponse.newBuilder()
                .setGroupName(name)
                .addAllCategories(ids.stream().filter(all::containsKey).map(all::get).toList())
                .build();
    }
}
