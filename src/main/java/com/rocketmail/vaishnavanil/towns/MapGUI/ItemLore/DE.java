package com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore;

import java.util.ArrayList;
import java.util.List;

public class DE implements LS {
    @Override
    public List<String> getModifications(List<String> msg) {
        List<String> lore = new ArrayList<>();
        if(msg.contains("true")){
            lore.add(c("&2============"));
            lore.add(" ");
            lore.add(c("&aEnabled!"));
            lore.add(" ");
            lore.add(c("&2============"));
            return lore;
        }else if(msg.contains("false")){
            lore.add(c("&4============"));
            lore.add("   ");
            lore.add(c("&cDisabled!"));
            lore.add("   ");
            lore.add(c("&4============"));
            return lore;
        }

        return msg;
    }

}
