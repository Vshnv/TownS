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
        TownS.g().getConfig().addDefault("Constraints.Containers", Arrays.asList("Chest","Crafting_table"));
        TownS.g().getConfig().addDefault("Constraints.Usables",Arrays.asList("Button","Lever"));
        TownS.g().getConfig().addDefault("Constraints.NoAllow",Arrays.asList("Obsidian","Bedrock"));

        TownS.g().getConfig().addDefault("FlagConfig.Mob.Disallow",Arrays.asList(EntityType.SKELETON.name(),EntityType.ZOMBIE.name()));
        TownS.g().getConfig().addDefault("Ranks.Assistant.order",1);
        TownS.g().getConfig().addDefault("Ranks.MVP.order",1);
        TownS.g().getConfig().addDefault("Ranks.Assistant",Arrays.asList("Claim","Unclaim","BuildALL","FS","NFS","ContainerALL","FlagSetALL"));
        TownS.g().getConfig().addDefault("Ranks.MVP",Arrays.asList("Change","This","Later","lel"));

        TownS.g().getConfig().addDefault("RegenUnclaim.worlds",Arrays.asList("world","world_nether"));


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
           int lvl = TownS.g().getConfig().getInt("Ranks."+sRank+".order");
           new Rank(sRank,lvl,perms);
       }
    }
}
