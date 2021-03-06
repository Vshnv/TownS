package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.Economy.EcoUpkeep;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Utilities.ActionBar;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import com.rocketmail.vaishnavanil.towns.Utilities.RegenSaveQueueManager;
import com.rocketmail.vaishnavanil.towns.Utilities.WarpLocation;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Serializable;
import java.util.*;

public class Town implements Serializable {
    public static final long serialVerisionUID = 537141124913L;
    private String town_name;
    private UUID town_uuid;
    private UUID Mayor_ID;
    private Double town_balance;
    private HashMap<UUID, Rank> rankMap = new HashMap<>();
    private HashMap<String, WarpLocation> warpPointMap = new HashMap<>();
    private List<UUID> Members = new ArrayList<>();
    private List<Town> Allies = new ArrayList<>();
    private List<Town> Enemies = new ArrayList<>();
    private String spawnChunkID;
    private List<Claim> townClaims = new ArrayList<>();
    private HashMap<String,Object> Var = new HashMap<>();
    public void regClaim(Claim c) {
        if (townClaims.contains(c)) return;
        if (c.getTown() != this) {
            townClaims.remove(c);
            return;
        }

        townClaims.add(c);
    }
    public Set<OfflinePlayer> getMembers(){
        Set<OfflinePlayer> op = new HashSet<>();
        for(UUID id:Members){
            op.add(Bukkit.getOfflinePlayer(id));
        }
        return op;
    }
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

    public double getCurrentUpkeep() {
        return EcoUpkeep.UPKEEP_PER_CLAIM * this.getClaims().size();
    }
    public List<Claim> getClaims() {
        return townClaims;
    }
    public List<Claim> getPayingClaims() {
        List<Claim> cll = new ArrayList<>();
        for(Claim c:townClaims){
            if(c.getOwnerID() == c.getTown().getMayor().getUniqueId())continue;
            cll.add(c);
        }
        return cll;
    }
    public void setRent(double rent){
        setVar("rent",rent);
    }
    public double getRent(){
        if(!varExists("rent")){
            setVar("rent", 0D);
        }
        try {
            return (double) getVar("rent");
        } catch (Exception e) {
            setVar("rent", 0D);
            return 0D;
        }
    }

    public double getTotalRentCollected() {
        return (getRent() * getPayingClaims().size());
    }
    public boolean isTownClaim(Claim c) {
        if (townClaims.contains(c)) return true;
        return false;
    }
    public void regClaim(Chunk c) {
        if (!TownS.g().isClaimed(c)) return;
        if (TownS.g().getClaim(c).getTown() != this) return;
        if(!townClaims.contains(TownS.g().getClaim(c)))townClaims.add(TownS.g().getClaim(c));
    }

    public void unregClaim(Claim c) {
        townClaims.remove(c);
    }

    public void unregClaim(Chunk c) {
        if (!TownS.g().isClaimed(c)) return;
        Claim claim = TownS.g().getClaim(c);
        if(this.townClaims.contains(claim)){
            this.townClaims.remove(claim);
        }
    }

    public String getName() {
        return town_name;
    }

    public OfflinePlayer getMayor() {
        return Bukkit.getOfflinePlayer(Mayor_ID);
    }

    public void setRank(OfflinePlayer p, Rank rank) {
        if(rank == null){
            rankMap.remove(p.getUniqueId());
            return;
        }
        rankMap.put(p.getUniqueId(), rank);
    }

