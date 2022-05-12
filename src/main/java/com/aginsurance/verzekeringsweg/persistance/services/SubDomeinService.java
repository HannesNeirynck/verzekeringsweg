package com.aginsurance.verzekeringsweg.persistance.services;

import com.aginsurance.verzekeringsweg.Util.Checker;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import com.aginsurance.verzekeringsweg.persistance.repos.SubdomeinRepository;
import com.aginsurance.verzekeringsweg.persistance.repos.VraagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SubDomeinService {

    private final SubdomeinRepository subdomeinRepository;
    private final VraagRepository vraagRepository;

    @Autowired
    public SubDomeinService(SubdomeinRepository subdomeinRepository, VraagRepository vraagRepository) {
        this.subdomeinRepository = subdomeinRepository;
        this.vraagRepository = vraagRepository;
    }

    public SubDomein voegVraagToe(SubDomein subDomein, Vraag vraag){
        if(!Checker.NullCheck(subDomein)) throw new IllegalArgumentException("subdomein leeg");
        if (!Checker.NullCheck(vraag)) throw new IllegalArgumentException("vraag leeg");
        subDomein.voegVraagToe(vraag);
        return subdomeinRepository.save(subDomein);
    }

    public SubDomein voegSubDomeinToe(SubDomein subDomein){
        return subdomeinRepository.save(subDomein);
    }

    public Optional<SubDomein> vindOpNaam(String string) {
        return subdomeinRepository.findByNaam(string);
    }

    public void verwijderAlles(){
        subdomeinRepository.findAll().forEach(subDomein -> {
            subDomein.setHoofdDomein(null);
            subDomein.setVragen(null);
            subdomeinRepository.save(subDomein);
            subdomeinRepository.deleteById(subDomein.getId());
        });
    }
}
