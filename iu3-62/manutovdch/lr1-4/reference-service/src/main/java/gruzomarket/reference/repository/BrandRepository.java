package gruzomarket.reference.repository;

import gruzomarket.reference.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
