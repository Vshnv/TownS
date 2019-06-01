package com.rocketmail.vaishnavanil.towns.MapGUI;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InvClickListen implements Listener {
    int s0 = 2;
    int s1 = 1;
    int s2 = 0;
    int s3 = -1;
    int s4 = -2;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getType() == InventoryType.CREATIVE)return;
        if (NameStyle.HIGHLIGHT.use("MAP MENU").equalsIgnoreCase(e.getView().getTopInventory().getItem(0).getItemMeta().getDisplayName())){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            int slotxRAW = (e.getSlot() - e.getSlot() % 9) / 9;
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
                    return;
            }
            if (e.getSlot() % 9 == 0 || e.getSlot() % 9 == 1) return;
            int sloty = e.getSlot() % 9 - 4;
            Chunk cur = player.getWorld().getChunkAt(player.getLocation().getChunk().getX() + slotx, player.getLocation().getChunk().getZ() + sloty);
            if (TownS.g().isClaimed(cur)) {
                player.openInventory(FlagShow.get.create(player, TownS.g().getClaim(cur)));
                player.updateInventory();
            }
            return;
        }
        if (NameStyle.HIGHLIGHT.use("Flag List").equalsIgnoreCase(e.getView().getTopInventory().getItem(0).getItemMeta().getDisplayName())) {
            e.setCancelled(true);
            //if(e.getClickedInventory() != e.getView().getTopInventory())return;
            if(e.getSlot() == 0)return;
            int x = Integer.valueOf(ChatColor.stripColor(e.getView().getTopInventory().getItem(0).getItemMeta().getLore().get(3).split("::")[0]));
            int z = Integer.valueOf(ChatColor.stripColor(e.getView().getTopInventory().getItem(0).getItemMeta().getLore().get(3).split("::")[1]));

            Chunk ch = e.getWhoClicked().getLocation().getWorld().getChunkAt(x,z);
            if(!TownS.g().isClaimed(ch))return;
            Flag f = Flag.getFlag(ChatColor.stripColor(e.getView().getTopInventory().getItem(0).getItemMeta().getDisplayName()));
            Claim m = TownS.g().getClaim(ch);

            if(m.hasFlag(f))m.removeFlag(f);
            else m.addFlag(f);


            ((Player) e.getWhoClicked()).closeInventory();
            ((Player) e.getWhoClicked()).openInventory(FlagShow.get.create((Player) e.getWhoClicked(),m));
            ((Player) e.getWhoClicked()).updateInventory();

        }
    }
}
