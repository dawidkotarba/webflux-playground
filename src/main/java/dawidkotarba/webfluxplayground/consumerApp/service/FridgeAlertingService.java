package dawidkotarba.webfluxplayground.consumerApp.service;

import dawidkotarba.webfluxplayground.consumerApp.model.FridgeTemperatureDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
@Slf4j
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
                .filter(FridgeTemperatureDto::isHigh)
                .subscribe(fridgeTemperatureDto -> log.info("ALERT: High temperature for {}: {}", fridgeTemperatureDto.getId(), fridgeTemperatureDto.getFormattedTemperature()));
    }
}
