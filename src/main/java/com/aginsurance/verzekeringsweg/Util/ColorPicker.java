package com.aginsurance.verzekeringsweg.Util;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;

@Tag("input")
public class ColorPicker extends AbstractSinglePropertyField<ColorPicker,String> {
    public ColorPicker() {
        super("value","",false);
        getElement().setAttribute("type","color");
        setSynchronizedEvent("change");
    }

    public void setLabel(String label) {
        this.getElement().setProperty("label", label == null ? "" : label);
    }

    public String getValue(){
        String ret = super.getValue();
        return (!Checker.String(ret)? "#000000":ret);
    };
}
