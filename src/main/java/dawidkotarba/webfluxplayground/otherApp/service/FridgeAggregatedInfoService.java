package dawidkotarba.webfluxplayground.otherApp.service;

import dawidkotarba.webfluxplayground.otherApp.model.FridgeFoodWeightDto;
import dawidkotarba.webfluxplayground.otherApp.model.FridgeTemperatureDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
public class FridgeAggregatedInfoService {
    final WebClient webClient = WebClient.create("http://localhost:8080");

    @PostConstruct
    @SneakyThrows
    public void init() {
        // get aggregated info for all five fridges
        for (int i = 0; i < 5; i++) {
            getAggregatedInfoForFridge(i);
        }
    }

    // will wait for two endpoints to respond and combine the result
    public void getAggregatedInfoForFridge(final int fridgeId) {
        final Flux<FridgeFoodWeightDto> weightFlux = webClient.get()
                .uri("/weight/" + fridgeId)
                .retrieve()
                .bodyToFlux(FridgeFoodWeightDto.class)
                .retry();

        webClient.get()
                .uri("/temp/" + fridgeId)
                .retrieve()
                .bodyToFlux(FridgeTemperatureDto.class)
                .retry()
                .zipWith(weightFlux)
                .subscribe(info -> System.out.println(String.format("INFO: %s, %s", info.getT1(), info.getT2())));
    }
}
