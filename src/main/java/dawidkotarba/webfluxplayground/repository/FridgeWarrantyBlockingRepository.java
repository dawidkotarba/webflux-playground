package dawidkotarba.webfluxplayground.repository;

import dawidkotarba.webfluxplayground.model.FridgeWarranty;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This is an example of a blocking service.
 */
@Repository
public class FridgeWarrantyBlockingRepository {
    private final Map<Integer, FridgeWarranty> warranties;

    public FridgeWarrantyBlockingRepository() {
        warranties = new HashMap<>();
        warranties.put(0, new FridgeWarranty(0, 125));
        warranties.put(1, new FridgeWarranty(1, 982));
        warranties.put(2, new FridgeWarranty(2, 51));
        warranties.put(3, new FridgeWarranty(3, 9));
        warranties.put(4, new FridgeWarranty(4, 598));
    }

    @SneakyThrows
    public FridgeWarranty getRemainingWarrantyDays(final int fridgeId) {
        // simulate a blocking resource
        Thread.sleep(new Random().nextInt(4000));
        return warranties.get(fridgeId);
    }
}
