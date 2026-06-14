package gruzomarket.ru.tools.service;

import gruzomarket.reference.grpc.BrandResponse;
import gruzomarket.ru.tools.dto.BrandDTO;
import gruzomarket.ru.tools.grpc.ReferenceGrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {

    private final ReferenceGrpcClientService referenceClient;

    public List<BrandDTO> findAll() {
        return referenceClient.listBrands().stream()
                .map(this::toBrandDTO)
                .collect(Collectors.toList());
    }

    public BrandDTO findById(Long id) {
        return referenceClient.getBrand(id)
                .map(this::toBrandDTO)
                .orElseThrow(() -> new gruzomarket.ru.tools.exception.NotFoundException("Brand not found: " + id));
    }

    public BrandDTO findByName(String name) {
        return findAll().stream()
                .filter(b -> b.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean existsByName(String name) {
        return referenceClient.existsBrandByName(name);
    }

    public BrandDTO create(BrandDTO dto) {
        throw new UnsupportedOperationException("Brand creation delegated to Reference Service B");
    }

    public BrandDTO update(Long id, BrandDTO dto) {
        throw new UnsupportedOperationException("Brand update delegated to Reference Service B");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Brand deletion delegated to Reference Service B");
    }

    private BrandDTO toBrandDTO(BrandResponse b) {
        BrandDTO dto = new BrandDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setProductCount(b.getProductCount());
        return dto;
    }
}
