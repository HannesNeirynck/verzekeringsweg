package com.aginsurance.verzekeringsweg.persistance.entities;

import com.aginsurance.verzekeringsweg.persistance.DTOs.LengteMetKleur;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spel {

    private int aantalSpelers;
    private List<Speler> spelers = new ArrayList<>();
    private Map<Domein, LengteMetKleur> domeinMetLengte;
    private int lengte;


    public Spel(Map<Domein, LengteMetKleur> domeinMetLengte, int aantalSpelers) {

        this.aantalSpelers = aantalSpelers;
        this.domeinMetLengte = domeinMetLengte;
        lengte = domeinMetLengte.values().stream().mapToInt(value -> value.getLengte()).sum() + domeinMetLengte.size() + 1;
    }

    public Spel(Map<Domein, LengteMetKleur> domeinMetLengte) {

        aantalSpelers = 10;
        this.domeinMetLengte = domeinMetLengte;
        lengte = domeinMetLengte.values().stream().mapToInt(value -> value.getLengte()).sum() + domeinMetLengte.size() + 1;
    }

    public int getAantalSpelers() {
        return aantalSpelers;
    }

    public void setAantalSpelers(int aantalSpelers) {
        this.aantalSpelers = aantalSpelers;
    }

    public void nieuweSpeler(Speler speler){
        spelers.add(speler);
    }

    public List<Speler> getSpelers() {
        return spelers;
    }

    public Div bordMaken() {
        Div ret = new Div();
        ret.setWidthFull();
        ret.setHeight(100, Unit.PERCENTAGE);
        ret.getStyle().set("display", "grid");
        ret.getStyle().set("grid-template-columns", stringVoorColumns());
        Paragraph startP = new Paragraph("START");
        startP.getStyle().set("margin","0");
        Div start = new Div(startP);
        start.getStyle().set("background","#50c878");
        ret.add(start);
        domeinMetLengte.forEach((domein, lengteMetKleur) -> {
            for (int i = 0; i < lengteMetKleur.getLengte(); i++) {
                Paragraph p = new Paragraph(domein.getNaam());
                p.getStyle().set("margin","0");
                Div inhoud = new Div(p);
                inhoud.getStyle().set("background",lengteMetKleur.getKleur()).set("border","solid 0.1vw black").set("margin","0.3vw").set("min-height", "9vw");
                ret.add(inhoud);
            }
            Paragraph stopP = new Paragraph("STOP: " + domein.getNaam());
            Div stop = new Div(stopP);
            stop.getStyle().set("background", "#FF2400");
            ret.add(stop);
        });

        return ret;
    }


    private  String stringVoorColumns(){

        int x = (int) Math.round(Math.ceil(Math.sqrt(lengte)));
        int i = (x<= 9? x:9);
        String ret = "";
        for (int y = 0; y < i; y++
             ) {
            ret += "10vw ";
        }
        return ret;
    }


}
