package com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore;

import java.util.List;

public enum LoreStyle {
    NO_STYLE(new NoStyle()),
    DISABLE_ENABLE(new DE()),
    INFO(new Info()),
    DETECTION(new Detection());

    private LS style;
    LoreStyle(LS s){
        this.style = s;
    }

    public List<String> use(List<String> input){
        return this.style.getModifications(input);
    }
}
