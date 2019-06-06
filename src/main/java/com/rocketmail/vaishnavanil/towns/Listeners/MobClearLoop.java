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

public enum MobClearLoop {
    get;

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {


                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (Entity e : player.getNearbyEntities(32, 256, 32)) {
                        if (e instanceof Monster) {
                            if (!TownS.g().getClaim(e.getLocation().getChunk()).hasFlag(Flag.MOBS)) {
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

        }.runTaskTimerAsynchronously(TownS.g(), 100, 100);
    }
}
