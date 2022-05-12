package com.aginsurance.verzekeringsweg.views;

import com.aginsurance.verzekeringsweg.Util.ColorPicker;
import com.aginsurance.verzekeringsweg.persistance.DTOs.LengteMetKleur;
import com.aginsurance.verzekeringsweg.persistance.entities.Domein;
import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Spel;
import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import com.aginsurance.verzekeringsweg.persistance.services.HoofdDomeinService;
import com.aginsurance.verzekeringsweg.persistance.services.SubDomeinService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

//TODO check met localisatie hoe
@Route("aanmaken")
@PageTitle("Spel aanmaken")
@RouteAlias("créer")
public class AanmaakView extends VerticalLayout implements AfterNavigationObserver {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HoofdDomeinService hoofdDomeinService;
    private final SubDomeinService subDomeinService;


    private IntegerField spelersAantal;
    private VerticalLayout meerdereDomeinen;

    private Button domeinToevoegen;
    private RadioButtonGroup<String> radioGroep;
    private Checkbox checkbox;
    private Button aanMaken;

    public AanmaakView(HoofdDomeinService hoofdDomeinService, SubDomeinService subDomeinService) {
        this.hoofdDomeinService = hoofdDomeinService;
        this.subDomeinService = subDomeinService;
        spelersAantal = new IntegerField();
        spelersAantal.setMin(1);
        spelersAantal.setValue(3);
        spelersAantal.setLabel("Aantal spelers");
        spelersAantal.setHasControls(true);
        meerdereDomeinen = new VerticalLayout();
        meerdereDomeinen.setAlignItems(Alignment.CENTER);
        meerdereDomeinen.setJustifyContentMode(JustifyContentMode.EVENLY);

        domeinToevoegen = new Button("Domein toevoegen");
        domeinToevoegen.addClickListener(buttonClickEvent -> {meerdereDomeinen.add(domeinAanmaak());});
        checkbox = new Checkbox("Ik wil eerst nog even de vragen controleren");
        radioGroep = new RadioButtonGroup<>();
        radioGroep.setItems("Speciale vragen", "Vasthouden tot juist");
        radioGroep.setLabel("Bij STOP");
        checkbox = new Checkbox("Ik wil eerst nog even de vragen controleren");
        aanMaken = new Button("Maak je spel!");
        aanMaken.addClickListener(event -> {

            ComponentUtil.setData(UI.getCurrent(),Spel.class,spelAanmaken());
            aanMaken.getUI().ifPresent(ui -> ui.navigate("spel"));
        });



        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.EVENLY);
        this.setHeightFull();
        this.add(spelersAantal, meerdereDomeinen,domeinToevoegen, radioGroep, checkbox,aanMaken);
    }

    private HorizontalLayout domeinAanmaak(){
        HorizontalLayout hl = new HorizontalLayout();

        VerticalLayout domein = new VerticalLayout();
        VerticalLayout lengte = new VerticalLayout();
        Select<HoofdDomein> domeinKiezen = new Select<>();
        Select<SubDomein> subDomeinKiezen = new Select<>();
        domeinKiezen.setLabel("Domein");
        //TODO DB IMPLEMENTATIE
        domeinKiezen.setItems(hoofdDomeinService.alles());
        domeinKiezen.setItemLabelGenerator(HoofdDomein::getNaam);
        subDomeinKiezen.setItemLabelGenerator(SubDomein::getNaam);
        domeinKiezen.addValueChangeListener(event-> {
            subDomeinKiezen.setItems(event.getValue().getSubdomeinen());
            subDomeinKiezen.add(new Text("Laat leeg voor alle vragen"));
        });
        domeinKiezen.setPlaceholder("Kies een domein.");
        subDomeinKiezen.setLabel("Optioneel: Subdomein");
        IntegerField lengteAantal = new IntegerField();
        lengteAantal.setMin(1);
        lengteAantal.setValue(5);
        lengteAantal.setLabel("Lengte");
        lengteAantal.setHasControls(true);

        //TODO Label :c
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLabel("Kies een kleur.");
        colorPicker.setId("15");
        Label kleurLabel = new Label("Kies een kleur.");
        kleurLabel.setFor("15");

        hl.add(domeinKiezen,subDomeinKiezen,lengteAantal,kleurLabel,colorPicker);
        hl.setAlignItems(Alignment.BASELINE);

        return hl;
    }

    public Spel spelAanmaken(){
        Map<Domein, LengteMetKleur> domeinLengteKleur = new HashMap<>();
        meerdereDomeinen.getChildren().forEach(component -> {


            HorizontalLayout hl = (HorizontalLayout) component;
            //Domein
            Domein domein = (Domein) ((Select)hl.getComponentAt(1)).getValue(); //Dit blijft zo als het niet null is


            if (domein == null){ //Als Sub leeg is.
                domein = (Domein) ((Select)hl.getComponentAt(0)).getValue();
            }

            domeinLengteKleur.put(domein, //Domein
                    new LengteMetKleur(((IntegerField)hl.getComponentAt(2)).getValue() //Lengte
                    ,((ColorPicker)hl.getComponentAt(4)).getValue())); //Kleur
        });
        return new Spel(domeinLengteKleur,spelersAantal.getValue());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        meerdereDomeinen.add(domeinAanmaak());
    }
}
