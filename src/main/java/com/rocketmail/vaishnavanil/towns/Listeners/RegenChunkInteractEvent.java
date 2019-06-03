package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.Messages.Message;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.RegenBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class RegenChunkInteractEvent implements Listener {
    @EventHandler
    public void onRCI(PlayerInteractEvent e){
        try {
            if(TownS.g().Cur.getChunk().equals(e.getClickedBlock().getLocation().getChunk())){
                e.setCancelled(true);
                Format.AlrtFrmt.use().a(e.getPlayer(),"That chunk is undergoing restoration! Please wait till it finishes!");
                return;
            }
            for (RegenBuilder b : TownS.g().getActiveRegenerators()) {
                if (b.getChunk().equals(e.getClickedBlock().getLocation().getChunk())) {
                    e.setCancelled(true);
                    Format.AlrtFrmt.use().a(e.getPlayer(),"That chunk is undergoing restoration! Please wait till it finishes!");
                    return;
                }
            }
        }catch (Exception ex){
            //Do nothing
        }
    }
}
