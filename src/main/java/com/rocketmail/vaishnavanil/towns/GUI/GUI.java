package com.rocketmail.vaishnavanil.towns.GUI;

import com.rocketmail.vaishnavanil.towns.GUI.SI.DeleteTownConfirm;
import com.rocketmail.vaishnavanil.towns.GUI.SI.SimpleInterface;
import com.rocketmail.vaishnavanil.towns.GUI.SI.WarpsGUI;
import com.rocketmail.vaishnavanil.towns.GUI.SI.YNtest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.Inventory;

public enum GUI {
    YN(new YNtest("test")),
    DELETETown(new DeleteTownConfirm("Confirm Town Delete?"));
    SimpleInterface i;
    GUI(SimpleInterface simpleInterface){
        i = simpleInterface;
    }
    public Inventory get(){
        return i.get();
    }
}