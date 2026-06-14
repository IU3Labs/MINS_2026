package gruzomarket.ru.tools.repository;

import gruzomarket.ru.tools.entity.Customer;
import gruzomarket.ru.tools.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByCustomerAndProductId(Customer customer, Long productId);

    List<Favorite> findByCustomerOrderByCreatedAtDesc(Customer customer);

    boolean existsByCustomerAndProductId(Customer customer, Long productId);

    long countByCustomer(Customer customer);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.customer = :customer")
    void deleteAllByCustomer(@Param("customer") Customer customer);

    @Modifying
    void deleteByCustomerAndProductId(Customer customer, Long productId);

    @Query("SELECT f.productId FROM Favorite f WHERE f.customer = :customer")
    List<Long> findProductIdsByCustomer(@Param("customer") Customer customer);
}
