package com.rocketmail.vaishnavanil.towns;

import com.rocketmail.vaishnavanil.towns.Commands.PlotCmd;
import com.rocketmail.vaishnavanil.towns.Commands.TownCmd;
import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.Economy.EconomyHandler;
import com.rocketmail.vaishnavanil.towns.Listeners.*;
import com.rocketmail.vaishnavanil.towns.Listeners.MobClearLoop;
import com.rocketmail.vaishnavanil.towns.Listeners.TownRestricter;
import com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers.*;
import com.rocketmail.vaishnavanil.towns.Listeners.TitleManager.MoveEventListener;
import com.rocketmail.vaishnavanil.towns.MapGUI.InvClickListen;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Rank;
import com.rocketmail.vaishnavanil.towns.Towns.RegenBuilder;
import com.rocketmail.vaishnavanil.towns.Towns.Town;

import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static java.lang.System.out;
import java.util.HashMap;
import java.util.UUID;
public final class TownS extends JavaPlugin {
    //SINGLETON
    private static TownS instance;

    public static TownS g() {
        return instance;
    }

    //SINGLETON
    public static String PREFIX = "[TownS]";
    //MAPPING
    //Private/*CLAIM MAP*/ HashMap<Claim,Town> CM = new HashMap<>();
    private/*TOWN MAP*/ HashMap<String, Town> TM = new HashMap<>();

    private/*CLAIM MAP*/ HashMap<String, Claim> Map = new HashMap<>();//FORMAT :: KEY ->  ChunkX::ChunkZ::WORLD

    private/*P-T Map*/ HashMap<UUID, Town> quickPlayer = new HashMap<>();

    private Set<RegenBuilder> RegenWorkers = new HashSet<>();

    private HashMap<String, Rank> RankList = new HashMap<>();
    public void registerRegenBuilder(RegenBuilder builder){
        RegenWorkers.add(builder);
    }
    public void finishRegenWork(RegenBuilder builder){
        RegenWorkers.remove(builder);
    }
    public Set<RegenBuilder> getActiveRegenerators(){
        return RegenWorkers;
    }

    private static Economy econ = null;


    public void/*ADD CLAIM TO CLAIM MAP*/ aCtT(Claim claim) {
        // CM.put(claim,town);
        Map.put(claim.x() + "::" + claim.z() + "::" + claim.getWorldName(), claim);
    }

    public void/*REMOVE CLAIM FROM CLAIM MAP*/rCfT(Claim claim) {
        Map.remove(claim.x() + "::" + claim.z() + "::" + claim.getWorldName());
        //CM.remove(claim);
    }

    public Town getTown(Player p) {
        if (quickPlayer.containsKey(p.getUniqueId())) return quickPlayer.get(p.getUniqueId());
        //ADD TO QUICK LOOKUP
        for (Town m : TM.values()) {
            if (m.belongs(p)) {
                quickPlayer.put(p.getUniqueId(), m);
                return m;
            }
        }
        return null;
    }

    public boolean hasTown(Player player) {
        try {
            return getTown(player) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void rPfT(Player player) {
        quickPlayer.remove(player.getUniqueId());
    }

    public void rPfT(UUID id) {
        quickPlayer.remove(id);
    }

    public boolean isClaimed(Chunk chunk) {
        return isClaimed(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }

    public boolean isClaimed(int CX, int CZ, String wName) {
        if (Map.containsKey(CX + "::" + CZ + "::" + wName)) {
            Claim claim = Map.get(CX + "::" + CZ + "::" + wName);
            try {
                if (claim.getTown() == null) {

                    Chunk chunk = claim.getChunk();
                    Map.remove(CX + "::" + CZ + "::" + wName);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            new RegenBuilder((Material[][][]) LoadManager.get.loadObject("ChunkSaves",chunk.getX()+"TT"+chunk.getZ()+"TT"+chunk.getWorld().getName()+".dat"),chunk).Build();

                        }
                    }.runTask(this);

                    return false;
                }
            } catch (Exception e) {
                Chunk chunk = claim.getChunk();
                Map.remove(CX + "::" + CZ + "::" + wName);
                Map.remove(CX + "::" + CZ + "::" + wName);
                return false;
            }
        }
        return Map.containsKey(CX + "::" + CZ + "::" + wName);
    }

    public Claim getClaim(Chunk chunk) {
        return Map.get(chunk.getX() + "::" + chunk.getZ() + "::" + chunk.getWorld().getName());
    }

    public boolean townExists(String name) {
        return TM.keySet().contains(name);
    }

    public Town getTown(String name) {
        if (TM.containsKey(name)) return TM.get(name);
        return null;
    }

    public Town getTown(Chunk chunk) {
        return getClaim(chunk).getTown();
    }

    //
    public void registerCMD(String cmd, CommandExecutor e) {
        this.getServer().getPluginCommand(cmd).setExecutor(e);
    }

    public void regListen(Listener lis) {
        getServer().getPluginManager().registerEvents(lis, this);
    }

    //
    public void/*ADD TOWN TO MAP*/ aTtM(Town town) {
        TM.put(town.getName(), town);
    }

    public void/*ADD TOWN FROM MAP*/ rTfM(Town town) {
        TM.remove(town.getName());
    }

    //
    public Rank getRank(String name) {
        return RankList.get(name);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
//MAPPING


    //ENABLE DISABLE
    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            Bukkit.getConsoleSender().sendMessage(getDescription().getName() + " - Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        ConfigManager.get.LoadUp();


        registerCMD("towns", new TownCmd());
        registerCMD("plot", new PlotCmd());
        regListen(new InvClickListen());
        regListen(new MoveEventListener());
        regListen(new ExplodeEventListener());
        regListen(new FireProtectionListener());
        regListen(new ContainerUseEventListener());
        regListen(new MobManagingEventListener());
        regListen(new PVPEventListener());
        regListen(new UtilityUSEEventListener());
        regListen(new TownRestricter());
        regListen(new BlockPhysics());
        regListen(new ChunkLoadListener());
        regListen(new RegenChunkInteractEvent());
        ConfigManager.get.LoadUp();

        MobClearLoop.get.start();
        RegenBuilder.ContinueRegens();
    }


    public void RegisterRanks(Rank rank) {
        RankList.put(rank.getName(), rank);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    //ENABLE DISABLE


}
