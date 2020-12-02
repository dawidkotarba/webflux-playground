package dawidkotarba.webfluxplayground.otherApp.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class FridgeWarrantyInfoService {

    final WebClient webClient = WebClient.create("http://localhost:8080");

    @PostConstruct
    @SneakyThrows
    public void init() {
        // get info about warranty for all fridges

        final List<Mono<Integer>> warranties = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            warranties.add(getAggregatedInfoForWarranty(i));
        }
        Flux.merge(warranties).subscribe(remainingWarrantyDays -> System.out.println("Remaining days: " + remainingWarrantyDays));
    }

    public Mono<Integer> getAggregatedInfoForWarranty(final int fridgeId) {
        return webClient.get()
                .uri("/warranty/" + fridgeId)
                .retrieve()
                .bodyToMono(Integer.class)
                .retry();
    }
}
