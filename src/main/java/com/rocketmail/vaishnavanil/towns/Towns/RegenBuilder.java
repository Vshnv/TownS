package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import com.rocketmail.vaishnavanil.towns.Utilities.SaveManager;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.load;
import static java.lang.System.out;

public class RegenBuilder {
    private Material[][][] loaded_data;
    private int X;
    private int Z;
    private String world;
    public RegenBuilder(Material[][][] data,Chunk regen){
        if(data == null){
            out.println("No saved data found!");
        }
        loaded_data = data;
        X = regen.getX();
        Z = regen.getZ();
        world = regen.getWorld().getName();
        this.saveUnfinishedData();

    }
    public Chunk getChunk(){
        return TownS.g().getServer().getWorld(world).getChunkAt(X,Z);
    }
    public RegenBuilder(Integer[] ChunkCoords,String world){
        this.X = ChunkCoords[0];
        this.Z = ChunkCoords[1];
        this.world = world;
        loaded_data = (Material[][][]) LoadManager.get.loadObject("ChunkSaves",X+"TT"+Z+"TT"+world+".dat");
        if(loaded_data == null){
            out.println("No saved data found to countinue an Unfinished Regen!");
            TownS.g().finishRegenWork(this);
            this.RegenComplete();
        }
        this.saveUnfinishedData();
    }

    public static void ContinueRegens(){
        TownS.g().alertQueue();
        for(World w:TownS.g().getServer().getWorlds()){
            Set<Integer[]> ChunkCoordList = loadUnfinData(w.getName());
            if(ChunkCoordList == null)continue;
            if(ChunkCoordList.isEmpty())continue;
            if(!ConfigManager.get.isRegenWorld(w))continue;
            for(Integer[] ChunkCoords:ChunkCoordList){
                new RegenBuilder(ChunkCoords,w.getName());
            }

        }
    }
    public static Set<Integer[]> loadUnfinData(String world){
        //File ef = new File(TownS.g().getDataFolder().getPath()+"\\" + "PendingRegens\\"+world,"Regens.dat");
        File crte = new File(TownS.g().getDataFolder().getPath() + "\\"+"PendingRegens\\"+world);
        if(!crte.exists())crte.mkdirs();
       /* if(!ef.exists()){
            try {
                ef.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
       Set<Integer[]> unfin = null;
        try{
            unfin = (Set<Integer[]>)LoadManager.get.loadObject("PendingRegens\\"+world,"Regens.dat");
        }catch (Exception e) {
            unfin = new HashSet<>();
        }

        if(unfin == null){
            unfin = new HashSet<>();
        }
        return unfin;
    }

    public  Set<Integer[]> loadUnfinData(){
        File ef = new File(TownS.g().getDataFolder().getPath()+"\\" + "PendingRegens\\"+world,"Regens.dat");
        if(!ef.exists()){
            try {
                ef.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }Set<Integer[]> unfin = null;
        try{
            unfin = (Set<Integer[]>)LoadManager.get.loadObject("PendingRegens\\"+world,"Regens.dat");
        }catch (Exception e) {
            unfin = new HashSet<>();
        }

        if(unfin == null){
            unfin = new HashSet<>();
        }
        return unfin;
    }
    public void saveUnfinishedData(){

                Set<Integer[]> unfin = loadUnfinData();
                Integer[] arr = new Integer[]{X,Z};
                unfin.add(arr);

                SaveManager.use.Save(unfin,"PendingRegens\\"+world,"Regens.dat");
                TownS.g().registerRegenBuilder(this);

    }
    public void RegenComplete(){


                Set<Integer[]> unfin = loadUnfinData();
                Integer[] rem = new Integer[1];
                for(Integer[] ChunkCoords : unfin){
                    if(ChunkCoords[0] == X && ChunkCoords[1] == Z){
                        rem =(ChunkCoords);
                    }
                }
                unfin.remove(rem);
                SaveManager.use.Save(unfin,"PendingRegens\\"+world,"Regens.dat");
                TownS.g().finishRegenWork(this);


    }
    public void Build() {
        if(loaded_data == null){
            out.println("No saved data found! cannot build!");
            this.RegenComplete();
            return;
        }

        Chunk toRegen = TownS.g().getServer().getWorld(world).getChunkAt(X,Z);
        if(toRegen == null){
            out.println("Error getting chunk from data! cannot build!");
            return;
        }
        if(!toRegen.isLoaded()){
            toRegen.load();
        }

        if(toRegen == null){
            out.println("Error getting chunk from data! cannot build!");
            return;
        }
        new BukkitRunnable() {

        @Override
            public void run() {
            for (int y = 0; y <= 255; y++) {
                for (int x = 0; x <= 15; x++) {
                    for (int z = 0; z <= 15; z++) {
                        if(toRegen.getBlock(x,y,z) == null){
                            out.println("Null in build");
                            return;
                        }
                        if (toRegen.getBlock(x, y, z).getType() != loaded_data[x][y][z]) {
                           if(loaded_data[x][y][z] == Material.TALL_GRASS || loaded_data[x][y][z] == Material.TALL_SEAGRASS){
                                continue;
                            }
                            toRegen.getBlock(x, y, z).setType(loaded_data[x][y][z], false);
                            return;
                        }
                    }
                }
            }
            RegenComplete();
            Collection<Entity> eList = toRegen.getWorld().getNearbyEntities(toRegen.getBlock(7,60,7).getLocation(),100,100,100);
            boolean s = false;

            outerloop:
            for(Entity e:eList){
                if(e instanceof Player){
                    s = true;
                    break outerloop;
                }
            }
            if(!s)toRegen.unload();
            this.cancel();
            //END BUILD
        }
    }.runTaskTimer(TownS.g(),10,1);
    }
}
