package com.rocketmail.vaishnavanil.towns.MapGUI.ItemName;

public class NormalHighLight implements NS {
    @Override
    public String getModifications(String input) {
        return NameStyle.HIGHLIGHT.use(NameStyle.NORMALIZED.use(input));
    }
}
