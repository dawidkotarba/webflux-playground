package dawidkotarba.webfluxplayground.otherApp.service;

import dawidkotarba.webfluxplayground.otherApp.model.FridgeWarrantyDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class FridgeAggregatedWarrantyService {

    final WebClient webClient = WebClient.create("http://localhost:8080");

    public Flux<FridgeWarrantyDto> streamAllWarranties() {
        final List<Mono<FridgeWarrantyDto>> warranties = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            warranties.add(getWarrantyDays(i));
        }
        return Flux.merge(warranties);
    }

    private Mono<FridgeWarrantyDto> getWarrantyDays(final int fridgeId) {
        return webClient.get()
                .uri("/warranty/" + fridgeId)
                .retrieve()
                .bodyToMono(FridgeWarrantyDto.class)
                .retry();
    }
}
