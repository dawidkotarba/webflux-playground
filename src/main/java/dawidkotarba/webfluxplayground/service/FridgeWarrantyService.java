package dawidkotarba.webfluxplayground.service;

import dawidkotarba.webfluxplayground.model.FridgeWarranty;
import dawidkotarba.webfluxplayground.repository.FridgeWarrantyBlockingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * The warranty service shows a possible solution of handling blocking resources by bounding them
 * to a different scheduler.
 */
@Service
public class FridgeWarrantyService {

    private final FridgeWarrantyBlockingRepository repository;

    @Autowired
    public FridgeWarrantyService(final FridgeWarrantyBlockingRepository repository) {
        this.repository = repository;
    }

    public Mono<FridgeWarranty> getRemainingWarrantyDays(final int fridgeId) {
        return Mono.just(repository.getRemainingWarrantyDays(fridgeId)).subscribeOn(Schedulers.boundedElastic());
    }
}
