package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Flag;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public enum MobClearLoop {
    get;

    public void start(){
        new BukkitRunnable(){

            @Override
            public void run() {
                for(World world:Bukkit.getServer().getWorlds()){
                    for(Entity e:world.getEntities()){
                        if(!(e instanceof LivingEntity))continue;

                        if(TownS.g().isClaimed(e.getLocation().getChunk())){
                            Claim c1 = TownS.g().getClaim(e.getLocation().getChunk());
                            if(c1.hasFlag(Flag.MOBS))continue;
                            ((LivingEntity)e).remove();
                        }
                    }
                }
            }
        }.runTaskTimer(TownS.g(),100,100);
    }
}
