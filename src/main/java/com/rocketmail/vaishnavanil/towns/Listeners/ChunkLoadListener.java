package com.rocketmail.vaishnavanil.towns.Listeners;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Utilities.SaveManager;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import static java.lang.System.out;

public class ChunkLoadListener implements Listener {
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e){
        if(!ConfigManager.get.isRegenWorld(e.getWorld()))return;
        ChunkSnapshot toSave = e.getChunk().getChunkSnapshot();
        File f =new File(TownS.g().getDataFolder().getPath() + "\\ChunkSaves",toSave.getX()+"::" + toSave.getZ() + "::" + toSave.getWorldName()+".dat");
        if(f.exists()){
            f = null;
            return;
        }
        f = null;

        new BukkitRunnable(){
            @Override
            public void run() {
                Material[][][] MatMaping = new Material[16][256][16];

                for(int y = 0;y<=255;y++) {
                    for (int x = 0; x <= 15; x++) {
                        for(int z = 0;z<=15;z++){
                            MatMaping[x][y][z] = toSave.getBlockType(x,y,z);
                        }
                    }
                }



                SaveManager.use.Save(MatMaping,"ChunkSaves",toSave.getX()+"TT" + toSave.getZ() + "TT" + toSave.getWorldName()+".dat");
                this.cancel();

            }
        }.runTaskAsynchronously(TownS.g());

    }
}
