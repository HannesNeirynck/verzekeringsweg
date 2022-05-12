package com.aginsurance.verzekeringsweg.persistance.entities;

import com.aginsurance.verzekeringsweg.Util.Checker;

import java.util.Map;

public class Speler {

    private String naam, kleur;
    private int vragen;
    private Map<Vraag,String> fouteAntwoorden;
    private Boolean overslaan;
    private int positie = 0;

    public Speler(String naam, String kleur) {
        this.naam = naam;
        this.kleur = kleur;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        if (Checker.String(naam))this.naam = naam;
    }

    public String getKleur() {
        return kleur;
    }

    public void setKleur(String kleur) {
        this.kleur = kleur;
    }

    public int getVragen() {
        return vragen;
    }

    public void setVragen(int vragen) {
        this.vragen = vragen;
    }

    public Map<Vraag, String> getFouteAntwoorden() {
        return fouteAntwoorden;
    }

    public void setFouteAntwoorden(Map<Vraag, String> fouteAntwoorden) {
        this.fouteAntwoorden = fouteAntwoorden;
    }

    public void addFoutAntwoord(Vraag vraag,String antwoord){
        fouteAntwoorden.put(vraag,antwoord);
    }

    public int verschuif(int i){
        positie += i;
        return positie;
    }

    @Override
    public String toString() {
        return "Speler{" +
                "naam='" + naam + '\'' +
                ", kleur='" + kleur + '\'' +
                '}';
    }
}
