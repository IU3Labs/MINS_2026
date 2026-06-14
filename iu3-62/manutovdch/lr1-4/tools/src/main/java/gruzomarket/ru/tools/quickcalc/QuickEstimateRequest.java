package gruzomarket.ru.tools.quickcalc;

import lombok.Data;
import java.util.List;

@Data
public class QuickEstimateRequest {
    private List<QuickEstimateItem> items;
    private boolean expressDelivery;
    private String promoCode;
}
