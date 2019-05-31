package com.rocketmail.vaishnavanil.towns.Listeners;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum Constraints {
    Container(Arrays.asList("DEFAULT")),
    Usable(Arrays.asList("DEFAULT")),
    DONT_USE(Arrays.asList("DEFAULT"));
    private List<String> eW;
    Constraints(List<String> endWith){
        eW = endWith;
    }

    public boolean isRestricted(Material material){
        for(String s:eW){
            if(material.toString().toLowerCase().endsWith(s.toLowerCase()))return true;
        }
        return false;
    }


    public void setRestrictions(List<String> restrictions){
        this.eW = restrictions;
    }

}
