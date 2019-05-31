package com.rocketmail.vaishnavanil.towns.MapGUI;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FlagShow {
    get;

    public Inventory create(Player sndr, Claim claim) {
        Inventory flaginv = Bukkit.createInventory(null, 9 * 1, "Flags");
        List<String> lore = new ArrayList<>();
        lore.add("Ownership ->>");
        if (claim.getOwner() == sndr) lore.add("Your Claim!");

        else lore.add("Claim Owner ->> " + claim.getOwner().getName());


        ItemStack Detection = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setNameStyle(NameStyle.HIGHLIGHT)
                .setLoreStyle(LoreStyle.DETECTION)
                .setDisplayName("Flag List")
                .setLore(lore)
                .pack();

        flaginv.setItem(0, Detection);

        List<Flag> e = claim.getFlags();
        int curE = 1;
        for (Flag f : Flag.values()) {
            ItemStack i = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNameStyle(NameStyle.NORMALIZED_HIGHLIGHT).setLoreStyle(LoreStyle.DISABLE_ENABLE).setDisplayName(f.getName()).setLore(Arrays.asList(String.valueOf(e.contains(f)))).pack();

            if (e.contains(f)) i.setType(Material.LIME_STAINED_GLASS_PANE);

            flaginv.setItem(curE++, i);

        }
        return flaginv;
    }
}
