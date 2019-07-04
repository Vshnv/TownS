package com.rocketmail.vaishnavanil.towns.Configurations;

import com.rocketmail.vaishnavanil.towns.Listeners.Constraints;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Rank;
import com.rocketmail.vaishnavanil.towns.Towns.RegenBuilder;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum ConfigManager {
    get;
    List<EntityType> DAimFlag = new ArrayList<>();
    List<String> regenWorldUnclaim = new ArrayList<>();
private void setUP(){
        TownS.g().getConfig().options().copyDefaults(true);
        TownS.g().getConfig().addDefault("Constraints.Containers", Arrays.asList("Chest","Crafting_table","BARREL","FURNACE","COMPOSTER","SHULKER_BOX","HOPPER_MINECART","FURNACE_MINECART","CHEST_MINECART","DROPPER","HOPPER","DISPENSER","FURNACE","SMOKER","JUKEBOX"));
        TownS.g().getConfig().addDefault("Constraints.Usables",Arrays.asList("Button","Lever","JUKEBOX","NOTE_BLOCK","BEACON","DOOR","COMPARATOR","REPEATER","FENCE_GATE","TRAPDOOR","MINECART","CARROT_STICK","DAYLIGHT_DETECTOR","STONECUTTER","SMITHING_TABLE","FLETCHING_TABLE","LOOM","LECTERN","WORKBENCH","COMPOSTER","CARTOGRAPHY_TABLE","BELL","FLINT_AND_STEEL","BUCKET","STORAGE_MINECART","INK_SACK","ENDER_PEARL","BOTTLE","FIREBALL","ARMOR_STAND","SKULL_ITEM","BOAT","END_CRYSTAL","DRAGON_EGG","ITEM_FRAME"));
        TownS.g().getConfig().addDefault("Constraints.NoAllow",Arrays.asList("Obsidian","Bedrock"));

        TownS.g().getConfig().addDefault("FlagConfig.Mob.Disallow",Arrays.asList(EntityType.SKELETON.name(),EntityType.ZOMBIE.name(),EntityType.PHANTOM.name(),EntityType.BLAZE.name(),EntityType.CREEPER.name(),EntityType.ENDERMAN.name(),EntityType.ENDERMITE.name()));
        TownS.g().getConfig().addDefault("RanksOrder.Assistant",1);
        TownS.g().getConfig().addDefault("RanksOrder.MVP",1);
        TownS.g().getConfig().addDefault("Ranks.Assistant",Arrays.asList("Claim","Unclaim","BuildALL","FS","NFS","ContainerALL","FlagSetALL"));
        TownS.g().getConfig().addDefault("Ranks.MVP",Arrays.asList("Change","This","Later","lel"));
        TownS.g().getConfig().addDefault("Ranks.Default",Arrays.asList("Warp","Deposit","Spawn","PlotClaim","PlotBorder","PlotAllow","PlotDisAllow","PlotAccess","PlotFlags","Plotsetname"));
        TownS.g().getConfig().addDefault("RegenUnclaim.worlds",Arrays.asList("world"));
        TownS.g().getConfig().addDefault("LastBackupTimeMS",0L);

    saveConfig();
    }

    public void saveConfig(){
        TownS.g().saveConfig();
    }
    public void loadDisaalowedMobs(){
    List<String> sLiT = (List<String>) TownS.g().getConfig().get("FlagConfig.Mob.Disallow");
    for(String s:sLiT){
        DAimFlag.add(EntityType.fromName(s));
    }
    }

    public boolean shouldBackUP(){
        long lastBackup = TownS.g().getConfig().getLong("LastBackupTimeMS");
        if(lastBackup == 0)        {
            TownS.g().getConfig().set("LastBackupTimeMS",System.currentTimeMillis());
            return false;
        }

        if(System.currentTimeMillis()-lastBackup >= 86400000L){
            TownS.g().getConfig().set("LastBackupTimeMS",System.currentTimeMillis());
            return true;
        }else{
            return false;
        }
    }
    public boolean nearBackUP() {
        long lastBackup = TownS.g().getConfig().getLong("LastBackupTimeMS");
        if (lastBackup == 0) return false;
        return System.currentTimeMillis()-lastBackup+3600000 >= 86400000L;
    }

    public boolean isAllowed(EntityType type){
       return !DAimFlag.contains(type);

    }
    public void reload(){
        setUP();
        TownS.g().reloadConfig();
        setupConstraints();
        setupranks();
        loadDisaalowedMobs();
        regenWorldUnclaim = TownS.g().getConfig().getStringList("RegenUnclaim.worlds");
    }
    public void LoadUp(){
        setUP();
        setupConstraints();
        setupranks();
        loadDisaalowedMobs();
        regenWorldUnclaim = TownS.g().getConfig().getStringList("RegenUnclaim.worlds");
    }
    public boolean isRegenWorld(World world){
        return regenWorldUnclaim.contains(world.getName());
    }
    private void setupConstraints(){
        List<String> ContConst = TownS.g().getConfig().getStringList("Constraints.Containers");
        List<String> UseConst = TownS.g().getConfig().getStringList("Constraints.Usables");
        List<String> Donts = TownS.g().getConfig().getStringList("Constraints.NoAllow");

        Constraints.Container.setRestrictions(ContConst);
        Constraints.Usable.setRestrictions(UseConst);
        Constraints.DONT_USE.setRestrictions(Donts);
    }
    private void setupranks(){
        Set<String> RanksListed = TownS.g().getConfig().getConfigurationSection("Ranks").getKeys(true);
       for(String sRank:RanksListed){
           List<String> perms = TownS.g().getConfig().getStringList("Ranks."+sRank);
           int lvl = TownS.g().getConfig().getInt("RanksOrder."+sRank);
           new Rank(sRank,lvl,perms);
       }
    }
}
