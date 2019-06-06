package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class WarpsGUI extends SimpleInterface {

    public WarpsGUI(String n, Player player) {
        super(n);

        if(TownS.g().hasTown(player)){
            List<String> warpList = new ArrayList<>();
            List<ItemStack> warpListItem = new ArrayList<>();
            Town town = TownS.g().getTown(player);
            Set<String> warpnames = town.getTownWarpKeys();
            warpnames.remove("spawn");
            warpList.addAll(warpnames);

            Function teleportToWarp = new Function() {
                @Override
                public void run(HashMap<String, String> INPUT) {
                    UUID id = UUID.fromString(INPUT.get("Player"));
                    Player player = Bukkit.getPlayer(INPUT.get("Player"));
                    String slot = INPUT.get("Slot");
                    Bukkit.broadcastMessage(slot);
                    Bukkit.broadcastMessage(warpList.get(Integer.valueOf(slot)));
                    //player.performCommand();
                    if(player == null){ Bukkit.broadcastMessage("playernull"); }
                    player.sendMessage("Works");
                    //Bukkit.broadcastMessage(contextPlayer.getName());
                }
            };

            StackFunc[] farray = new StackFunc[warpList.size()];
            int i = 0;
            for(String warpname: warpList){
                ItemStack is = new ItemBuilder(Material.ENDER_PEARL).setDisplayName("Warp: "+warpname).setLoreStyle(LoreStyle.INFO).addLoreLine("Click to Warp").pack();
                warpListItem.add(is);
                StackFunc fY = new StackFunc(is,teleportToWarp);
                farray[i] = fY;
                i++;
            }

            Function fNo = new Function() {
                @Override
                public void run(HashMap<String, String> INPUT) {
                    UUID id = UUID.fromString(INPUT.get("Player"));
                    Bukkit.getPlayer(id).sendMessage("Works too");
                }

            };
            ItemStack no = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNameStyle(NameStyle.HIGHLIGHT).setDisplayName("No!").setLoreStyle(LoreStyle.INFO).addLoreLine("Proceed with \"No\"").pack();

            StackFunc fN = new StackFunc(no,fNo);
            //farray[i] = fN;
            super.inv = farray;
            super.init();

        }
    }

}
