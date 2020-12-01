package dawidkotarba.webfluxplayground.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@ToString
@Getter
public class FridgeTemperature {
    private final int id;
    private final String formattedTemperature;
    private final boolean isHigh;

    public FridgeTemperature() {
        id = new Random().nextInt(5);
        final double temp = retrieveTemperature();
        formattedTemperature = formatTemperature(temp);
        isHigh = isTooHigh(temp);
    }

    private double retrieveTemperature() {
        return ThreadLocalRandom.current().nextDouble(0, 1);
    }

    private String formatTemperature(final double temp) {
        return String.format("%.2fC", temp);
    }

    private boolean isTooHigh(final double temp) {
        return temp >= 0.85;
    }
}
