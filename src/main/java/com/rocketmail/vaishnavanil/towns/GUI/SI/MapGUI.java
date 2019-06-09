package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
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
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MapGUI extends SimpleInterface {
    public MapGUI(String n, Player User) {
        super(n+ " ID:" + ThreadLocalRandom.current().nextLong(1000,999999));
        Integer playerx =  User.getLocation().getChunk().getX();
        Integer playery =   User.getLocation().getChunk().getZ();
        World world = User.getLocation().getWorld();
        Integer[] currChunk = {playerx-4, playery-2};
        super.inv = new StackFunc[45];

        int changingy = -1;

        for(int i=0; i<45 ; i++ ){
            int changingx = i%9;
            if(changingx==0){ changingy++; }
            int chunkx = currChunk[0]+changingx;
            int chunky = currChunk[1]+changingy;
            Chunk curr = world.getChunkAt(chunkx, chunky);

            ItemStack Item = new ItemBuilder(getItem(curr,User))
                    .setNameStyle(NameStyle.HIGHLIGHT)
                    .setLoreStyle(LoreStyle.INFO)
                    .setDisplayName("Chunk")
                    .setLore(getDetails(curr))
                    .pack();
            StackFunc SF;
            if(TownS.g().isClaimed(curr)) {
                Function ClaimFunc = new Function() {
                    Claim c = TownS.g().getClaim(curr);
                    @Override
                    public void run(HashMap<String, String> INPUT) {
                        Player clicker = Bukkit.getPlayer(UUID.fromString(INPUT.get("Player")));
                        clicker.closeInventory();
                        clicker.openInventory(new FlagGUI("FlagList",c).get());
                    }
                };
                SF = new StackFunc(Item,ClaimFunc);
            }else{
                SF = new StackFunc(Item,NoFunction);
            }


            super.inv[i] = SF;
        }

        super.init();
    }


    private Material getItem(Chunk chunk,Player p){
        if(chunk == p.getLocation().getChunk())return Material.BREWING_STAND;
        if(TownS.g().isClaimed(chunk)){
            if(TownS.g().getClaim(chunk).getName().equals("")){
                return Material.CAMPFIRE;
            }
            return Material.BELL;
        }else{
            return Material.LIGHT_GRAY_STAINED_GLASS_PANE;
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
        lore.add("Town: &a" + claim.getTown().getName());
        if(claim.getName().equals("")){
            lore.add("Area Name: &aUnnamed" );
        }else{
            lore.add("Area Name: &a" +claim.getName());
        }
        lore.add("Claim Owner: &6" + claim.getOwner().getName());
        lore.add(" ");
        lore.add("Chunk X: &c" + claim.getChunk().getX() + "&f Chunk Z: &c"+ claim.getChunk().getZ());
        lore.add("World: &c" + claim.getChunk().getWorld().getName());
        lore.add(" ");
        lore.add("&e&lClick To View Flags");
        return lore;
    }
}
