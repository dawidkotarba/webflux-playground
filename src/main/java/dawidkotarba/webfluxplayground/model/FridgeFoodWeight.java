package dawidkotarba.webfluxplayground.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@ToString
@Getter
public class FridgeFoodWeight {
    private final int id;
    private final String formattedWeight;

    public FridgeFoodWeight() {
        id = new Random().nextInt(5);
        formattedWeight = retrieveFoodWeight() + "kg";
    }

    private int retrieveFoodWeight() {
        return ThreadLocalRandom.current().nextInt(200, 250);
    }
}
