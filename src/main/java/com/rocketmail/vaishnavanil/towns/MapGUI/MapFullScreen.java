package com.rocketmail.vaishnavanil.towns.MapGUI;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MapFullScreen {
    get;

    public Inventory create(Player player) {
        Inventory Map = Bukkit.createInventory(null, 9 * 5, "Towny Map");
        Integer playerx = (int) player.getLocation().getChunk().getX();
        Integer playery =  (int) player.getLocation().getChunk().getZ();
        World world = player.getLocation().getWorld();
        Integer[] currChunk = {playerx-4, playery-2};
        System.out.println("spawn: "+playerx +"   "+playery);
        System.out.println(TownS.g().isClaimed(player.getLocation().getChunk()));

        int changingy = -1;

        for(int i=0; i<45 ; i++ ){
            int changingx = i%9;
            if(changingx==0){ changingy++; }
            int chunkx = currChunk[0]+changingx;
            int chunky = currChunk[1]+changingy;
            Chunk curr = world.getChunkAt(chunkx, chunky);
            System.out.println("CHunk: "+chunkx+ " | " + chunky);
            if(i==22){ System.out.println("spawn chunk ^"); }
            System.out.println(TownS.g().isClaimed(curr));

            ItemStack item = new ItemBuilder(getItem(curr))
                    .setNameStyle(NameStyle.HIGHLIGHT)
                    .setLoreStyle(LoreStyle.INFO)
                    .setDisplayName("Chunk")
                    .setLore(getDetails(curr))
                    .pack();
            Map.setItem(i, item);
        }

        return Map;
    }

    public Chunk getChunk(int x, int y, World world){
        return world.getChunkAt(x, y);
    }

    private Material getItem(Chunk chunk){
        if(TownS.g().isClaimed(chunk)){
            if(TownS.g().getClaim(chunk).getName().equals("")){
                return Material.CAMPFIRE;
            }
            return Material.BELL;
        }else{
            return Material.BLACK_STAINED_GLASS_PANE;
        }
    }

    private List<String> getDetails(Chunk chunk) {
        List<String> lore = new ArrayList<>();
        if (!TownS.g().isClaimed(chunk)) {
            lore.add("&a&lWilderness");
            return lore;
        }
        Claim claim = TownS.g().getClaim(chunk);
        lore.add("&f&lPlot Claimed");
        lore.add(" ");
        lore.add("Town: &c" + claim.getTown().getName());
        if(claim.getName().equals("")){
            lore.add("Area Name: &aUnnamed" );
        }else{
            lore.add("Area Name: &a" +claim.getName());
        }
        lore.add("Claim Owner: &6" + claim.getOwner().getName()+"  ");
        lore.add(" ");
        lore.add("&e&lClick To View Flags");
        return lore;
    }

}
