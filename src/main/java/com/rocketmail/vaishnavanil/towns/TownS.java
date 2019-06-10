package com.rocketmail.vaishnavanil.towns;

import com.rocketmail.vaishnavanil.towns.Commands.AutoComplete.PlotCmdCompleter;
import com.rocketmail.vaishnavanil.towns.Commands.AutoComplete.TownAdminCompleter;
import com.rocketmail.vaishnavanil.towns.Commands.AutoComplete.TownCmdCompleter;
import com.rocketmail.vaishnavanil.towns.Commands.AutoComplete.WildernessInteractListener;
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
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemCreation.FakeEnchant;
import com.rocketmail.vaishnavanil.towns.Placeholders.PlaceholderProvider;
import com.rocketmail.vaishnavanil.towns.Storage.LoadObject;
import com.rocketmail.vaishnavanil.towns.Storage.SaveObject;
import com.rocketmail.vaishnavanil.towns.Towns.*;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import com.rocketmail.vaishnavanil.towns.Utilities.PlotBorderShowTimer;
import com.rocketmail.vaishnavanil.towns.Utilities.SaveManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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

    public Town getTown(OfflinePlayer p) {
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
    public boolean hasTown(OfflinePlayer player) {
        try {
            return getTown(player) != null;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean hasTown(UUID id) {
        try {
            return getTown(id) != null;
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

    public boolean isClaimedQuick(int CX, int CZ, String wName) {
        return  (Map.containsKey(CX + "::" + CZ + "::" + wName));
    }
    public boolean isClaimed(String ClaimID){
        String[] split = ClaimID.split("::");
        int CX = Integer.parseInt(split[0]);
        int CZ = Integer.parseInt(split[1]);
        String w = split[2];
        return isClaimed(CX,CZ,w);
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

    public Set<String> getAlLTownUUID(){ return TM.keySet(); }

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

    public void registerCompleter(String cmd, TabCompleter exec){
        this.getCommand(cmd).setTabCompleter(exec);
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
        instance = this;
        registerFE();
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(getDescription().getName() + " - Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        if(new File(getDataFolder().getPath()+"\\Data","towns.dat").exists()) {
            TM = (HashMap<String, Town>) LoadObject.LoadObject(getDataFolder().getPath() + "\\Data", "towns.dat");
        }
        if(new File(getDataFolder().getPath()+"\\Data","claims.dat").exists()) {
            Map = (HashMap<String, Claim>) LoadObject.LoadObject(getDataFolder().getPath() + "\\Data", "claims.dat");
        }
        ConfigManager.get.LoadUp();


        registerCMD("towns", new TownCmd());
        registerCompleter("towns", new TownCmdCompleter());


        registerCMD("plot", new PlotCmd());
        registerCompleter("plot", new PlotCmdCompleter());

        registerCMD("tc", new TownChatCmd());

        registerCMD("ta", new TownAdmin());
        registerCompleter("ta", new TownAdminCompleter());

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
        regListen(new InventoryCloseListener());
        regListen(new WildernessInteractListener());
        regListen(new PlayerRespawnListener());

        regListen(FunctionRunner.get());
        hookPlaceholderAPI();
        ChunkLoadListener.get().runSaveQueue();
        PlotBorderShowTimer.INSTANCE.startBorderShow();

        MobClearLoop.get.start();

        RegenBuilder.ContinueRegens();

    }

    private void hookPlaceholderAPI() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderProvider().register();
            getServer().getConsoleSender().sendMessage("[TOWNS] [✔] Hooked into PlaceholderAPI");
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.RED+"[TOWNS] PlaceholderAPI Hook Failed");
        }
    }
    public void registerFE() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FakeEnchant FE = new FakeEnchant(new NamespacedKey(this,"TownSFakeEnchant"));
            Enchantment.registerEnchantment(FE);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static String getChunkID(Chunk c){
        return c.getX()+"::"+c.getZ()+"::"+c.getWorld();
    }
    public static Chunk getChunkFromID(String ID){
        String[] sep = ID.split("::");
        int x = Integer.parseInt(sep[0]);
        int z = Integer.parseInt(sep[1]);
        World world = Bukkit.getWorld(sep[2]);
        return world.getChunkAt(x,z);
    }

    public void RegisterRanks(Rank rank) {

        RankList.put(rank.getName(), rank);
    }
    public void saveTown(){
        File f = new File(getDataFolder().getPath() + "\\Data", "towns.dat");
        if(f.exists()){
            SaveObject.SaveObject(TM,getDataFolder().getPath() + "\\Data", "towns.dat");
        }else{
            try {
                f.createNewFile();
                SaveObject.SaveObject(TM,getDataFolder().getPath() + "\\Data", "towns.dat");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void saveClaims(){
        File f = new File(getDataFolder().getPath() + "\\Data", "claims.dat");
        if(f.exists()){
            SaveObject.SaveObject(Map,getDataFolder().getPath() + "\\Data", "claims.dat");
        }else{
            try {
                f.createNewFile();
                SaveObject.SaveObject(Map,getDataFolder().getPath() + "\\Data", "claims.dat");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Set<String> getRanks(){ return RankList.keySet(); }

    @Override
    public void onDisable() {
        if(!asyncTasks.isEmpty()){
            for(BukkitTask tk:asyncTasks){
                tk.cancel();
            }
        }
        //DATA SAVE
        if(!TM.keySet().isEmpty()){
            SaveObject.SaveObject(TM,getDataFolder().getPath() + "\\Data", "towns.dat");
        }else{
            File f = new File(getDataFolder().getPath() + "\\Data", "towns.dat");
            if(f.exists()){
                SaveObject.SaveObject(TM,getDataFolder().getPath() + "\\Data", "towns.dat");
            }
        }
        if(!Map.keySet().isEmpty()){
            SaveObject.SaveObject(Map,getDataFolder().getPath() + "\\Data", "claims.dat");

        }else{
            File f = new File(getDataFolder().getPath() + "\\Data", "claims.dat");
            if(f.exists()){
                SaveObject.SaveObject(Map,getDataFolder().getPath() + "\\Data", "claims.dat");
            }
        }

    }

    //ENABLE DISABLE


}
