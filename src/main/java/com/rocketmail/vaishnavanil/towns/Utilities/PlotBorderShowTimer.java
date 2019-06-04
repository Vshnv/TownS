package com.rocketmail.vaishnavanil.towns.Utilities;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public enum  PlotBorderShowTimer {
    INSTANCE;

    public void startBorderShow(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size()<1)
                    return;
                for(Player player: Bukkit.getOnlinePlayers()){
                    if(TownS.g().getTownPlayer(player).showBorder()){
                        if(TownS.g().isClaimed(player.getLocation().getChunk())){
                            ParticleManager.INSTANCE.drawChunkBorder(player, player.getLocation().getChunk(), Color.WHITE);
                        }else {
                            ParticleManager.INSTANCE.drawChunkBorder(player, player.getLocation().getChunk(), Color.LIME);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(TownS.g(), 1, 10);
    }

}
