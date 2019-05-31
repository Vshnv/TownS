package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Claim {
    //DATA
    private int CX;
    private int CZ;
    private String world;
    private String town_name;
    private UUID ownerID;
    private boolean fs = false;
    private double cost = 100;
    //DATA

    private List<Flag> EnabledFlags = new ArrayList<>();
    //CONSTRUCTORS
    protected Claim(Chunk chunk,Town town){
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_name = town.getName();
        ownerID = town.getMayor().getUniqueId();
        TownS.g().aCtT(this);
    }

    public void setOwner(Player player){
        setOwner(player.getUniqueId());
    }
    public void setOwner(UUID id){
        ownerID = id;
    }

    protected Claim(Chunk chunk, Town town, Player owner){
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_name = town.getName();
        ownerID = owner.getUniqueId();
        TownS.g().aCtT(this);
    }
    public Chunk getChunk(){
        return Bukkit.getWorld(world).getChunkAt(x(),z());
    }
    public World getWorld(){
        return Bukkit.getWorld(world);
    }

    public java.lang.String getWorldName() {
        return world;
    }

    public int x(){
        return CX;
    }
    public int z(){
        return CZ;
    }
    public void setTown(Town town){
        town_name = town.getName();
    }
    protected Claim(Chunk chunk, Town town, Player owner, List<Flag> flags){
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_name = town.getName();
        ownerID = owner.getUniqueId();
        EnabledFlags.addAll(flags);
    }
    //END CONSTRUCTORS

    public Town getTown(){
        if(TownS.g().townExists(town_name)) return TownS.g().getTown(town_name);
        return null;
    }

    public void setFS(boolean FS,double Cost){
        fs = FS;
        cost = Cost;
    }
    public List<Flag> getFlags(){
        return EnabledFlags;
    }
    public Player getOwner(){
        return Bukkit.getPlayer(ownerID);
    }
    public boolean isFS(){
        return fs;
    }
    public void addFlag(Flag flag){
        EnabledFlags.add(flag);
    }
    public void removeFlag(Flag flag){
        EnabledFlags.remove(flag);
    }
}
