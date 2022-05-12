package com.aginsurance.verzekeringsweg.persistance.DTOs;

public class LengteMetKleur {

    private int lengte;
    private String kleur;

    public LengteMetKleur(int lengte, String kleur) {
        this.lengte = lengte;
        this.kleur = kleur;
    }

    public int getLengte() {
        return lengte;
    }

    public String getKleur() {
        return kleur;
    }
}
