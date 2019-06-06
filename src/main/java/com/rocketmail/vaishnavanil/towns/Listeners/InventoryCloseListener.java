package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent e){

        if(e.getReason() == InventoryCloseEvent.Reason.PLAYER){
            if(e.getView().getTitle().equalsIgnoreCase(NameStyle.HIGHLIGHT.use("Flag List"))){
                Bukkit.dispatchCommand(e.getPlayer(),"t map");
            }
        }
    }
}
