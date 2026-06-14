package gruzomarket.ru.tools.service;

import gruzomarket.reference.grpc.CategoryGroupResponse;
import gruzomarket.reference.grpc.CategoryResponse;
import gruzomarket.ru.tools.dto.CategoryDTO;
import gruzomarket.ru.tools.dto.CategoryGroupDTO;
import gruzomarket.ru.tools.grpc.ReferenceGrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final ReferenceGrpcClientService referenceClient;

    public List<CategoryDTO> findAll() {
        return referenceClient.listCategories().stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO findById(Long id) {
        return referenceClient.getCategory(id)
                .map(this::toCategoryDTO)
                .orElseThrow(() -> new gruzomarket.ru.tools.exception.NotFoundException("Category not found: " + id));
    }

    public List<CategoryDTO> findByParentId(Long parentId) {
        return referenceClient.listCategories().stream()
                .filter(c -> c.getParentId() == parentId)
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> findByNameContaining(String namePart) {
        return referenceClient.listCategories().stream()
                .filter(c -> c.getName().toLowerCase().contains(namePart.toLowerCase()))
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryGroupDTO> getCategoryGroups() {
        return referenceClient.listCategoryGroups().stream()
                .map(this::toCategoryGroupDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO create(CategoryDTO dto) {
        throw new UnsupportedOperationException("Category creation delegated to Reference Service B");
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        throw new UnsupportedOperationException("Category update delegated to Reference Service B");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Category deletion delegated to Reference Service B");
    }

    private CategoryDTO toCategoryDTO(CategoryResponse c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setParentId(c.getParentId() != 0 ? c.getParentId() : null);
        dto.setProductCount(c.getProductCount());
        dto.setImageUrl(c.getImageUrl());
        return dto;
    }

    private CategoryGroupDTO toCategoryGroupDTO(CategoryGroupResponse g) {
        CategoryGroupDTO dto = new CategoryGroupDTO();
        dto.setGroupName(g.getGroupName());
        dto.setCategories(g.getCategoriesList().stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList()));
        return dto;
    }
}
