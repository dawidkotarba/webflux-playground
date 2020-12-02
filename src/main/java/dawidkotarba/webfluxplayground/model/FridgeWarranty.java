package dawidkotarba.webfluxplayground.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FridgeWarranty {
    private final int id;
    private final int remainingWarrantyDays;
}
