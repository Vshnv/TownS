package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import com.rocketmail.vaishnavanil.towns.Utilities.SaveManager;
import org.bukkit.Bukkit;
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
        RegenComplete();
        return;
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
            TownS.g().finishRegenWork(this);
            this.RegenComplete();
            return;
        }
        this.saveUnfinishedData();
    }

    public static void ContinueRegens(){
        TownS.g().alertQueue();
        for(World w:TownS.g().getServer().getWorlds()){
            if(!ConfigManager.get.isRegenWorld(w))continue;
            Set<Integer[]> ChunkCoordList = loadUnfinData(w.getName());
            if(ChunkCoordList == null)continue;
            if(ChunkCoordList.isEmpty())continue;

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
            if(!ef.getParentFile().exists()){
                ef.getParentFile().mkdirs();
            }
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
                boolean go = true;
                for(Integer[] list:unfin){
                    if(list[0] ==X && list[1] == Z){
                        go = false;
                        break;
                    }
                }
                TownS.g().registerRegenBuilder(this);
                if(!go)return;
                unfin.add(arr);

                SaveManager.use.Save(unfin,"PendingRegens\\"+world,"Regens.dat");

    }
    public void RegenComplete(){


                Set<Integer[]> unfin = loadUnfinData();
                Integer[] rem = new Integer[1];
                for(Integer[] ChunkCoords : unfin){
                    if(ChunkCoords[0] == X && ChunkCoords[1] == Z){
                        rem =(ChunkCoords);
                        break;
                    }
                }
                unfin.remove(rem);
                SaveManager.use.Save(unfin,"PendingRegens\\"+world,"Regens.dat");
                TownS.g().finishRegenWork(this);


    }
    public void Build() {
        if(loaded_data == null){
            this.RegenComplete();
            return;
        }

        Chunk toRegen = TownS.g().getServer().getWorld(world).getChunkAt(X,Z);
        toRegen.setForceLoaded(true);
        if(toRegen == null){
            this.RegenComplete();
            return;
        }
        if(!toRegen.isLoaded()){
            Bukkit.getServer().getConsoleSender().sendMessage("[TownS-Regenerator] Regen Job on unloaded chunk! Force Loading safely");
            toRegen.load(false);

        }

        if(toRegen == null){
            this.RegenComplete();
            return;
        }
        new BukkitRunnable() {

        @Override
            public void run() {
            for (int y = 0; y <= 255; y++) {
                for (int x = 0; x <= 15; x++) {
                    for (int z = 0; z <= 15; z++) {
                        if(toRegen.getBlock(x,y,z) == null){
                            return;
                        }
                        if (toRegen.getBlock(x, y, z).getType() != loaded_data[x][y][z]) {
                           if(loaded_data[x][y][z] == Material.TALL_GRASS || loaded_data[x][y][z] == Material.TALL_SEAGRASS || loaded_data[x][y][z].toString().endsWith("ORE")){
                                continue;
                            }
                            toRegen.getBlock(x, y, z).setType(loaded_data[x][y][z], false);
                            return;
                        }
                    }
                }
            }
            RegenComplete();
            toRegen.setForceLoaded(false);
            Collection<Entity> eList = toRegen.getWorld().getNearbyEntities(toRegen.getBlock(7,60,7).getLocation(),100,100,100);
            boolean s = false;

            outerloop:
            for(Entity e:eList){
                if(e instanceof Player){
                    s = true;
                    break outerloop;
                }
            }

            if(toRegen.isLoaded() && !s)toRegen.unload(true);//SAFE SAVED CHUNK UNLOAD
            this.cancel();
            //END BUILD
        }
    }.runTaskTimer(TownS.g(),10,1);
    }
}
