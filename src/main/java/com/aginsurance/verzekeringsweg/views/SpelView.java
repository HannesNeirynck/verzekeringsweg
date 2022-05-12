package com.aginsurance.verzekeringsweg.views;

import com.aginsurance.verzekeringsweg.Util.ColorPicker;
import com.aginsurance.verzekeringsweg.persistance.DTOs.LengteMetKleur;
import com.aginsurance.verzekeringsweg.persistance.entities.Domein;
import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import com.aginsurance.verzekeringsweg.persistance.entities.Spel;
import com.aginsurance.verzekeringsweg.persistance.entities.Speler;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.ui.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Push(transport = Transport.LONG_POLLING)
@Route("spel")
@RouteAlias("Jeu")
public class SpelView extends VerticalLayout {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private H1 h1;

    private HorizontalLayout testSuite;
    private Button speler;
    public Image dice;

    private Div div;

    private Dialog dialog;


    public SpelView() {
        Map<Domein, LengteMetKleur> domeinen = new HashMap<>();
        domeinen.put(new HoofdDomein.Builder().naam("Test1").build(),new LengteMetKleur(5,"#FF0000"));
        domeinen.put(new HoofdDomein.Builder().naam("Test2").build(),new LengteMetKleur(3,"#00FF00"));
        domeinen.put(new HoofdDomein.Builder().naam("Test3").build(),new LengteMetKleur(9,"#0000FF"));

        Spel init = ComponentUtil.getData(UI.getCurrent(),Spel.class);
        Spel spel = (init == null? new Spel(domeinen):init);
        final int[] x = {spel.getAantalSpelers()};


        h1 = new H1("SPEL");
        div = spel.bordMaken(); //new Div(Spel.bordMaken());
        div.setHeightFull();
        div.setWidthFull();

        this.setWidthFull();
        this.setHeightFull();

        //Dialoog voor speler
        dialog = new Dialog();

        VerticalLayout vl = new VerticalLayout();
        Text text = new Text(String.format("We wachten nog op %s spelers", x[0]));
        TextField naam = new TextField();
        naam.setLabel("Naam");
        Label kleur = new Label("Kleur");
        ColorPicker kleurKies = new ColorPicker();
        kleur.add(kleurKies);
        Button save = new Button("Save");
        save.addClickListener(e -> {
            logger.info(naam.getValue() + " kleur: " + kleurKies.getValue());
            spel.nieuweSpeler(new Speler(naam.getValue(),kleurKies.getValue()));
            x[0]--;
            if (x[0] > 0) text.setText(String.format("We wachten nog op %s spelers", x[0]));
            else {
                dialog.close();
                spel.getSpelers().forEach(speler1 ->{
                    Div div1 = (Div) div.getComponentAt(0);
                    Icon icon = new Icon(VaadinIcon.USER);
                    icon.setColor(speler1.getKleur());
                    div1.add(icon);
                });
            }
        });
        vl.add(text,naam,kleur,save);
        dialog.add(vl);
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        dialog.open();






        speler = new Button("Test speler plaatsen");
        speler.addClickListener(e -> {
            ((Div)div.getComponentAt(0)).add(new Icon(VaadinIcon.MAILBOX));
        });

        StreamResource d1 = new StreamResource("Dice1.png",
                () -> getClass().getResourceAsStream("/images/Dice1.png"));

        StreamResource rollingResource = new StreamResource("rollingDice.gif",
                () ->getClass().getResourceAsStream("/images/rollingDice.gif"));
        dice = new Image(d1,"Dobbelsteen");
        dice.setMaxHeight(5, Unit.VW);
        dice.setMaxWidth(5,Unit.VW);

        dice.addClickListener(e->{
            UI ui = UI.getCurrent();
            dice.setSrc(rollingResource);
            ui.getChildren().forEach(component -> logger.info(component.toString()));

            new Thread(() -> {
                try {
                    logger.info("clicked");
                    ui.access(() -> this.add(new Text("Test")));
                    ui.close();
                    ui.push();
                    //ui.//dice.setSrc(d1)
                    Thread.sleep(5000);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }).start();

        });



        testSuite = new HorizontalLayout(speler, dice);
        this.add(h1, dice,testSuite,div);



        }
    }

