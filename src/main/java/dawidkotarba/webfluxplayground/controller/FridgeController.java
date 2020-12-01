package dawidkotarba.webfluxplayground.controller;

import dawidkotarba.webfluxplayground.model.FridgeTemperature;
import dawidkotarba.webfluxplayground.service.FridgeTemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class FridgeController {

    private final FridgeTemperatureService temperatureService;

    @Autowired
    public FridgeController(final FridgeTemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    // http://localhost:8080/temp
    @GetMapping(path = "/temp", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeTemperature> streamFridgeTemperature() {
        return temperatureService.getTemperaturePublisher();
    }

    // http://localhost:8080/hist
    @GetMapping(path = "/hist", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeTemperature> streamFridgeTemperatureWithHistory() {
        return temperatureService.getHistoricalPublisher();
    }

    // http://localhost:8080/temp/1
    @GetMapping(path = "/temp/{fridgeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeTemperature> streamFridgeTemperature(@PathVariable final int fridgeId) {
        return temperatureService.getTemperaturePublisher().filter(fridgeTemperature -> fridgeTemperature.getId() == fridgeId);
    }
}
