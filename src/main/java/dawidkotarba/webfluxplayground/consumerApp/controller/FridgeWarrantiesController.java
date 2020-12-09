package dawidkotarba.webfluxplayground.consumerApp.controller;

import dawidkotarba.webfluxplayground.consumerApp.model.FridgeWarrantyDto;
import dawidkotarba.webfluxplayground.consumerApp.service.FridgeAggregatedWarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
public class FridgeWarrantiesController {
    private final FridgeAggregatedWarrantyService warrantyService;

    @Autowired
    public FridgeWarrantiesController(final FridgeAggregatedWarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    // http://localhost:8080/warranties
//    @GetMapping(path = "/warranties", produces = APPLICATION_JSON_VALUE) // example with improper produces type
    @GetMapping(path = "/warranties", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<FridgeWarrantyDto> streamAllWarranties() {
        return warrantyService.streamAllWarranties();
    }
}
