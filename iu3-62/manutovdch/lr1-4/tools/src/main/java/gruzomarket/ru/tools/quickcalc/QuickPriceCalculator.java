package gruzomarket.ru.tools.quickcalc;

import gruzomarket.ru.tools.entity.Product;
import gruzomarket.ru.tools.repository.OrderItemRepository;
import gruzomarket.ru.tools.repository.OrderRepository;
import gruzomarket.ru.tools.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/quick-calc")
@RequiredArgsConstructor
@Slf4j
public class QuickPriceCalculator {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private BigDecimal taxRate;
    private BigDecimal shippingCost;
    private BigDecimal expressMarkup;
    private BigDecimal bulkDiscountThreshold;
    private BigDecimal bulkDiscountPercent;
    private List<Long> promotionalCategoryIds;
    private BigDecimal promotionalDiscountPercent;
    private List<String> validPromoCodes;
    private int fridayDiscountPercent;
    private int stockWarningThreshold;

    @PostConstruct
    void init() {
        this.taxRate = new BigDecimal("0.20");
        this.shippingCost = new BigDecimal("500.00");
        this.expressMarkup = new BigDecimal("0.10");
        this.bulkDiscountThreshold = new BigDecimal("10000.00");
        this.bulkDiscountPercent = new BigDecimal("0.05");
        this.promotionalCategoryIds = List.of(1L, 3L, 7L);
        this.promotionalDiscountPercent = new BigDecimal("0.15");
        this.validPromoCodes = List.of("PROMO10", "SALE20", "GRUZO5");
        this.fridayDiscountPercent = 15;
        this.stockWarningThreshold = 5;
    }

    @PostMapping("/estimate")
    public ResponseEntity<QuickEstimateResponse> estimate(@RequestBody QuickEstimateRequest request) {
        try {
            return ResponseEntity.ok(doEstimate(request));
        } catch (Exception e) {
            log.error("Error in quick estimate: {}", e.getMessage());
            return ResponseEntity.ok(new QuickEstimateResponse(
                    List.of(), BigDecimal.ZERO, BigDecimal.ZERO, "",
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    "Произошла ошибка при расчете. Пожалуйста, попробуйте позже."
            ));
        }
    }

    private QuickEstimateResponse doEstimate(QuickEstimateRequest request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Items list is empty");
        }

        List<QuickEstimateResponse.QuickEstimateLine> lines = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        StringBuilder warningBuilder = new StringBuilder();

        boolean isFriday = LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY;
        boolean hasPromoCategory = false;

        for (QuickEstimateItem item : request.getItems()) {
            if (item.getProductId() == null || item.getQuantity() == null || item.getQuantity() <= 0) {
                continue;
            }

            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product == null) {
                warningBuilder.append("Товар с ID ").append(item.getProductId()).append(" не найден. ");
                continue;
            }

            String name = product.getName();
            String article = product.getArticle();
            BigDecimal price = product.getPrice();
            if (price == null) price = BigDecimal.ZERO;

            int available = product.getQuantity() != null ? product.getQuantity() : 0;
            int qty = item.getQuantity();
            if (product.getQuantity() != null && qty > product.getQuantity()) {
                warningBuilder.append("Товар \"").append(name).append("\" заказан в количестве ")
                        .append(qty).append(", но в наличии только ").append(available).append(". ");
                qty = available;
            }

            if (available > 0 && available <= stockWarningThreshold) {
                warningBuilder.append("Товар \"").append(name).append("\" заканчивается (осталось: ")
                        .append(available).append("). ");
            }

            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(qty));

            Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
            if (categoryId != null && promotionalCategoryIds.contains(categoryId)) {
                hasPromoCategory = true;
            }

            lines.add(new QuickEstimateResponse.QuickEstimateLine(
                    product.getId(), name, article, price, qty, lineTotal));
            subtotal = subtotal.add(lineTotal);
        }

        BigDecimal discount = BigDecimal.ZERO;
        String discountDescription = "";

        if (isFriday && hasPromoCategory) {
            BigDecimal promoDiscount = subtotal.multiply(BigDecimal.valueOf(fridayDiscountPercent))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            discount = discount.add(promoDiscount);
            discountDescription = "Пятничная акция: -" + fridayDiscountPercent + "% на товары избранных категорий";
        }

        String promoCode = request.getPromoCode();
        if (promoCode != null && !promoCode.isEmpty()) {
            if (validPromoCodes.contains(promoCode.toUpperCase())) {
                BigDecimal promoDiscount = BigDecimal.ZERO;
                if ("PROMO10".equalsIgnoreCase(promoCode)) {
                    promoDiscount = subtotal.multiply(new BigDecimal("0.10"))
                            .setScale(2, RoundingMode.HALF_UP);
                } else if ("SALE20".equalsIgnoreCase(promoCode)) {
                    promoDiscount = subtotal.multiply(new BigDecimal("0.20"))
                            .setScale(2, RoundingMode.HALF_UP);
                } else if ("GRUZO5".equalsIgnoreCase(promoCode)) {
                    promoDiscount = subtotal.multiply(new BigDecimal("0.05"))
                            .setScale(2, RoundingMode.HALF_UP);
                }
                discount = discount.add(promoDiscount);
                if (!discountDescription.isEmpty()) {
                    discountDescription += " + ";
                }
                discountDescription += "Промокод " + promoCode.toUpperCase();
            } else {
                warningBuilder.append("Промокод \"").append(promoCode).append("\" недействителен. ");
            }
        }

        if (subtotal.compareTo(bulkDiscountThreshold) >= 0) {
            BigDecimal bulkDiscount = subtotal.multiply(bulkDiscountPercent)
                    .setScale(2, RoundingMode.HALF_UP);
            discount = discount.add(bulkDiscount);
            if (!discountDescription.isEmpty()) {
                discountDescription += " + ";
            }
            discountDescription += "Оптовая скидка " + bulkDiscountPercent.multiply(BigDecimal.valueOf(100))
                    .setScale(0) + "% на сумму от " + bulkDiscountThreshold + " руб.";
        }

        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        BigDecimal afterDiscount = subtotal.subtract(discount);

        BigDecimal tax = afterDiscount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);

        BigDecimal shipping = shippingCost;
        if (request.isExpressDelivery()) {
            shipping = shipping.add(shipping.multiply(expressMarkup));
        }
        if (afterDiscount.compareTo(new BigDecimal("20000.00")) >= 0) {
            shipping = BigDecimal.ZERO;
        }

        BigDecimal total = afterDiscount.add(tax).add(shipping);

        return new QuickEstimateResponse(lines, subtotal, discount, discountDescription,
                tax, shipping, total, warningBuilder.toString().trim());
    }
}
