package com.rocketmail.vaishnavanil.towns;

import com.rocketmail.vaishnavanil.towns.Commands.PlotCmd;
import com.rocketmail.vaishnavanil.towns.Commands.TownAdmin;
import com.rocketmail.vaishnavanil.towns.Commands.TownChatCmd;
import com.rocketmail.vaishnavanil.towns.Commands.TownCmd;
import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.GUI.FunctionRunner;
import com.rocketmail.vaishnavanil.towns.Listeners.*;
import com.rocketmail.vaishnavanil.towns.Listeners.FlagManagers.*;
import com.rocketmail.vaishnavanil.towns.Listeners.TitleManager.MoveEventListener;
import com.rocketmail.vaishnavanil.towns.MapGUI.InvClickListen;
import com.rocketmail.vaishnavanil.towns.Towns.*;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import com.rocketmail.vaishnavanil.towns.Utilities.PlotBorderShowTimer;
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

public final class TownS extends JavaPlugin {
    //SINGLETON
    public static String PREFIX = "[TownS]";
    public static Double TownCost = 200.0;
    public static Double PlotCost = 1000.0;
    //SINGLETON
    private static TownS instance;
    private static Economy econ = null;
    private List<BukkitTask> asyncTasks = new ArrayList<>();
    public HashMap<UUID, Town> quickPlayer = new HashMap<>(); /*P-T Map*/
    public RegenBuilder Cur;
    //MAPPING
    private HashMap<String, Town> TM = new HashMap<>(); /*TOWN MAP*/
    private HashMap<String, Claim> Map = new HashMap<>();/*CLAIM MAP*/  //FORMAT :: KEY ->  ChunkX::ChunkZ::WORLD
    private HashMap<UUID, TownPlayer> townPlayerMap = new HashMap<>();
    private HashMap<String, Rank> RankList = new HashMap<>();
    private Queue<RegenBuilder> RegenWorkers = new LinkedList<>();
    public void regAsync(BukkitTask tk){
        if(!asyncTasks.contains(tk))asyncTasks.add(tk);
    }
    public static TownS g() {
        return instance;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public void addTownPlayer(Player player) {
        townPlayerMap.put(player.getUniqueId(), new TownPlayer(player));
    }

    public void removeTownPlayer(Player player) {
        townPlayerMap.remove(player.getUniqueId());
    }

    public TownPlayer getTownPlayer(Player player) {
        if (townPlayerMap.get(player.getUniqueId()) == null)
            addTownPlayer(player);
        return townPlayerMap.get(player.getUniqueId());
    }

    public void registerRegenBuilder(RegenBuilder builder) {
        for (RegenBuilder b : RegenWorkers) {
            if (b.getChunk() == builder.getChunk()) {
                return;
            }
        }
        RegenWorkers.add(builder);
    }

    public void alertQueue() {
        out.println("[TownS-Regenerator]Started Regenerator Queue!");
        regAsync(new BukkitRunnable() {

            @Override
            public void run() {
                if (Cur != null) return;
                if (RegenWorkers.peek() == null) return;

                if (Cur == null) {

                    out.println("[TownS-Regenerator]Queue moved to next Region!");

                    Cur = RegenWorkers.poll();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            out.println("[TownS-Regenerator]Started next Chunk Regen!");

                            Cur.Build();


                            this.cancel();
                        }
                    }.runTask(TownS.g());
                    return;
                }
                return;
            }
        }.runTaskTimerAsynchronously(TownS.g(), 0, 20));
    }

    public void finishRegenWork(RegenBuilder builder) {
        if (Cur == builder) Cur = null;
    }

    public Queue<RegenBuilder> getActiveRegenerators() {
        return RegenWorkers;
    }

    public void aCtT(Claim claim) { /*ADD CLAIM TO CLAIM MAP*/
        Map.put(claim.x() + "::" + claim.z() + "::" + claim.getWorldName(), claim);
    }

    public void rCfT(Claim claim) { /*REMOVE CLAIM FROM CLAIM MAP*/
        Map.remove(claim.x() + "::" + claim.z() + "::" + claim.getWorldName());
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
    public boolean isNameUsed(String townname){
        if(this.getTown(townname) == null)return false;
        return true;
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
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new RegenBuilder((Material[][][]) LoadManager.get.loadObject("ChunkSaves", chunk.getX() + "TT" + chunk.getZ() + "TT" + chunk.getWorld().getName() + ".dat"), chunk);

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
            if(TownS.g().hasTown(claim.getOwner())){
                if(claim.getTown() != TownS.g().getTown(claim.getOwner())){
                    claim.setOwner(claim.getTown().getMayor().getUniqueId());
                }
            }else{
                claim.setOwner(claim.getTown().getMayor().getUniqueId());

            }
        }

        return Map.containsKey(CX + "::" + CZ + "::" + wName);
    }

    public Claim getClaim(Chunk chunk) {
        return Map.get(chunk.getX() + "::" + chunk.getZ() + "::" + chunk.getWorld().getName());
    }

    public boolean townExists(String town_uuid) {
        return TM.keySet().contains(town_uuid);
    }

    @Deprecated
    public Town getTown(String town_name) {
        for (Town town : TM.values()) {
            if (town.getName().equals(town_name)) {
                return town;
            }
        }
        return null;
    }

    public Town getTown(UUID town_uuid) {
        return TM.get(town_uuid.toString());
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
        TM.put(town.getTownUUID().toString(), town);
    }

    public void/*ADD TOWN FROM MAP*/ rTfM(Town town) {
        TM.remove(town.getTownUUID().toString());
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
//MAPPING

    //ENABLE DISABLE
    @Override
    public void onEnable() {

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(getDescription().getName() + " - Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        ConfigManager.get.LoadUp();


        registerCMD("towns", new TownCmd());
        registerCMD("plot", new PlotCmd());
        registerCMD("tc", new TownChatCmd());
        registerCMD("ta", new TownAdmin());
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
        regListen(ChunkLoadListener.get());
        regListen(new RegenChunkInteractEvent());
        regListen(new PlayerJoinQuitListener());
        regListen(new PlayerChatListener());
        regListen(FunctionRunner.get());
        ChunkLoadListener.get().runSaveQueue();
        PlotBorderShowTimer.INSTANCE.startBorderShow();
        ConfigManager.get.LoadUp();

        MobClearLoop.get.start();

        RegenBuilder.ContinueRegens();
    }


    public void RegisterRanks(Rank rank) {
        RankList.put(rank.getName(), rank);
    }

    @Override
    public void onDisable() {
        if(!asyncTasks.isEmpty()){
            for(BukkitTask tk:asyncTasks){
                tk.cancel();
            }
        }
        // Plugin shutdown logic
    }

    //ENABLE DISABLE


}
