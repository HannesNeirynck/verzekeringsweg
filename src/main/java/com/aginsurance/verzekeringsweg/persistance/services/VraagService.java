package com.aginsurance.verzekeringsweg.persistance.services;

import com.aginsurance.verzekeringsweg.Util.Checker;
import com.aginsurance.verzekeringsweg.persistance.DTOs.VraagDTO;
import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import com.aginsurance.verzekeringsweg.persistance.repos.VraagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
@Transactional
public class VraagService {

    private final VraagRepository vraagRepository;

    @Autowired
    public VraagService(VraagRepository vraagRepository) {
        this.vraagRepository = vraagRepository;
    }

    public Vraag addVraag(Vraag vraag){
        return vraagRepository.save(vraag);
    }

    public List<Vraag> alleVragen(){
        return vraagRepository.findAll();
    }

    public void verwijderAlles(){
        vraagRepository.findAll().forEach(vraag -> {
            vraag.setHoofdDomeinen(null);
            vraag.setSubDomeinen(null);
            vraagRepository.save(vraag);
            vraagRepository.deleteById(vraag.getId());
        });
    }

    public ArrayList<VraagDTO> alleVragenExcel(){
        ArrayList<VraagDTO> ret = new ArrayList<>();
        for (Vraag vraag: vraagRepository.findAll()
             ) {
            ret.add(new VraagDTO.Builder()
                    .id(vraag.getId())
                    .vraag(vraag.getVraag())
                    .juistAntwoord(vraag.getJuistAntwoord())
                    .fout1(vraag.getFout1())
                    .fout2(vraag.getFout2())
                    .hoofdDomeinen(getHoofd(vraag.getHoofdDomeinen()))
                    .subDomeinen(getSub(vraag.getSubDomeinen()))
                    .build());
        }
        return ret;
    }



    private static String getHoofd(List<HoofdDomein> hoofden){
        StringJoiner ret = new StringJoiner(",");
        if (Checker.ListChecker(hoofden)){
            for (HoofdDomein hoofd: hoofden
                 ) {
                ret.add(hoofd.getNaam());
            }
        }
        return ret.toString();
    }

    private static String getSub(List<SubDomein> subs){
        StringJoiner ret = new StringJoiner(",");
        if (Checker.ListChecker(subs)){
            for (SubDomein sub: subs
            ) {
                ret.add(sub.getHoofdDomein().getNaam() + "." + sub.getNaam());
            }
        }
        return ret.toString();
    }

}
