package dawidkotarba.webfluxplayground.controller;

import dawidkotarba.webfluxplayground.model.FridgeTemperature;
import dawidkotarba.webfluxplayground.service.FridgeTemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
public class FridgeTemperatureController {

    private final FridgeTemperatureService temperatureService;

    @Autowired
    public FridgeTemperatureController(final FridgeTemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    // http://localhost:8080/temp
    @GetMapping(path = "/temp", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeTemperature> streamFridgeTemperature() {
        return temperatureService.getTemperaturePublisher();
    }

    // http://localhost:8080/temp-hist
    @GetMapping(path = "/temp-hist", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeTemperature> streamFridgeTemperatureWithHistory() {
        return temperatureService.getHistoricalPublisher();
    }

    // http://localhost:8080/temp/1
    @GetMapping(path = "/temp/{fridgeId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeTemperature> streamFridgeTemperature(@PathVariable final int fridgeId) {
        return temperatureService.getTemperaturePublisher().filter(fridgeTemperature -> fridgeTemperature.getId() == fridgeId);
    }

    // http://localhost:8080/temp-avg/5
    @GetMapping(path = "/temp-avg/{entries}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> averageTemperature(@PathVariable final int entries) {
        return temperatureService.getTemperaturePublisher()
                .doOnNext(fridgeTemperature -> System.out.println("Taking into avg: " + fridgeTemperature.getFormattedTemperature()))
                .take(entries)
                .collect(Collectors.averagingDouble(FridgeTemperature::getTemperature))
                .map(avg -> String.format("%.2f°C", avg));
    }
}
