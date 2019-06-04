package com.rocketmail.vaishnavanil.towns.GUI;

import com.rocketmail.vaishnavanil.towns.GUI.SI.SimpleInterface;
import com.rocketmail.vaishnavanil.towns.GUI.SI.YNtest;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.Inventory;

public enum GUI {
    YN(new YNtest("test"));
    SimpleInterface i;
    GUI(SimpleInterface simpleInterface){
        i = simpleInterface;
    }
    public Inventory get(){
        return i.get();
    }
}