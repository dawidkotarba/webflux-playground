package dawidkotarba.webfluxplayground.otherApp.model;

import lombok.Data;

@Data
public class FridgeTemp {
    private int id;
    private String formattedTemperature;
    private boolean isHigh;
}
