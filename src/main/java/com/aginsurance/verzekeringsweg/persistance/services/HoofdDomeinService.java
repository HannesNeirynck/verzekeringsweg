package com.aginsurance.verzekeringsweg.persistance.services;

import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import com.aginsurance.verzekeringsweg.persistance.repos.DomeinRepository;
import com.aginsurance.verzekeringsweg.persistance.repos.SubdomeinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HoofdDomeinService {

    private final DomeinRepository domeinRepository;
    private final SubdomeinRepository subdomeinRepository;

    @Autowired
    public HoofdDomeinService(DomeinRepository domeinRepository, SubdomeinRepository subdomeinRepository) {
        this.domeinRepository = domeinRepository;
        this.subdomeinRepository = subdomeinRepository;
    }

    public HoofdDomein voegVraagToe(HoofdDomein hoofdDomein, Vraag vraag){
        hoofdDomein.voegVraagToe(vraag);
        return domeinRepository.save(hoofdDomein);
    }

    public HoofdDomein voegHoofdDomeinToe(HoofdDomein domein){
        return domeinRepository.save(domein);
    }

    public HoofdDomein voegSubToe(HoofdDomein hoofdDomein, SubDomein subDomein){
        subDomein.setHoofdDomein(hoofdDomein);
        subdomeinRepository.save(subDomein);
        hoofdDomein.voegSubDomeinToe(subDomein);
        return domeinRepository.save(hoofdDomein);
    }

    public Optional<HoofdDomein> vindOpNaam(String naam){
        return domeinRepository.findByNaam(naam);
    }

    public void verwijderAlles(){
        domeinRepository.findAll().forEach(hoofdDomein -> {
            hoofdDomein.setSubdomeinen(null);
            hoofdDomein.setVragen(null);
            domeinRepository.save(hoofdDomein);
            domeinRepository.deleteById(hoofdDomein.getId());
        });
    }

    public List<HoofdDomein> alles(){
        return domeinRepository.findAll();
    }

}
