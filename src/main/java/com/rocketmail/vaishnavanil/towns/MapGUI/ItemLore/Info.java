package com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore;

import java.util.ArrayList;
import java.util.List;

public class Info implements LS {
    @Override
    public List<String> getModifications(List<String> msg) {
        List<String> lore = new ArrayList<>();
        lore.add(c("&7============"));
        for(String l:msg){
            lore.add(c("&f"+l));
        }
        lore.add(c("&7============"));
        return lore;
    }
}
