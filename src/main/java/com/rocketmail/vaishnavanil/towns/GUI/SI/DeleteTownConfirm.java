package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class DeleteTownConfirm extends SimpleInterface {
    public DeleteTownConfirm(String n) {
        super(n);
        ItemStack yes = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("Yes!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"Yes\"").pack();
        ItemStack no = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("No!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"No\"").pack();
        ItemStack center = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("Select!").setLoreStyle(LoreStyle.INFO).addLoreLine("Choose Yes/No!").pack();
        Function fYes = new Function() {
            @Override
            public void run(HashMap<String, String> INPUT) {
                UUID id = UUID.fromString(INPUT.get("Player"));
                Player p = Bukkit.getPlayer(id);
                p.performCommand("t delete ConfirmFromGUI");
                p.closeInventory();
            }
        };
        Function fNo = new Function() {
            @Override
            public void run(HashMap<String, String> INPUT) {
                UUID id = UUID.fromString(INPUT.get("Player"));
                Player p = Bukkit.getPlayer(id);
                Format.AlrtFrmt.use().a(p,"Cancelled Town Delete!");
            }

        };

        StackFunc fY = new StackFunc(yes,fYes);
        StackFunc fN = new StackFunc(no,fNo);
        StackFunc C = new StackFunc(center, new Function() {
            @Override
            public void run(HashMap<String, String> INPUT) {
                //NOTHING
            }
        });
        super.inv = new StackFunc[] {fY,fY,fY,fY,C,fN,fN,fN,fN};
        super.init();
    }
}
