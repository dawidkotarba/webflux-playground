package dawidkotarba.webfluxplayground.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class DemoController {

    private final Flux<Integer> coldPublisher = Flux.fromIterable(generateCollectionOfIntegers(1000)).delayElements(Duration.ofSeconds(1));
    private final Flux<Integer> hotPublisher = Flux.fromIterable(generateCollectionOfIntegers(1000)).delayElements(Duration.ofSeconds(1)).share();

    // gets all published elements
    private final Flux<Integer> hotPublisherFromCold = Flux.from(coldPublisher).cache();

    // Sequential retrieveal every second
    @GetMapping(path = "/fluxIntegerSequenceNoDelay", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxIntegerSequenceNoDelay() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    // http://localhost:8080/fluxIntegerSequence
    // Sequential retrieveal every second
    @GetMapping(path = "/fluxIntegerSequence", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxIntegerSequence() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).delayElements(Duration.ofSeconds(1));
    }

    // http://localhost:8080/coldPublisher
    @GetMapping(path = "/coldPublisher", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> coldPublisher() {
        return coldPublisher;
    }

    // http://localhost:8080/hotPublisher
    @GetMapping(path = "/hotPublisher", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> hotPublisher() {
        return hotPublisher;
    }

    // http://localhost:8080/hotPublisherFromCold
    @GetMapping(path = "/hotPublisherFromCold", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> hotPublisherFromCold() {
        return hotPublisherFromCold;
    }

    // http://localhost:8080/zipPublishers
    @GetMapping(path = "/zipPublishers", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> zipPublishers() {
        return hotPublisher.doOnNext(integer -> System.out.println("Publishing: " + integer))
                .zipWith(coldPublisher, Integer::sum).doOnNext(integer -> System.out.println("Sum of publishers: " + integer));
    }

    private static List<Integer> generateCollectionOfIntegers(final int count) {
        return IntStream.range(0, count).boxed().collect(Collectors.toList());
    }
}
