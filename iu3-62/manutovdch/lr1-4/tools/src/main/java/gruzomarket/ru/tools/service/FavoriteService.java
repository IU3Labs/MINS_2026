package gruzomarket.ru.tools.service;

import gruzomarket.ru.tools.entity.Customer;
import gruzomarket.ru.tools.entity.Favorite;
import gruzomarket.ru.tools.grpc.ReferenceGrpcClientService;
import gruzomarket.reference.grpc.ProductResponse;
import gruzomarket.ru.tools.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ReferenceGrpcClientService referenceClient;
    private final CustomerService customerService;

    @Transactional
    public void addToFavorites(Long productId, String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        if (!referenceClient.existsProduct(productId)) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        if (!favoriteRepository.existsByCustomerAndProductId(customer, productId)) {
            Favorite favorite = new Favorite();
            favorite.setCustomer(customer);
            favorite.setProductId(productId);
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void removeFromFavorites(Long productId, String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        favoriteRepository.deleteByCustomerAndProductId(customer, productId);
    }

    public List<Long> getFavoriteProductIds(String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        return favoriteRepository.findProductIdsByCustomer(customer);
    }

    public List<ProductResponse> getFavoriteProducts(String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        List<Long> ids = favoriteRepository.findProductIdsByCustomer(customer);
        return referenceClient.getProductsByIds(ids).stream()
                .filter(ProductResponse::getIsVisible)
                .toList();
    }

    public boolean isFavorite(Long productId, String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        return favoriteRepository.existsByCustomerAndProductId(customer, productId);
    }

    public long getFavoriteCount(String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        return favoriteRepository.countByCustomer(customer);
    }

    @Transactional
    public void clearFavorites(String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        favoriteRepository.deleteAllByCustomer(customer);
    }

    @Transactional
    public boolean toggleFavorite(Long productId, String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);

        Optional<Favorite> existing = favoriteRepository.findByCustomerAndProductId(customer, productId);

        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            return false;
        } else {
            Favorite favorite = new Favorite();
            favorite.setCustomer(customer);
            favorite.setProductId(productId);
            favoriteRepository.save(favorite);
            return true;
        }
    }
}
