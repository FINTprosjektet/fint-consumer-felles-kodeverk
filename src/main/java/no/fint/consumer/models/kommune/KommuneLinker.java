package no.fint.consumer.models.kommune;

import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.KommuneResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class KommuneLinker extends FintLinker<KommuneResource> {

    public KommuneLinker() {
        super(KommuneResource.class);
    }

    public void mapLinks(KommuneResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public KommuneResources toResources(Collection<KommuneResource> collection) {
        KommuneResources resources = new KommuneResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(KommuneResource kommune) {
        if (!isNull(kommune.getSystemId()) && !isEmpty(kommune.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(kommune.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}
