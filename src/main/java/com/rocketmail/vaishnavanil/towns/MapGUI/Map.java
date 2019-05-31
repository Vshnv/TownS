package com.rocketmail.vaishnavanil.towns.MapGUI;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.ItemBuilder;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemLore.LoreStyle;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Map {
    //5*9
    get;
    int s0 = 2;
    int s1 = 1;
    int s2 = 0;
    int s3 = -1;
    int s4 = -2;

    public Inventory create(Player player) {
        Inventory Map = Bukkit.createInventory(null, 9 * 5, "Towny Map");
        int center = 22;
        ItemStack centerItem = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE)
                .setNameStyle(NameStyle.DESIGNED)
                .setLoreStyle(LoreStyle.INFO)
                .setDisplayName("Current Chunk")
                .setLore(getDetails(player.getLocation().getChunk()))
                .pack();

        Map.setItem(22, centerItem);

        for (int n = 0; n <= Map.getSize() - 1; n++) {
            int y = n % 9 + 1;
            if (y == 1 || y == 2 || y == 8 || y == 9) {
                Map.setItem(n, GenerateDetection());
                continue;
            }
            if (n == center) continue;
            Chunk cur = getChunkFromSlot(n, player);
            ItemStack item = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                    .setNameStyle(NameStyle.HIGHLIGHT)
                    .setLoreStyle(LoreStyle.INFO)
                    .setDisplayName("Chunk")
                    .setLore(getDetails(cur))
                    .pack();
            Map.setItem(n, item);
        }
        /*for(int r1 = 2;r1<=6;r1++){
            Chunk cur = getChunkFromSlot(r1,player);
            ItemStack item = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                    .setNameStyle(NameStyle.HIGHLIGHT)
                    .setLoreStyle(LoreStyle.INFO)
                    .setDisplayName("Chunk")
                    .setLore(getDetails(cur))
                    .pack();
            Map.setItem(r1,item);
        }

        for(int r1 = 11;r1<=15;r1++){
            Chunk cur = getChunkFromSlot(r1,player);
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta m = item.getItemMeta();
            m.setDisplayName("Chunk");
            m.setLore(getDetails(cur));
            item.setItemMeta(m);
            Map.setItem(r1,item);
        }

        for(int r1 = 20;r1<=24;r1++){
            if(r1==center)continue;
            Chunk cur = getChunkFromSlot(r1,player);
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta m = item.getItemMeta();
            m.setDisplayName("Chunk");
            m.setLore(getDetails(cur));
            item.setItemMeta(m);
            Map.setItem(r1,item);
        }

        for(int r1 = 29;r1<=33;r1++){
            Chunk cur = getChunkFromSlot(r1,player);
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta m = item.getItemMeta();
            m.setDisplayName("Chunk");
            m.setLore(getDetails(cur));
            item.setItemMeta(m);
            Map.setItem(r1,item);
        }

        for(int r1 = 38;r1<=42;r1++){
            Chunk cur = getChunkFromSlot(r1,player);
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta m = item.getItemMeta();
            m.setDisplayName("Chunk");
            m.setLore(getDetails(cur));
            item.setItemMeta(m);
            Map.setItem(r1,item);
        }

        for(int i = 0;i<=Map.getSize()-1;i++){
            if(Map.getItem(i) == null || Map.getItem(i).getType() == Material.AIR ){
                Map.setItem(i, GenerateDetection());
            }
        }*/

        return Map;
    }


    private ItemStack GenerateDetection() {
        ItemStack stack = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setNameStyle(NameStyle.HIGHLIGHT)
                .setLoreStyle(LoreStyle.DETECTION)
                .setDisplayName("MAP MENU")
                .setLore(Arrays.asList("Map Menu Display"))
                .pack();
        return stack;
    }

    private List<String> getDetails(Chunk chunk) {
        List<String> lore = new ArrayList<>();
        if (!TownS.g().isClaimed(chunk)) {
            lore.add("Not Claimed!");
            return lore;
        }
        Claim claim = TownS.g().getClaim(chunk);
        lore.add("Claimed!");
        lore.add("Town         ->> " + claim.getTown().getName());
        lore.add("Claim Owner ->> " + claim.getOwner().getName());
        lore.add("&l->>Click To View Flags<<-");
        return lore;
    }

    public Chunk getChunkFromSlot(int slot, Player player) {
        int slotxRAW = (slot - slot % 9) / 9;
        int slotx = 0;
        switch (slotxRAW) {
            case 0:
                slotx = s0;
                break;
            case 1:
                slotx = s1;
                break;
            case 2:
                slotx = s2;
                break;
            case 3:
                slotx = s3;
                break;
            case 4:
                slotx = s4;
                break;
            default:
                return null;
        }
        int sloty = slot % 9 - 4;
        Chunk cur = player.getWorld().getChunkAt(player.getLocation().getChunk().getX() + slotx, player.getLocation().getChunk().getZ() + sloty);
        return cur;
    }

}
