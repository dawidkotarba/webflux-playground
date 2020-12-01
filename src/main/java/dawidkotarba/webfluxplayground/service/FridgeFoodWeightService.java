package dawidkotarba.webfluxplayground.service;

import dawidkotarba.webfluxplayground.model.FridgeFoodWeight;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Getter
public class FridgeFoodWeightService {
    private final Flux<FridgeFoodWeight> weightPublisher = streamWeight().share();
    private final Flux<FridgeFoodWeight> historicalPublisher = weightPublisher.cache(10000);

    private Flux<FridgeFoodWeight> streamWeight() {
        return Flux.<FridgeFoodWeight>generate(fluxSink -> fluxSink.next(new FridgeFoodWeight()))
                .delayElements(Duration.ofSeconds(1));
    }
}
