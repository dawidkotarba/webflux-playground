package dawidkotarba.webfluxplayground.service;

import dawidkotarba.webfluxplayground.model.FridgeTemperature;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Getter
public class FridgeTemperatureService {
    private final Flux<FridgeTemperature> temperaturePublisher = streamTemperature().share();
    private final Flux<FridgeTemperature> historicalPublisher = temperaturePublisher.cache(10000);

    private Flux<FridgeTemperature> streamTemperature() {
        return Flux.<FridgeTemperature>generate(fluxSink -> fluxSink.next(new FridgeTemperature()))
                .delayElements(Duration.ofMillis(500));
    }
}
