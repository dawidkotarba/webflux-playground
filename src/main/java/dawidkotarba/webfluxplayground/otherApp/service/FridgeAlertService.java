package dawidkotarba.webfluxplayground.otherApp.service;

import dawidkotarba.webfluxplayground.otherApp.model.FridgeTemp;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
public class FridgeAlertService {
    final WebClient webClient = WebClient.create("http://localhost:8080");

    @PostConstruct
    @SneakyThrows
    public void init() {
        alertWhenTemperatureIsTooHigh();
    }

    public void alertWhenTemperatureIsTooHigh() {
        webClient.get()
                .uri("/temp")
                .retrieve()
                .bodyToFlux(FridgeTemp.class)
                .retry()
                .subscribe(fridgeTemp -> {
                    if (fridgeTemp.isHigh()) {
                        System.out.printf("ALERT!!! High temperature for %d: %s%n", fridgeTemp.getId(), fridgeTemp.getFormattedTemperature());
                    }
                });
    }
}
