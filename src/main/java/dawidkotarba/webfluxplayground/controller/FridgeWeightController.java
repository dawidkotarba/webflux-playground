package dawidkotarba.webfluxplayground.controller;

import dawidkotarba.webfluxplayground.model.FridgeFoodWeight;
import dawidkotarba.webfluxplayground.service.FridgeFoodWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
public class FridgeWeightController {

    private final FridgeFoodWeightService weightService;

    @Autowired
    public FridgeWeightController(final FridgeFoodWeightService weightService) {
        this.weightService = weightService;
    }

    // http://localhost:8080/weight
    @GetMapping(path = "/weight", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeFoodWeight> streamFridgeFoodWeight() {
        return weightService.getWeightPublisher();
    }

    // http://localhost:8080/weight-hist
    @GetMapping(path = "/weight-hist", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeFoodWeight> streamFridgeWeightWithHistory() {
        return weightService.getHistoricalPublisher();
    }

    // http://localhost:8080/weight/1
    @GetMapping(path = "/weight/{fridgeId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeFoodWeight> streamFridgeWeight(@PathVariable final int fridgeId) {
        return weightService.getWeightPublisher().filter(fridgeFoodWeight -> fridgeFoodWeight.getId() == fridgeId);
    }
}
