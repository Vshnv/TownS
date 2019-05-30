package com.rocketmail.vaishnavanil.towns.MapGUI.ItemName;

import java.util.List;

public enum NameStyle {

    NO_STYLE(new NoStyle()),
    HIGHLIGHT(new HighLight()),
    DESIGNED(new Designed()),
    NORMALIZED_HIGHLIGHT(new NormalHighLight()),
    NORMALIZED(new Normalized());
    ;
    private NS style;
    NameStyle(NS s){
        this.style = s;
    }

    public String use(String input){
        return this.style.getModifications(input);
    }


}
