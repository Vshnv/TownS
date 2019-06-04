package com.rocketmail.vaishnavanil.towns.GUI;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class YesNoInterface implements SimpleInterface {
    @Override
    public Inventory get(int size, String name) {

        Inventory Interface = Bukkit.createInventory(null,9,name);


        if(size%2 == 0){
            size+=1;

        }
        ItemStack center = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName(name).setLoreStyle(LoreStyle.DETECTION).addLoreLine("Yes or No?").pack();
        int c = (size/2) + (size%2);
        Interface.setItem(c,center);
        ItemStack yes = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("Yes!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"Yes\"").pack();
        ItemStack no = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("No!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"No\"").pack();

        for(int i = 0;i<=size-1;i++){
            if(i == c)continue;
            Interface.setItem(i,i<c ? yes:no);
        }


        return Interface;
    }
}