    public void setName(String name) {
        town_name = name;
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

    public boolean setOwner(Player player) {
        if (TownS.g().hasTown(player)) {
            if (TownS.g().getTown(player).equals(this)) {
                Mayor_ID = player.getUniqueId();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean setOwner(UUID id) {
        if (TownS.g().hasTown(id)) {
            if (TownS.g().getTown(id).equals(this)) {
                Mayor_ID = id;
                System.out.println("success mayor set");
                return true;
            } else {
                System.out.println("fail not same town mayor set");
                return false;
            }
        } else {
            System.out.println("fail mayor set has no town");
            return false;
        }
    }

    public boolean hasPermission(String perm, OfflinePlayer player) {
        if (!hasRank(player.getUniqueId())){
            if(getMayor().getUniqueId() == player.getUniqueId()){
                return true;
            }
            return false;
        }
        return getRank(player.getUniqueId()).hasPermission(perm) || getMayor() == player;
    }
    public boolean hasPermissionStrict(String perm, OfflinePlayer player) {
        if (!hasRank(player.getUniqueId())){

            return false;
        }
        return getRank(player.getUniqueId()).hasPermission(perm);
    }
    public boolean hasPermission(String perm,UUID id){
        if(!hasRank(id)){
            if(getMayor().getUniqueId() == id){
                return true;
            }
            return false;
        }
        return getRank(id).hasPermission(perm) || getMayor().getUniqueId() == id;
    }

    public void claim(Chunk chunk, Player owner) {

        regClaim(new Claim(chunk, this, owner.getUniqueId()));
        if(RegenSaveQueueManager.get.isCached(chunk))return;
        if(RegenSaveQueueManager.get.isQueued(chunk))return;

        RegenSaveQueueManager.get.addToQueue(chunk);
    }

    public boolean setSpawnChunk(Town town, Chunk chunk) {
        if (TownS.g().isClaimed(chunk)) {
            Claim claim = TownS.g().getClaim(chunk);
            if (claim.getTown().equals(town)) {
                town.spawnChunkID = TownS.getChunkID(chunk);
            }
        }
        return false;
    }

    public boolean isSpawnChunk(Chunk chunk) {
        if (spawnChunkID == null || chunk == null)
            return false;
        return TownS.getChunkID(chunk).equals(spawnChunkID);
    }

    public void resetClaim(Claim claim){
        if(this.townClaims.contains(claim)){
            Chunk chunk = claim.getChunk();
            new BukkitRunnable() {
                @Override
                public void run() {
                    new RegenBuilder((Material[][][]) LoadManager.get.loadObject("ChunkSaves", chunk.getX() + "TT" + chunk.getZ() + "TT" + chunk.getWorld().getName() + ".dat"), chunk);
                }
            }.runTask(TownS.g());
        }
    }

    public void unclaim(Chunk chunk) {
        TownS.g().rCfT(TownS.g().getClaim(chunk));
        unregClaim(chunk);
        new BukkitRunnable() {
            @Override
            public void run() {
                new RegenBuilder((Material[][][]) LoadManager.get.loadObject("ChunkSaves", chunk.getX() + "TT" + chunk.getZ() + "TT" + chunk.getWorld().getName() + ".dat"), chunk);
            }
        }.runTask(TownS.g());
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

    public void deleteWarpPoint(String spawn_name) {
        warpPointMap.remove(spawn_name);
    }

    public void setWarpPoint(Town town, String spawn_name, Location location) {
        spawn_name = spawn_name.toLowerCase();
        if (TownS.g().isClaimed(location.getChunk())) {
            Claim claim = TownS.g().getClaim(location.getChunk());
            if (claim != null && claim.getTown().equals(town)) {
                town.warpPointMap.put(spawn_name, new WarpLocation(location));
                return;
            }
        }
        Bukkit.getConsoleSender().sendMessage("[TOWNS] Failed to set warp point: " + spawn_name + " Town: " + town.getName());
    }

    public Set<String> getTownWarpKeys() {
        return warpPointMap.keySet();
    }

    public Location getWarpPoint(Town town, String spawn_name) {
        spawn_name = spawn_name.toLowerCase();
        if (warpPointMap.get(spawn_name) != null) {
            Location warp_location = warpPointMap.get(spawn_name).getLocation();
            if (TownS.g().isClaimed(warp_location.getChunk())) {
                Town town_at_location = TownS.g().getTown(warp_location.getChunk());
                if (town_at_location.equals(town)) {
                    return warpPointMap.get(spawn_name).getLocation();
                } else {
                    warpPointMap.remove(spawn_name);
                    return null;
                }
            } else {
                warpPointMap.remove(spawn_name);
                return null;
            }
        }
        return null;
    }

    public UUID getTownUUID() {
        return town_uuid;
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
        townClaims.clear();
    }

    public boolean addMember(Player p) {
        if (TownS.g().getTown(p) != null) return false;
        TownS.g().quickPlayer.put(p.getUniqueId(), this);
        TownS.g().getTownPlayer(p).silentUnvite(this);
        if (!Members.contains(p.getUniqueId())) this.Members.add(p.getUniqueId());
        if(!rankMap.keySet().contains(p.getUniqueId()) && getMayor().getUniqueId() != p.getUniqueId())setRank(p,TownS.g().getRank("Default"));
        return true;
    }
    public boolean addMember(UUID id){
        if (TownS.g().getTown(id) != null) return false;
        TownS.g().quickPlayer.put(id, this);
        if (!Members.contains(id)) this.Members.add(id);

        return true;
    }
    public boolean removePlayer(OfflinePlayer p) {
        if (!belongs(p.getUniqueId())) return false;

        if (TownS.g().quickPlayer.containsKey(p.getUniqueId())) {
            TownS.g().quickPlayer.remove(p.getUniqueId());
        }
        this.Members.remove(p.getUniqueId());
        this.rankMap.remove(p.getUniqueId());
        return true;
    }

    public boolean belongs(OfflinePlayer player) {
        if (Members.contains(player.getUniqueId()) || rankMap.containsKey(player.getUniqueId()) || Mayor_ID == player.getUniqueId())
            return true;
        return false;
    }

    public boolean belongs(UUID id) {
        if (Members.contains(id) || rankMap.containsKey(id) || Mayor_ID == id)
            return true;
        return false;
    }

    public Town(String name, Player mayor) {
        town_name = name;
        town_balance = 0.0;
        town_uuid = UUID.randomUUID();
        Mayor_ID = mayor.getUniqueId();
        addMember(Bukkit.getPlayer(Mayor_ID));
        TownS.g().aTtM(this);
    }

    public Town(String name, Player mayor, List<UUID> MemberL, HashMap<UUID, Rank> Rank) {
        town_name = name;
        town_balance = 0.0;
        town_uuid = UUID.randomUUID();
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
        addMember(Bukkit.getPlayer(Mayor_ID));
        this.rankMap = Rank;
        //Assistants = AssistantL;
        TownS.g().aTtM(this);
    }

    public Town(String name, Player mayor, List<UUID> MemberL, List<UUID> AssistantL, List<Claim> claims) {
        town_name = name;
        town_balance = 0.0;
        town_uuid = UUID.randomUUID();
        Mayor_ID = mayor.getUniqueId();
        Members = MemberL;
        addMember(Bukkit.getPlayer(Mayor_ID));

        // Assistants = AssistantL;

        for (Claim c : claims) {
            TownS.g().aCtT(c);
            this.townClaims = claims;
        }
        TownS.g().aTtM(this);
    }

    /* Not to be used directly */
    public void setTownBalance(Double value) {
        this.town_balance = value;
    }

    public Double getTownBalance() {
        return this.town_balance;
    }

    public void changeTownBalanceBy(Double amount) {
        this.town_balance += amount;
    }
    /* Not to be used directly */

}
