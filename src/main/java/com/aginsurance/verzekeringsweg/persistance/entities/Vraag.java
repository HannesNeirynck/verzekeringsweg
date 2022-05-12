package com.aginsurance.verzekeringsweg.persistance.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Vraag implements Vraagbaar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String vraag;
    @Column
    private String juistAntwoord;
    @Column
    private String fout1;
    @Column
    private String fout2;
    @ManyToMany(/*fetch = FetchType.EAGER MULTIPLE BAGS,*/mappedBy = "vragen")
    private List<HoofdDomein> hoofdDomeinen;
    @ManyToMany(/*fetch = FetchType.EAGER MULTIPLE BAGS,*/mappedBy = "vragen")
    private List<SubDomein> subDomeinen;


    public Vraag() {
    }

    private Vraag(Builder builder) {
        setId(builder.id);
        setVraag(builder.vraag);
        setJuistAntwoord(builder.juistAntwoord);
        setFout1(builder.fout1);
        setFout2(builder.fout2);
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

    public List<HoofdDomein> getHoofdDomeinen() {
        return hoofdDomeinen;
    }

    public List<SubDomein> getSubDomeinen() {
        return subDomeinen;
    }

    public void setHoofdDomeinen(List<HoofdDomein> hoofdDomeinen) {
        this.hoofdDomeinen = hoofdDomeinen;
    }

    public void setSubDomeinen(List<SubDomein> subDomeinen) {
        this.subDomeinen = subDomeinen;
    }

    @Override
    public String toString() {
        return "Vraag " + id +
                " \nvraag: '" + vraag + '\'' +
                " \njuist antwoord: '" + juistAntwoord + '\'' +
                " \nfoutieve antwoorden:\t'" + fout1 + '\'' +
                " \n\t\t\t\t\t'" + fout2 + '\'';
    }

    public static final class Builder {
        private Long id;
        private String vraag;
        private String juistAntwoord;
        private String fout1;
        private String fout2;

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



        public Vraag build() {
            return new Vraag(this);
        }
    }
}
