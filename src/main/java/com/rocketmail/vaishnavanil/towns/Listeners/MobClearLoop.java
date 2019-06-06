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

public enum MobClearLoop {
    get;

    public void start(){
        new BukkitRunnable(){

            @Override
            public void run() {
                for(World world:Bukkit.getServer().getWorlds()){
                    for(Entity e:world.getEntities()){
                        if(!(e instanceof LivingEntity))continue;
                        if((e instanceof Player))continue;
                        if(TownS.g().isClaimed(e.getLocation().getChunk())){
                            Claim c1 = TownS.g().getClaim(e.getLocation().getChunk());
                            if(c1.hasFlag(Flag.MOBS))continue;
                            try {
                                if( (LivingEntity) e instanceof Monster){
                                    ((LivingEntity) e).remove();
                                }
                            }catch (Exception ex){

                            }
                        }
                    }
                }
            }



        }.runTaskTimer(TownS.g(),100,100);
    }
}
