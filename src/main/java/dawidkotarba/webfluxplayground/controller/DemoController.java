package dawidkotarba.webfluxplayground.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class DemoController {

    private final Flux<Integer> coldPublisher = Flux.fromIterable(generateCollectionOfIntegers(1000)).delayElements(Duration.ofSeconds(1));
    private final Flux<Integer> hotPublisherShare = Flux.fromIterable(generateCollectionOfIntegers(1000)).delayElements(Duration.ofSeconds(1)).share();
    private final Flux<Integer> hotPublisherCache = Flux.fromIterable(generateCollectionOfIntegers(1000)).delayElements(Duration.ofSeconds(1)).cache();

    // gets all published elements
    private final Flux<Integer> hotPublisherFromColdCache = Flux.from(coldPublisher).cache();
    private final Flux<Integer> hotPublisherFromColdShare = Flux.from(coldPublisher).share();

    // Sequential retrieveal every second
    @GetMapping(path = "/fluxIntegerSequenceNoDelay", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxIntegerSequenceNoDelay() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    // http://localhost:8080/fluxIntegerSequence
    @GetMapping(path = "/fluxIntegerSequence", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxIntegerSequence() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).delayElements(Duration.ofSeconds(1));
    }

    // http://localhost:8080/fluxIntegerSequenceNoMediaType
    @GetMapping(path = "/fluxIntegerSequenceNoMediaType")
    public Flux<Integer> fluxIntegerSequenceNoMediaType() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).delayElements(Duration.ofSeconds(1));
    }

    // http://localhost:8080/coldPublisher
    @GetMapping(path = "/coldPublisher", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> coldPublisher() {
        return coldPublisher;
    }

    /**
     * Share starts from the latest element
     *
     * @return
     */
    // http://localhost:8080/hotPublisherShare
    @GetMapping(path = "/hotPublisherShare", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> hotPublisherShare() {
        return hotPublisherShare;
    }

    /**
     * Cache retains all previous elements
     *
     * @return
     */
    // http://localhost:8080/hotPublisherCache
    @GetMapping(path = "/hotPublisherCache", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> hotPublisherCache() {
        return hotPublisherCache;
    }

    /**
     * Cache retains all previous elements
     *
     * @return
     */
    // http://localhost:8080/hotPublisherFromColdCache
    @GetMapping(path = "/hotPublisherFromColdCache", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> hotPublisherFromColdCache() {
        return hotPublisherFromColdCache;
    }

    /**
     * Share starts from the latest element
     *
     * @return
     */
    // http://localhost:8080/hotPublisherFromColdShare
    @GetMapping(path = "/hotPublisherFromColdShare", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> hotPublisherFromColdShare() {
        return hotPublisherFromColdShare;
    }

    // http://localhost:8080/zipPublishers
    @GetMapping(path = "/zipPublishers", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> zipPublishers() {
        return hotPublisherShare.doOnNext(integer -> System.out.println("Publishing: " + integer))
                .zipWith(coldPublisher, Integer::sum).doOnNext(integer -> System.out.println("Sum of publishers: " + integer));
    }

    private static List<Integer> generateCollectionOfIntegers(final int count) {
        return IntStream.range(0, count).boxed().collect(Collectors.toList());
    }

    // http://localhost:8080/client
    @GetMapping(path = "/client")
    public void client() {
        final WebClient webClient = WebClient.create("http://localhost:8080");
        final Flux<Integer> integerStream = webClient.get()
                .uri("/coldPublisher")
                .retrieve()
                .bodyToFlux(Integer.class);

        integerStream.subscribe(integer -> System.out.println("Subscribed: " + integer));
        integerStream.distinct(integer -> integer % 2 == 0).subscribe(integer -> System.out.println("Even: " + integer));
    }
}
