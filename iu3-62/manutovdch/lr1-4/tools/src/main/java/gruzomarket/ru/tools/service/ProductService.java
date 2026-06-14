package gruzomarket.ru.tools.service;

import gruzomarket.reference.grpc.ProductResponse;
import gruzomarket.ru.tools.dto.ProductDTO;
import gruzomarket.ru.tools.dto.ProductSearchResponse;
import gruzomarket.ru.tools.grpc.ReferenceGrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ReferenceGrpcClientService referenceClient;

    public List<ProductDTO> findAll() {
        return referenceClient.searchProducts("").stream()
                .map(this::toProductDTO)
                .toList();
    }

    public List<ProductDTO> findAllVisible() {
        return referenceClient.searchProducts("").stream()
                .filter(ProductResponse::getIsVisible)
                .map(this::toProductDTO)
                .toList();
    }

    public ProductDTO findById(Long id) {
        return referenceClient.getProduct(id)
                .map(this::toProductDTO)
                .orElseThrow(() -> new gruzomarket.ru.tools.exception.NotFoundException("Product not found: " + id));
    }

    public List<ProductDTO> findByNameContaining(String name) {
        return referenceClient.searchProducts(name).stream()
                .map(this::toProductDTO)
                .toList();
    }

    public List<ProductDTO> findByCategoryId(Long categoryId) {
        return referenceClient.searchProducts("").stream()
                .filter(p -> p.getCategoryId() == categoryId)
                .map(this::toProductDTO)
                .toList();
    }

    public ProductDTO findByArticle(String article) {
        return referenceClient.searchProducts(article).stream()
                .filter(p -> article.equalsIgnoreCase(p.getArticle()))
                .findFirst()
                .map(this::toProductDTO)
                .orElseThrow(() -> new gruzomarket.ru.tools.exception.NotFoundException("Product not found by article: " + article));
    }

    public ProductSearchResponse<ProductDTO> search(String q, String categoryIds, String brandIds,
                                                     BigDecimal minPrice, BigDecimal maxPrice,
                                                     Boolean inStock, int page, int size, String sort) {
        Stream<ProductResponse> stream = referenceClient.searchProducts(q != null ? q : "").stream();

        if (categoryIds != null && !categoryIds.isBlank()) {
            Set<Long> catIds = Arrays.stream(categoryIds.split(","))
                    .map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            stream = stream.filter(p -> catIds.contains(p.getCategoryId()));
        }

        if (brandIds != null && !brandIds.isBlank()) {
            Set<Long> bIds = Arrays.stream(brandIds.split(","))
                    .map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            Set<Long> validProductIds = new HashSet<>();
            for (Long brandId : bIds) {
                referenceClient.getProductsByBrand(brandId).stream()
                        .map(ProductResponse::getId).forEach(validProductIds::add);
            }
            stream = stream.filter(p -> validProductIds.contains(p.getId()));
        }

        if (minPrice != null) {
            stream = stream.filter(p -> BigDecimal.valueOf(p.getPrice()).compareTo(minPrice) >= 0);
        }
        if (maxPrice != null) {
            stream = stream.filter(p -> BigDecimal.valueOf(p.getPrice()).compareTo(maxPrice) <= 0);
        }
        if (inStock != null && inStock) {
            stream = stream.filter(p -> p.getQuantity() > 0);
        }

        List<ProductDTO> all = stream.map(this::toProductDTO).toList();

        if (sort != null) {
            Comparator<ProductDTO> comp = switch (sort) {
                case "price_asc" -> Comparator.comparing(ProductDTO::getPrice);
                case "price_desc" -> Comparator.comparing(ProductDTO::getPrice).reversed();
                case "name_desc" -> Comparator.comparing(ProductDTO::getName).reversed();
                default -> Comparator.comparing(ProductDTO::getName);
            };
            all = all.stream().sorted(comp).toList();
        }

        int total = all.size();
        int from = page * size;
        int to = Math.min(from + size, total);
        List<ProductDTO> content = from < total ? all.subList(from, to) : List.of();

        var resp = new ProductSearchResponse<ProductDTO>();
        resp.setContent(content);
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalElements((long) total);
        resp.setTotalPages((int) Math.ceil((double) total / size));
        resp.setFirst(page == 0);
        resp.setLast(from + size >= total);
        return resp;
    }

    public ProductDTO create(ProductDTO dto) {
        throw new UnsupportedOperationException("Product creation delegated to Reference Service B");
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        throw new UnsupportedOperationException("Product update delegated to Reference Service B");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Product deletion delegated to Reference Service B");
    }

    public void save(ProductDTO dto) {
        throw new UnsupportedOperationException("Product save delegated to Reference Service B");
    }

    public ProductDTO getProductById(Long id) {
        return findById(id);
    }

    private ProductDTO toProductDTO(ProductResponse p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setArticle(p.getArticle());
        dto.setDescription(p.getDescription());
        dto.setPrice(BigDecimal.valueOf(p.getPrice()));
        dto.setQuantity(p.getQuantity());
        dto.setCategoryId(p.getCategoryId() != 0 ? p.getCategoryId() : null);
        dto.setCategoryName(p.getCategoryName());
        dto.setOriginalAuto(p.getOriginalAuto());
        dto.setIsVisible(p.getIsVisible());
        dto.setImageUrl(p.getImageUrl());
        dto.setAdditionalImageUrls(new ArrayList<>(p.getAdditionalImageUrlsList()));
        return dto;
    }
}
