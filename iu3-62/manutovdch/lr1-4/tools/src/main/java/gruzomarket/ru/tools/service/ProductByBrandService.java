package gruzomarket.ru.tools.service;

import gruzomarket.reference.grpc.BrandResponse;
import gruzomarket.ru.tools.dto.BrandDTO;
import gruzomarket.ru.tools.dto.ProductByBrandDTO;
import gruzomarket.ru.tools.exception.NotFoundException;
import gruzomarket.ru.tools.grpc.ReferenceGrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductByBrandService {

    private final ReferenceGrpcClientService referenceClient;

    public List<ProductByBrandDTO> findAll() {
        return List.of();
    }

    public ProductByBrandDTO findById(Long productId, Long brandId) {
        throw new NotFoundException("Product-Brand not found: " + productId + ", " + brandId);
    }

    public List<ProductByBrandDTO> findByBrandId(Long brandId) {
        return referenceClient.getProductsByBrand(brandId).stream()
                .map(p -> {
                    ProductByBrandDTO dto = new ProductByBrandDTO();
                    dto.setProductId(p.getId());
                    dto.setBrandId(brandId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductByBrandDTO> findByProductId(Long productId) {
        return referenceClient.getBrandsByProduct(productId).stream()
                .map(b -> {
                    ProductByBrandDTO dto = new ProductByBrandDTO();
                    dto.setProductId(productId);
                    dto.setBrandId(b.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ProductByBrandDTO create(ProductByBrandDTO dto) {
        throw new UnsupportedOperationException("Product-Brand creation delegated to Reference Service B");
    }

    public void delete(Long productId, Long brandId) {
        throw new UnsupportedOperationException("Product-Brand deletion delegated to Reference Service B");
    }

    private BrandDTO toBrandDTO(BrandResponse b) {
        BrandDTO dto = new BrandDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setProductCount(b.getProductCount());
        return dto;
    }
}
