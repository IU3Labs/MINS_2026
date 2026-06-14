package gruzomarket.ru.tools.quickcalc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuickEstimateItem {
    private Long productId;
    private Integer quantity;
}
