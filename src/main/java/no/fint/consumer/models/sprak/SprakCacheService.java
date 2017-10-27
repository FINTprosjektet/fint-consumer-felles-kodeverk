package no.fint.consumer.models.sprak;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.cache.FintCache;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import no.fint.model.felles.kodeverk.iso.Sprak;
import no.fint.model.felles.kodeverk.iso.IsoActions;

@Slf4j
@Service
public class SprakCacheService extends CacheService<FintResource<Sprak>> {

    public static final String MODEL = Sprak.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public SprakCacheService() {
        super(MODEL, IsoActions.GET_ALL_SPRAK);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_SPRAK, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_SPRAK)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    private void populateCache(String orgId) {
		log.info("Populating Sprak cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, IsoActions.GET_ALL_SPRAK, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }

    public Optional<FintResource<Sprak>> getSprak(String orgId, String SystemId) {
        return getOne(orgId, (fintResource) -> fintResource.getResource().getSystemId().getIdentifikatorverdi().equals(SystemId));
    }

	@Override
    public void onAction(Event event) {
        update(event, new TypeReference<List<FintResource<Sprak>>>() {
        });
    }
}
