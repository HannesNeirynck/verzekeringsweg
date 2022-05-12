package com.aginsurance.verzekeringsweg.Util.runners;

import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import com.aginsurance.verzekeringsweg.persistance.services.HoofdDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.SubDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.VraagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VraagService vraagService;
    private final SubDomeinService subDomeinService;
    private final HoofdDomeinService hoofdDomeinService;

    @Autowired
    public InitRunner(VraagService vraagService, SubDomeinService subDomeinService, HoofdDomeinService hoofdDomeinService) {
        this.vraagService = vraagService;
        this.subDomeinService = subDomeinService;
        this.hoofdDomeinService = hoofdDomeinService;
    }


    @Override
    public void run(String... args) throws Exception {

        //logger.warn("This runner is useless right now, just nice to have if I need a runner or a trackstar");
        Vraag vraag = new Vraag.Builder()
                .vraag("Wie heeft deze vragen gemaakt")
                .fout1("Jos")
                .fout2("Eddy")
                .juistAntwoord("Hannes")
                .build();
        vraag = vraagService.addVraag(vraag);
        Vraag vraag2 = new Vraag.Builder()
                .vraag("Wie is de natuurlijke zoon van de god Odin")
                .fout1("Loki")
                .fout2("Akshan")
                .juistAntwoord("Thor")
                .build();
        vraag2 = vraagService.addVraag(vraag2);

        Vraag vraag3 = new Vraag.Builder()
                .vraag("Welk zoogdier maakt geen melk aan")
                .fout1("Kat")
                .fout2("Mens")
                .juistAntwoord("Vogelbekdier")
                .build();
        vraag3 = vraagService.addVraag(vraag3);
        Vraag vraag4 = new Vraag.Builder()
                .vraag("Wat is het antwoord op de ultieme vraag over het Leven, het Universum en Alles")
                .fout1("Liefde")
                .fout2("De mens")
                .juistAntwoord("42")
                .build();
        vraag4 = vraagService.addVraag(vraag4);

        Vraag vraag5 = new Vraag.Builder()
                .vraag("Wat is Daisy Johnson haar superhelden naam")
                .fout1("Tremmors")
                .fout2("Skye")
                .juistAntwoord("Quake")
                .build();
        vraag5 = vraagService.addVraag(vraag5);



        SubDomein sub = new SubDomein.Builder().naam("Test subdomein").build();
        sub = subDomeinService.voegSubDomeinToe(sub);
        sub = subDomeinService.voegVraagToe(sub,vraag);
        sub = subDomeinService.voegVraagToe(sub,vraag2);

        SubDomein sub2 = new SubDomein.Builder().naam("Test subdomein 2").build();
        sub2 = subDomeinService.voegSubDomeinToe(sub2);
        sub2 = subDomeinService.voegVraagToe(sub2,vraag3);
        sub2 = subDomeinService.voegVraagToe(sub2,vraag4);

        HoofdDomein hoofd = new HoofdDomein.Builder().naam("Test hoofddomein").build();
        hoofd = hoofdDomeinService.voegHoofdDomeinToe(hoofd);
        hoofd = hoofdDomeinService.voegSubToe(hoofd,sub);
        hoofd = hoofdDomeinService.voegSubToe(hoofd,sub2);
        hoofd = hoofdDomeinService.voegVraagToe(hoofd,vraag5);

        Vraag vraag6 = new Vraag.Builder()
                .vraag("Wie is beter bekend als 'The queen of pop punk'")
                .fout1("Jessica Alba")
                .fout2("Halsey")
                .juistAntwoord("Avril Lavigne")
                .build();
        vraag6 = vraagService.addVraag(vraag6);

        Vraag vraag7 = new Vraag.Builder()
                .vraag("Waar werd Tupac vermoord")
                .fout1("In zijn huis")
                .fout2("In Compton")
                .juistAntwoord("In Las Vegas")
                .build();
        vraag7 = vraagService.addVraag(vraag7);

        SubDomein sub3 = new SubDomein.Builder().naam("Punk").build();
        sub3 = subDomeinService.voegSubDomeinToe(sub3);
        sub3 = subDomeinService.voegVraagToe(sub3,vraag6);

        HoofdDomein hoofd2 = new HoofdDomein.Builder().naam("Muziek").build();
        hoofd2 = hoofdDomeinService.voegHoofdDomeinToe(hoofd2);
        hoofd2 = hoofdDomeinService.voegSubToe(hoofd2 , sub3);
        hoofd2 = hoofdDomeinService.voegVraagToe(hoofd2, vraag7);

    }
}
