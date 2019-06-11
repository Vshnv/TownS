package com.rocketmail.vaishnavanil.towns.Utilities;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public enum RegenSaveQueueManager {
    get;

    private Queue<ChunkSnapshot> tSve = new LinkedList<>();
    private List<String> QueuedID = new ArrayList<>();
    private List<String> SavedChunkIDcache = new ArrayList<>();


    public boolean isCached(Chunk c){
        return SavedChunkIDcache.contains(TownS.getChunkID(c));
    }
    public boolean isQueued(Chunk c){
        return QueuedID.contains(TownS.getChunkID(c));
    }

    public boolean addToQueue(Chunk chunk){
        if(!ConfigManager.get.isRegenWorld(chunk.getWorld()))return true;
        if(SavedChunkIDcache.contains(TownS.getChunkID(chunk)))return true;
        if(QueuedID.contains(TownS.getChunkID(chunk)))return false;

            File f =new File(TownS.g().getDataFolder().getPath() + "\\ChunkSaves",chunk.getX()+"TT" + chunk.getZ() + "TT" + chunk.getWorld().getName()+".dat");
        if(f.exists()) {
            f = null;
            if(!SavedChunkIDcache.contains(TownS.getChunkID(chunk)))SavedChunkIDcache.add(TownS.getChunkID(chunk));
            return true;
        }
        ChunkSnapshot TSve = chunk.getChunkSnapshot();
        if(!tSve.contains(TSve)){
            tSve.add(TSve);
            if(!QueuedID.contains(TownS.getChunkID(chunk))){
                QueuedID.add(TownS.getChunkID(chunk));
            }
        }
        return false;
    }
    public void AddChunkFileToCache(File f){
        String ChunkID = f.getName().replace("TT","::");
        if(!SavedChunkIDcache.contains(ChunkID))SavedChunkIDcache.add(ChunkID);
    }
    public void runSaveQueue(){
        TownS.g().regTask(new BukkitRunnable(){

            @Override
            public void run() {
                if(tSve.peek() == null)return;
                //if(saving)return;
                ChunkSnapshot toSave = tSve.poll();
                Material[][][] MatMaping = new Material[16][256][16];

                for(int y = 0;y<=255;y++) {
                    for (int x = 0; x <= 15; x++) {
                        for(int z = 0;z<=15;z++){
                            MatMaping[x][y][z] = toSave.getBlockType(x,y,z);
                        }
                    }
                }



                SaveManager.use.Save(MatMaping,"ChunkSaves",toSave.getX()+"TT" + toSave.getZ() + "TT" + toSave.getWorldName()+".dat");
                //out.println("Saved a chunk ASYNC");
                if(!SavedChunkIDcache.contains(TownS.getChunkID(toSave)))SavedChunkIDcache.add(TownS.getChunkID(toSave));

                QueuedID.remove(TownS.getChunkID(toSave));
            }
        }.runTaskTimerAsynchronously(TownS.g(),1,1));
    }
}
