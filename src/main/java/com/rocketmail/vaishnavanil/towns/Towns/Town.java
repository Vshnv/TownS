package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Town {
    private String town_name;
    private UUID town_uuid;
    private UUID Mayor_ID;
    private Double town_balance;
    private HashMap<UUID, Rank> rankMap = new HashMap<>();
    private HashMap<String, Location> warpPointMap = new HashMap<>();
    private List<UUID> Members = new ArrayList<>();
    private List<Town> Allies = new ArrayList<>();
    private List<Town> Enemies = new ArrayList<>();
    private String spawnChunkID;
    private List<Claim> townClaims = new ArrayList<>();

    public void regClaim(Claim c) {
        if (townClaims.contains(c)) return;
        if (c.getTown() != this) {
            townClaims.remove(c);
            return;
        }

        townClaims.add(c);
    }

    public List<Claim> getClaims() {
        return townClaims;
    }

    public boolean isTownClaim(Claim c) {
        if (townClaims.contains(c)) return true;
        return false;
    }

    public void regClaim(Chunk c) {
        if (!TownS.g().isClaimed(c)) return;
        if (TownS.g().getClaim(c).getTown() != this) return;
        townClaims.add(TownS.g().getClaim(c));
    }

    public void unregClaim(Claim c) {
        townClaims.remove(c);
    }

    public void unregClaim(Chunk c) {
        if (!TownS.g().isClaimed(c)) return;
        townClaims.remove(TownS.g().getClaim(c));
    }

    public String getName() {
        return town_name;
    }

    public OfflinePlayer getMayor() {
        return Bukkit.getOfflinePlayer(Mayor_ID);
    }

    public void setRank(Player p, Rank rank) {
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
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean hasPermission(String perm, Player player) {
        if (!hasRank(player)){
            if(getMayor().getUniqueId() == player.getUniqueId()){
                return true;
            }
            return false;
        }
        return getRank(player).hasPermission(perm) || getMayor() == player;
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
        return chunk.equals(TownS.getChunkFromID(spawnChunkID));
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
                town.warpPointMap.put(spawn_name, location);
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
            Location warp_location = warpPointMap.get(spawn_name);
            if (TownS.g().isClaimed(warp_location.getChunk())) {
                Town town_at_location = TownS.g().getTown(warp_location.getChunk());
                if (town_at_location.equals(town)) {
                    return warpPointMap.get(spawn_name);
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
