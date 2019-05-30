package com.rocketmail.vaishnavanil.towns.MapGUI;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class InvClickListen implements Listener {
    int s0 = 2;
    int s1 = 1;
    int s2 = 0;
    int s3 = -1;
    int s4 = -2;
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (ChatColor.stripColor(e.getView().getTopInventory().getItem(0).getItemMeta().getDisplayName()).equalsIgnoreCase("MAP MENU")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            int slotxRAW = (e.getSlot()-e.getSlot()%9)/9;
            int slotx = 0;
            switch (slotxRAW){
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
                default: return;
            }
            if(e.getSlot()%9 == 0 || e.getSlot()%9 == 1)return;
            int sloty = e.getSlot()%9-4;
            Chunk cur = player.getWorld().getChunkAt(player.getLocation().getChunk().getX()+slotx,player.getLocation().getChunk().getZ()+sloty);
           if(TownS.g().isClaimed(cur)){
               player.openInventory(FlagShow.get.create(player,TownS.g().getClaim(cur)));
               player.updateInventory();
           }
            return;
        }
        if (ChatColor.stripColor(e.getView().getTopInventory().getItem(0).getItemMeta().getDisplayName()).equalsIgnoreCase("Flag List")) {
            e.setCancelled(true);
        }
    }
}
