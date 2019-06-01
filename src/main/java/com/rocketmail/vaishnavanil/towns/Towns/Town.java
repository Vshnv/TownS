package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Town {
    private String town_name;
    private UUID Mayor_ID;
    private HashMap<UUID, Rank> rankMap = new HashMap<>();
    private HashMap<String, Location> warpPointMap = new HashMap<>();
    private List<UUID> Members = new ArrayList<>();
    private List<Town> Allies = new ArrayList<>();
    private List<Town> Enemies = new ArrayList<>();
    private Chunk spawnChunk;

    public String getName() {
        return town_name;
    }

    public Player getMayor() {
        return Bukkit.getPlayer(Mayor_ID);
    }

    public void setRank(Player p, Rank rank) {
        rankMap.put(p.getUniqueId(), rank);
    }

    public Rank getRank(Player p) {
        return rankMap.get(p.getUniqueId());
    }

    public Rank getRank(UUID id) {
        return rankMap.get(id);
    }

    public boolean hasRank(Player p) {
        return rankMap.keySet().contains(p.getUniqueId());
    }

    public boolean hasRank(UUID id) {
        return rankMap.keySet().contains(id);
    }

    public boolean hasPermission(String perm, Player player) {
        if (!hasRank(player)) return false;
        return getRank(player).hasPermission(perm);
    }

    public void claim(Chunk chunk, Player owner) {
        new Claim(chunk, this, owner.getUniqueId());
    }

    public boolean setSpawnChunk(Town town, Chunk chunk){
        if(TownS.g().isClaimed(chunk)){
            Claim claim = TownS.g().getClaim(chunk);
            if(claim.getTown().equals(town)){
                town.spawnChunk = chunk;
            }
        }
        return false;
    }

    public boolean isSpawnChunk(Chunk chunk){
        if(spawnChunk == null || chunk == null)
            return false;
        return chunk.equals(spawnChunk);
    }

    public void unclaim(Chunk chunk) {
        TownS.g().rCfT(TownS.g().getClaim(chunk));
    }

    public void Ally(Town t) {
        unEnemy(t);
        Allies.add(t);
    }

    public void Enemy(Town t) {
        unAlly(t);
        Enemies.add(t);
    }

    public void unAlly(Town t) {
        Allies.remove(t);
    }

    public void unEnemy(Town t) {
        Enemies.remove(t);
    }

    public void setWarpPoint(Town town, String spawn_name, Location location){
        if(TownS.g().isClaimed(location.getChunk())){
            Claim claim = TownS.g().getClaim(location.getChunk());
            if(claim != null && claim.getTown().equals(town)){
                town.warpPointMap.put(spawn_name, location);
                return;
            }
        }
        Bukkit.broadcastMessage("[TOWNS] Failed to set warp point: "+spawn_name+" Town: "+town.getName());
    }

    public Location getWarpPoint(Town town, String spawn_name){
        if(warpPointMap.get(spawn_name) != null){
            Location warp_location = warpPointMap.get(spawn_name);
            if(TownS.g().isClaimed(warp_location.getChunk())){
                Town town_at_location = TownS.g().getTown(warp_location.getChunk());
                if(town_at_location.equals(town)){
                    return warpPointMap.get(spawn_name);
                }else{
                    warpPointMap.remove(spawn_name);
                    return null;
                }
            }else{
                warpPointMap.remove(spawn_name);
                return null;
            }
        }
        return null;
    }

    public void deleteTown() {
        TownS.g().rPfT(Mayor_ID);

        for (UUID a : rankMap.keySet()) {
            TownS.g().rPfT(a);
        }
        for (UUID m : Members) {
            TownS.g().rPfT(m);
        }
        TownS.g().rTfM(this);
    }

    public boolean belongs(Player player) {
        if (Members.contains(player.getUniqueId()) || rankMap.containsKey(player.getUniqueId()) || Mayor_ID == player.getUniqueId())
            return true;
        return false;
    }

    public Town(String name, Player mayor) {
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        TownS.g().aTtM(this);
    }

    public Town(String name, Player mayor, List<UUID> MemberL, List<UUID> AssistantL) {
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
        //Assistants = AssistantL;
        TownS.g().aTtM(this);
    }

    public Town(String name, Player mayor, List<UUID> MemberL, List<UUID> AssistantL, List<Claim> claims) {
        town_name = name;
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
        // Assistants = AssistantL;

        for (Claim c : claims) {
            TownS.g().aCtT(c);
        }
        TownS.g().aTtM(this);
    }

}
