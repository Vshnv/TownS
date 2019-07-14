package com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import javax.management.monitor.Monitor;

public class MobManagingEventListener implements Listener {
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (!TownS.g().isClaimed(e.getEntity().getLocation().getChunk())) return;

        Claim claim = TownS.g().getClaim(e.getEntity().getLocation().getChunk());

        if (!claim.hasFlag(Flag.MOBS)) {

            if (ConfigManager.get.isAllowed(e.getEntityType())) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEDE(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player && !(e.getDamager() instanceof Player)) {
            Player nga = (Player) e.getEntity();
            if (TownS.g().isClaimed(nga.getLocation().getChunk())) {
                if (!TownS.g().getClaim(nga.getLocation().getChunk()).hasFlag(Flag.MOBS)) {
                    e.setCancelled(true);
                }
            }
        } else if (e.getDamager() instanceof Player && !(e.getEntity() instanceof Player)) {
            Player nga = (Player) e.getDamager();
            if (TownS.g().isClaimed(nga.getLocation().getChunk())) {
                if (!TownS.g().getClaim(nga.getLocation().getChunk()).hasFlag(Flag.MOBS)) {
                    e.setCancelled(true);
                    nga.sendActionBar('&', "&cCannot attack mobs while in Mob protected claim");
                }
            }
        }

    }
}
