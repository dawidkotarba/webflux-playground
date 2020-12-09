package dawidkotarba.webfluxplayground.consumerApp.model;

import lombok.Data;

@Data
public class FridgeTemperatureDto {
    private int id;
    private String formattedTemperature;
    private boolean isHigh;
}
