package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class WarpsGUI extends SimpleInterface {

    public WarpsGUI(String n, Player player) {
        super(n);

        if (TownS.g().hasTown(player)) {
            List<String> warpList = new ArrayList<>();
            List<ItemStack> warpListItem = new ArrayList<>();
            Town town = TownS.g().getTown(player);
            Set<String> warpnames = town.getTownWarpKeys();
            if(!town.getTownWarpKeys().isEmpty()){ warpList.addAll(warpnames); }
            warpList.remove("spawn");

            Function teleportToWarp = new Function() {
                @Override
                public void run(HashMap<String, String> INPUT) {
                    UUID id = UUID.fromString(INPUT.get("Player"));
                    Player player = Bukkit.getPlayer(id);
                    String slot = INPUT.get("Slot");
                    player.performCommand("town warp " + warpList.get(Integer.valueOf(slot)));
                }
            };

            StackFunc[] farray = new StackFunc[warpList.size() + (9 - warpList.size() % 9)];
            int i = 0;
            for (String warpname : warpList) {
                if(!warpname.equalsIgnoreCase("spawn")){
                    ItemStack is = new ItemBuilder(Material.BELL).setDisplayName("Warp: " + warpname).setLoreStyle(LoreStyle.INFO).addLoreLine("Click to Warp").pack();
                    warpListItem.add(is);
                    StackFunc fY = new StackFunc(is, teleportToWarp);
                    farray[i] = fY;
                    i++;
                }
            }

            Function fNo = new Function() {
                @Override
                public void run(HashMap<String, String> INPUT) {
                }
            };

            /*EMPTY PLACEHOLDER FILL*/
            ItemStack no = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").pack();
            StackFunc fN = new StackFunc(no, fNo);
            for (int a = 0; a < (farray.length - warpList.size()); a++) {
                farray[i] = fN;
                i++;
            }
            /*EMPTY PLACEHOLDER FILL*/
            System.out.println(farray.length);
            super.inv = farray;
            super.init();

        }
    }

}
