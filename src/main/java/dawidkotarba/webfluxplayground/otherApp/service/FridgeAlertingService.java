package dawidkotarba.webfluxplayground.otherApp.service;

import dawidkotarba.webfluxplayground.otherApp.model.FridgeTemperatureDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
public class FridgeAlertingService {
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
                .bodyToFlux(FridgeTemperatureDto.class)
                .retry()
                .subscribe(fridgeTemperatureDto -> {
                    if (fridgeTemperatureDto.isHigh()) {
                        System.out.printf("ALERT!!! High temperature for %d: %s%n", fridgeTemperatureDto.getId(), fridgeTemperatureDto.getFormattedTemperature());
                    }
                });
    }
}
