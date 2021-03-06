package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Claim implements Serializable {
    public static final long serialVerisionUID = 71413L;

    //DATA
    private int CX;
    private int CZ;
    private String world;
    private String claim_name = "";
    private String town_uuid;
    private UUID ownerID;
    private boolean fs = false;
    private double cost = 0;
    private List<UUID> ContainerTrust = new ArrayList<>();
    private List<UUID> BuildTrust = new ArrayList<>();
    private List<String> RankBuildTrust = new ArrayList<>();
    private List<String> RankContainerTrust = new ArrayList<>();
    //DATA

    private List<Flag> EnabledFlags = new ArrayList<>();
    private HashMap<String,Object> Var = new HashMap<>();

    public void setVar(String var,Object value){
        Var.put(var,value);
    }
    public boolean varExists(String var){
        if(Var.keySet().contains(var))return true;
        return false;
    }
    public Object getVar(String var){
        return Var.get(var);
    }
    //CONSTRUCTORS
    protected Claim(Chunk chunk, Town town) {
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_uuid = town.getTownUUID().toString();
        ownerID = town.getMayor().getUniqueId();
        TownS.g().aCtT(this);
    }
    //TEST
    public void BuildTrust(Player p){
        if(!BuildTrust.contains(p.getUniqueId()))BuildTrust.add(p.getUniqueId());
    }
    public void unBuildTrust(Player p){
        BuildTrust.remove(p.getUniqueId());
    }
    public void BuildTrust(UUID id){
        if(!BuildTrust.contains(id))BuildTrust.add(id);
    }
    public void unBuildTrust(UUID id){
        BuildTrust.remove(id);
    }
    public void ContainerTrust(Player p){if(!ContainerTrust.contains(p.getUniqueId()))ContainerTrust.add(p.getUniqueId());}
    public void ContainerTrust(UUID id){if(!ContainerTrust.contains(id))ContainerTrust.add(id);}
    public void unContainerTrust(Player p){ContainerTrust.remove(p.getUniqueId());}
    public void unContainerTrust(UUID id){ContainerTrust.remove(id);}

    public void BuildTrust(Rank k){
        if(!RankBuildTrust.contains(k.getName()))RankBuildTrust.add(k.getName());
    }
    public void unBuildTrust(Rank k){
        RankBuildTrust.remove(k.getName());
    }
    public void ContainerTrust(Rank k){
        if(!(RankContainerTrust.contains(k.getName())))RankContainerTrust.add(k.getName());
    }
    public void unContainerTrust(Rank k){
        RankContainerTrust.remove(k.getName());
    }
    public Double getPlotCost(){ return cost; }



    public boolean canBuild(Player p){
        if(getTown().hasRank(p)){
            if(TownS.g().getTown(p).equals(getTown())) {
                if (RankBuildTrust.contains(getTown().getRank(p).getName())) {
                    return true;
                }
            }
        }
        if(TownS.g().hasTown(p)){
           if(TownS.g().getTown(p) == this.getTown()){
               if(this.hasFlag(Flag.EDIT)){
                   return true;
               }
           }
        }
        return (BuildTrust.contains(p.getUniqueId()))||(getTown().getMayor().getUniqueId() == p.getUniqueId()) || (ownerID == p.getUniqueId());
    }

    public boolean canUseContainer(Player p){
        if(getTown().hasRank(p)){
            if(TownS.g().getTown(p).equals(getTown())) {
                if (RankContainerTrust.contains(getTown().getRank(p).getName())) {
                    return true;
                }
            }
        }
        if(TownS.g().hasTown(p)){
            if(TownS.g().getTown(p) == this.getTown()){
                if(this.hasFlag(Flag.EDIT)){
                    return true;
                }
            }
        }
        return (ContainerTrust.contains(p.getUniqueId()))||(getTown().getMayor().getUniqueId() == p.getUniqueId()) || (ownerID == p.getUniqueId())||(getTown().hasPermission("Allow.ContainerALL",p));
    }
    public boolean hasFlag(Flag f){
        return getFlags().contains(f);
    }

    public void setOwner(Player player) {

        setOwner(player.getUniqueId());
    }

    public void setOwner(UUID id) {
        ownerID = id;
    }

    protected Claim(Chunk chunk, Town town, Player owner) {
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_uuid = town.getTownUUID().toString();
        ownerID = owner.getUniqueId();
        TownS.g().aCtT(this);
    }

    protected Claim(Chunk chunk, Town town, UUID owner_uuid) {
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_uuid = town.getTownUUID().toString();
        ownerID = owner_uuid;
        TownS.g().aCtT(this);
    }

    public Chunk getChunk() {
        return Bukkit.getWorld(world).getChunkAt(x(), z());
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public java.lang.String getWorldName() {
        return world;
    }

    public int x() {
        return CX;
    }

    public int z() {
        return CZ;
    }

    public void setTown(Town town) {
        town_uuid = town.getTownUUID().toString();
    }

    protected Claim(Chunk chunk, Town town, Player owner, List<Flag> flags) {
        world = chunk.getWorld().getName();
        CX = chunk.getX();
        CZ = chunk.getZ();
        town_uuid = town.getTownUUID().toString();
        ownerID = owner.getUniqueId();
        EnabledFlags.addAll(flags);
    }
    //END CONSTRUCTORS

    public Town getTown() {
        if (TownS.g().townExists(town_uuid)){
            return TownS.g().getTown(UUID.fromString(town_uuid));
        }
        return null;
    }

    public void setFS(boolean FS, double Cost) {
        fs = FS;
        cost = Cost;
    }

    public void setName(String name){
        claim_name = name;
    }

    public String getName(){
        if(claim_name == null)
            return "";
        return claim_name;
    }

    public List<Flag> getFlags() {
        return EnabledFlags;
    }
    public UUID getOwnerID() {
        return ownerID;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(ownerID);
    }

    public boolean isFS() {
        return fs;
    }

    public void addFlag(Flag flag) {
        EnabledFlags.add(flag);
    }

    public void removeFlag(Flag flag) {
        EnabledFlags.remove(flag);
    }

    public List<UUID> getBuildTrusted() { return BuildTrust;  }
    public List<UUID> getContainerTrusted(){ return ContainerTrust; }
    public List<String> getRankBuildTrusted(){ return RankBuildTrust; }
    public List<String> getRankContainerTrusted(){ return RankContainerTrust; }

}
