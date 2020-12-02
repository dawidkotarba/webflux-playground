package dawidkotarba.webfluxplayground.service;

import dawidkotarba.webfluxplayground.repository.FridgeWarrantyBlockingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
public class FridgeWarrantyService {

    private final FridgeWarrantyBlockingRepository repository;
    private final Scheduler repositoryScheduler = Schedulers.boundedElastic();


    @Autowired
    public FridgeWarrantyService(final FridgeWarrantyBlockingRepository repository) {
        this.repository = repository;
    }

    public Mono<Integer> getRemainingWarrantyDays(final int fridgeId) {
        return Mono.just(repository.getRemainingWarrantyDays(fridgeId)).subscribeOn(repositoryScheduler);
    }
}
