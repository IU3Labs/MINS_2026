package gruzomarket.reference.repository;

import gruzomarket.reference.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);
    List<Category> findByNameContainingIgnoreCase(String namePart);
}
