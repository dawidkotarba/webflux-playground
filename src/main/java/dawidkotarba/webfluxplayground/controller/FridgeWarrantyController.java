package dawidkotarba.webfluxplayground.controller;

import dawidkotarba.webfluxplayground.model.FridgeWarranty;
import dawidkotarba.webfluxplayground.service.FridgeWarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class FridgeWarrantyController {
    private final FridgeWarrantyService warrantyService;

    @Autowired
    public FridgeWarrantyController(final FridgeWarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    // http://localhost:8080/warranty/1
    @GetMapping(path = "/warranty/{fridgeId}", produces = APPLICATION_JSON_VALUE)
    public Mono<FridgeWarranty> getWarrantyDays(@PathVariable final int fridgeId) {
        return warrantyService.getRemainingWarrantyDays(fridgeId);
    }
}
