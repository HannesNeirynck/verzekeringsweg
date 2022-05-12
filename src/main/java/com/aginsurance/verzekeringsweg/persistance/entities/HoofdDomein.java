package com.aginsurance.verzekeringsweg.persistance.entities;

import com.aginsurance.verzekeringsweg.Util.Checker;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class HoofdDomein implements Domein {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String naam;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Vraag> vragen = new ArrayList<Vraag>();
    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<SubDomein> subdomeinen = new ArrayList<SubDomein>();

    public HoofdDomein() {
        this.vragen = new ArrayList<Vraag>();
        this.subdomeinen = new ArrayList<SubDomein>();
    }

    private HoofdDomein(Builder builder) {
        setId(builder.id);
        setNaam(builder.naam);
        setVragen(builder.vragen);
        setSubdomeinen(builder.subdomeinen);
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

    public void setVragen(List<Vraag> vragen) {
        this.vragen = vragen;
    }

    public List<SubDomein> getSubdomeinen() {
        return subdomeinen;
    }

    public void setSubdomeinen(List<SubDomein> subdomeinen) {
        this.subdomeinen = subdomeinen;
    }

    public Vraag vraagVraag(){
        return vragen.remove(ThreadLocalRandom.current().nextInt(0,vragen.size()));
    }

    public void voegVraagToe(Vraag vraag) {
        if (this.vragen == null) this.vragen = new ArrayList<Vraag>();
        vragen.add(vraag);
    }

    public void voegSubDomeinToe(SubDomein subDomein) {
        if (!Checker.NullCheck(subdomeinen)) {
            subdomeinen = new ArrayList<SubDomein>();
            logger.info("arraylist aangemaakt");
        }
        subdomeinen.add(subDomein);
    }

    public List<Vraag> alleVragenInDomein(){
        List<Vraag> ret =  getVragen();
        for (SubDomein sub: subdomeinen
             ) {
            ret.addAll(sub.getVragen());
        }
        return ret;
    }

    @Override
    public Vraag vraagStellen() {
        return null;
    }


    public static final class Builder {
        private Long id;
        private String naam;
        private List<Vraag> vragen;
        private List<SubDomein> subdomeinen;

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

        public Builder subdomeinen(List<SubDomein> val) {
            subdomeinen = val;
            return this;
        }

        public HoofdDomein build() {
            return new HoofdDomein(this);
        }
    }
}
