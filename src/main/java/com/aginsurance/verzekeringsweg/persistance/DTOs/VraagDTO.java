package com.aginsurance.verzekeringsweg.persistance.DTOs;

import com.aginsurance.verzekeringsweg.Util.Checker;
import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import com.aginsurance.verzekeringsweg.persistance.services.HoofdDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.SubDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.VraagService;

public class VraagDTO {

    private Long id;
    private String vraag;
    private String juistAntwoord;
    private String fout1;
    private String fout2;
    private String hoofdDomeinen;
    private String subDomeinen;

    public VraagDTO() {
    }

    private VraagDTO(Builder builder) {
        setId(builder.id);
        setVraag(builder.vraag);
        setJuistAntwoord(builder.juistAntwoord);
        setFout1(builder.fout1);
        setFout2(builder.fout2);
        setHoofdDomeinen(builder.hoofdDomeinen);
        setSubDomeinen(builder.subDomeinen);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVraag() {
        return vraag;
    }

    public void setVraag(String vraag) {
        this.vraag = vraag;
    }

    public String getJuistAntwoord() {
        return juistAntwoord;
    }

    public void setJuistAntwoord(String juistAntwoord) {
        this.juistAntwoord = juistAntwoord;
    }

    public String getFout1() {
        return fout1;
    }

    public void setFout1(String fout1) {
        this.fout1 = fout1;
    }

    public String getFout2() {
        return fout2;
    }

    public void setFout2(String fout2) {
        this.fout2 = fout2;
    }

    public String getHoofdDomeinen() {
        return hoofdDomeinen;
    }

    public void setHoofdDomeinen(String hoofdDomeinen) {
        this.hoofdDomeinen = hoofdDomeinen;
    }

    public String getSubDomeinen() {
        return subDomeinen;
    }

    public void setSubDomeinen(String subDomeinen) {
        this.subDomeinen = subDomeinen;
    }

    public void naarVraag(HoofdDomeinService hoofdDomeinService, SubDomeinService subDomeinService, VraagService vraagService){
      Vraag ret = new Vraag.Builder()
              .vraag(this.vraag)
              .juistAntwoord(this.juistAntwoord)
              .fout1(this.fout1)
              .fout2(this.fout2)
              .build();
      ret = vraagService.addVraag(ret);
        if (Checker.String(hoofdDomeinen)) {
            String[] hoofdDomeinenSplit = this.hoofdDomeinen.split(",");
            for (String string: hoofdDomeinenSplit) {
                Vraag finalRet = ret;
                hoofdDomeinService.vindOpNaam(string).ifPresentOrElse(hoofdDomein ->
                                hoofdDomeinService.voegVraagToe(hoofdDomein, finalRet),
                        () -> hoofdDomeinService.voegVraagToe(new HoofdDomein.Builder().naam(string).build(),finalRet));
            }
        }
        if (Checker.String(subDomeinen)){
            String[] subDomeinenSplit = this.subDomeinen.split(",");
            for (String string: subDomeinenSplit){
                String[] gesplitst = string.split("\\.");
                Vraag finalVraag1 = ret;
                subDomeinService.vindOpNaam(gesplitst[1]).ifPresentOrElse(subDomein ->
                                subDomeinService.voegVraagToe(subDomein, finalVraag1), //Sub word gevonden
                        () -> { //Sub niet gevonden
                            hoofdDomeinService.vindOpNaam(gesplitst[0]).ifPresentOrElse(hoofdDomein -> { //Hoofd gevonden, sub niet gevonden
                                        SubDomein sub = subDomeinService.voegSubDomeinToe(new SubDomein.Builder().naam(gesplitst[1]).build());
                                        sub = subDomeinService.voegVraagToe(sub,finalVraag1);
                                        hoofdDomeinService.voegSubToe(hoofdDomein,sub);
                                    },
                                    () -> {   //Hoofd niet gevonden, sub niet gevonden
                                        SubDomein sub = subDomeinService.voegSubDomeinToe(new SubDomein.Builder().naam(gesplitst[1]).build());
                                        sub = subDomeinService.voegVraagToe(sub, finalVraag1);
                                        HoofdDomein hoofd = hoofdDomeinService.voegHoofdDomeinToe(new HoofdDomein.Builder().naam(gesplitst[0]).build());
                                        hoofdDomeinService.voegSubToe(hoofd,new SubDomein.Builder().naam(gesplitst[1]).build());
                                    });
                        });
            }
        }

    }

    @Override
    public String toString() {
        return "VraagDTO{" +
                "id=" + id +
                ", vraag='" + vraag + '\'' +
                ", juistAntwoord='" + juistAntwoord + '\'' +
                ", fout1='" + fout1 + '\'' +
                ", fout2='" + fout2 + '\'' +
                ", hoofdDomeinen='" + hoofdDomeinen + '\'' +
                ", subDomeinen='" + subDomeinen + '\'' +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String vraag;
        private String juistAntwoord;
        private String fout1;
        private String fout2;
        private String hoofdDomeinen;
        private String subDomeinen;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder vraag(String val) {
            vraag = val;
            return this;
        }

        public Builder juistAntwoord(String val) {
            juistAntwoord = val;
            return this;
        }

        public Builder fout1(String val) {
            fout1 = val;
            return this;
        }

        public Builder fout2(String val) {
            fout2 = val;
            return this;
        }

        public Builder hoofdDomeinen(String val) {
            hoofdDomeinen = val;
            return this;
        }

        public Builder subDomeinen(String val) {
            subDomeinen = val;
            return this;
        }

        public VraagDTO build() {
            return new VraagDTO(this);
        }
    }
}
