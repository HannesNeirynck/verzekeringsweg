package com.aginsurance.verzekeringsweg.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("")
@PageTitle("Verzekeringsweg")
@RouteAlias("Main")
public class MainView extends VerticalLayout {

    private H1 h1;
    private Button start;
    private Button vragen;


    public MainView(){

        h1 = new H1("Verzekeringsweg");
        start = new Button("Spel starten");
        vragen = new Button("Vragenbeheer");

        start.addClickListener(buttonClickEvent -> {
            start.getUI().ifPresent(ui -> ui.navigate("aanmaken"));
        });
        vragen.addClickListener(buttonClickEvent -> {
            vragen.getUI().ifPresent(ui -> ui.navigate("vragen"));
        });

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.EVENLY);
        this.setHeightFull();
        this.add(h1,start,vragen);
    }
}
