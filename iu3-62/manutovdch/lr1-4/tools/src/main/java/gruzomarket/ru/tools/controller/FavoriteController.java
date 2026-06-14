package gruzomarket.ru.tools.controller;

import gruzomarket.reference.grpc.ProductResponse;
import gruzomarket.ru.tools.dto.FavoriteResponse;
import gruzomarket.ru.tools.dto.ProductDTO;
import gruzomarket.ru.tools.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductDTO>> getUserFavorites() {
        String userEmail = getCurrentUserEmail();
        List<ProductResponse> favorites = favoriteService.getFavoriteProducts(userEmail);
        List<ProductDTO> dtos = favorites.stream()
                .map(this::toProductDTO)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ProductDTO toProductDTO(ProductResponse p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setArticle(p.getArticle());
        dto.setDescription(p.getDescription());
        dto.setPrice(java.math.BigDecimal.valueOf(p.getPrice()));
        dto.setQuantity(p.getQuantity());
        dto.setCategoryId(p.getCategoryId() != 0 ? p.getCategoryId() : null);
        dto.setCategoryName(p.getCategoryName());
        dto.setOriginalAuto(p.getOriginalAuto());
        dto.setIsVisible(p.getIsVisible());
        dto.setImageUrl(p.getImageUrl());
        dto.setAdditionalImageUrls(new java.util.ArrayList<>(p.getAdditionalImageUrlsList()));
        return dto;
    }


    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addToFavorites(@PathVariable Long productId) {
        String userEmail = getCurrentUserEmail();
        favoriteService.addToFavorites(productId, userEmail);

        FavoriteResponse response = new FavoriteResponse();
        response.setProductId(productId);
        response.setFavorite(true);
        response.setFavoriteCount(favoriteService.getFavoriteCount(userEmail));

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> removeFromFavorites(@PathVariable Long productId) {
        String userEmail = getCurrentUserEmail();
        favoriteService.removeFromFavorites(productId, userEmail);

        FavoriteResponse response = new FavoriteResponse();
        response.setProductId(productId);
        response.setFavorite(false);
        response.setFavoriteCount(favoriteService.getFavoriteCount(userEmail));

        return ResponseEntity.ok(response);
    }


    @PostMapping("/toggle/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> toggleFavorite(@PathVariable Long productId) {
        String userEmail = getCurrentUserEmail();
        boolean isNowFavorite = favoriteService.toggleFavorite(productId, userEmail);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("isFavorite", isNowFavorite);
        response.put("favoriteCount", favoriteService.getFavoriteCount(userEmail));

        return ResponseEntity.ok(response);
    }


    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getFavoritesCount() {
        String userEmail = getCurrentUserEmail();
        long count = favoriteService.getFavoriteCount(userEmail);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/check/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long productId) {
        String userEmail = getCurrentUserEmail();
        boolean isFavorite = favoriteService.isFavorite(productId, userEmail);
        return ResponseEntity.ok(isFavorite);
    }


    @DeleteMapping("/clear")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> clearFavorites() {
        String userEmail = getCurrentUserEmail();
        favoriteService.clearFavorites(userEmail);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/ids")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Long>> getFavoriteProductIds() {
        String userEmail = getCurrentUserEmail();
        List<Long> productIds = favoriteService.getFavoriteProductIds(userEmail);
        return ResponseEntity.ok(productIds);
    }
}