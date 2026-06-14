package gruzomarket.ru.tools.quickcalc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuickEstimateResponse {
    private List<QuickEstimateLine> lines;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private String discountDescription;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal total;
    private String warning;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuickEstimateLine {
        private Long productId;
        private String productName;
        private String article;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal lineTotal;
    }
}
