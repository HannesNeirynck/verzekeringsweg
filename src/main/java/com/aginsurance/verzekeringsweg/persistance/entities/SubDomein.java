package com.aginsurance.verzekeringsweg.persistance.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class SubDomein implements Domein {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String naam;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Vraag> vragen = new ArrayList<Vraag>();
    @ManyToOne(cascade = CascadeType.PERSIST)
    private HoofdDomein hoofdDomein;

    public SubDomein() {
    }

    private SubDomein(Builder builder) {
        setId(builder.id);
        setNaam(builder.naam);
        setVragen(builder.vragen);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<Vraag> getVragen() {
        return vragen;
    }

    public HoofdDomein getHoofdDomein() {
        return hoofdDomein;
    }

    public void setHoofdDomein(HoofdDomein hoofdDomein) {
        this.hoofdDomein = hoofdDomein;
    }

    public void setVragen(List<Vraag> vragen) {
        this.vragen = vragen;
    }

    public void voegVraagToe(Vraag vraag){
        if (this.vragen == null) this.vragen = new ArrayList<Vraag>();
        vragen.add(vraag);
    }

    @Override
    public Vraag vraagStellen() {

        return vragen.remove(ThreadLocalRandom.current().nextInt(0,vragen.size()));
    }


    public static final class Builder {
        private Long id;
        private String naam;
        private List<Vraag> vragen;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder naam(String val) {
            naam = val;
            return this;
        }

        public Builder vragen(List<Vraag> val) {
            vragen = val;
            return this;
        }

        public SubDomein build() {
            return new SubDomein(this);
        }
    }
}
