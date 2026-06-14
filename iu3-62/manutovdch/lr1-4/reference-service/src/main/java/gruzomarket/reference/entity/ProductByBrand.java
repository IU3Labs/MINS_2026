package gruzomarket.reference.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "product_by_brand")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product", "brand"})
public class ProductByBrand {

    @EmbeddedId
    private ProductBrandId id = new ProductBrandId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("brandId")
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductBrandId implements Serializable {
        private Long productId;
        private Long brandId;
    }
}
