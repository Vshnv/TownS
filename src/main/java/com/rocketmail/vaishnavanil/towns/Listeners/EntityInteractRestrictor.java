package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityInteractRestrictor implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEIE(PlayerInteractEntityEvent e) {
        if (!TownS.g().isClaimed(e.getRightClicked().getLocation().getChunk())) return;

        Claim c = TownS.g().getClaim(e.getRightClicked().getLocation().getChunk());
        if (c.getOwnerID() == e.getPlayer().getUniqueId() || c.getTown().getMayor().getUniqueId() == e.getPlayer().getUniqueId()) {

            return;

        }
        e.setCancelled(true);
    }


}
