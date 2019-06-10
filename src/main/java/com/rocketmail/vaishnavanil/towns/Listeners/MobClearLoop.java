package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;

public enum MobClearLoop {
    get;

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
List<Player> l = (List<Player>) Bukkit.getOnlinePlayers();
    if(l.isEmpty())return;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(!player.getWorld().getName().equals("world")) return;
                    List<Entity> t =player.getNearbyEntities(32, 256, 32);
                    if(t.isEmpty())continue;
                    for (Entity e : t) {
                        if (e instanceof Monster) {
                            if (TownS.g().isClaimed(e.getLocation().getChunk()) &&  !TownS.g().getClaim(e.getLocation().getChunk()).hasFlag(Flag.MOBS)) {
                                try {
                                    new BukkitRunnable(){
                                        @Override
                                        public void run(){
                                            ((LivingEntity) e).remove();
                                        }
                                    }.runTask(TownS.g());
                                } catch (Exception exp) {

                                }
                            }
                        }
                    }
                }
            }

        }.runTaskTimerAsynchronously(TownS.g(), 100, 50);
    }
}
