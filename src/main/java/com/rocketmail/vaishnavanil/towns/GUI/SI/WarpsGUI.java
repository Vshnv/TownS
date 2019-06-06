package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class WarpsGUI extends SimpleInterface {

    public WarpsGUI(String n) {
        super(n);
        ItemStack yes = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("Yes!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"Yes\"").pack();
        ItemStack no = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("No!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"No\"").pack();
        Function fYes = new Function() {
            @Override
            public void run(HashMap<String, String> INPUT) {
                UUID id = UUID.fromString(INPUT.get("Player"));
                Bukkit.getPlayer(id).sendMessage("Works");
            }
        };
        Function fNo = new Function() {
            @Override
            public void run(HashMap<String, String> INPUT) {
                UUID id = UUID.fromString(INPUT.get("Player"));
                Bukkit.getPlayer(id).sendMessage("Works too");
            }

        };
        StackFunc fY = new StackFunc(yes,fYes);
        StackFunc fN = new StackFunc(no,fNo);
        super.inv = new StackFunc[] {fY,fY,fY,fY,fN,fN,fN,fN,fN};


        super.init();
    }

}
