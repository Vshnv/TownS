package com.rocketmail.vaishnavanil.towns.Configurations;

import com.rocketmail.vaishnavanil.towns.Listeners.Constraints;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Rank;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ConfigManager {
    get;
    List<EntityType> DAimFlag = new ArrayList<>();
private void setUP(){
        TownS.g().getConfig().options().copyDefaults(true);
        TownS.g().getConfig().addDefault("Constraints.Containers", Arrays.asList("Chest","Crafting_table"));
        TownS.g().getConfig().addDefault("Constraints.Usables",Arrays.asList("Button","Lever"));
        TownS.g().getConfig().addDefault("Constraints.NoAllow",Arrays.asList("Obsidian","Bedrock"));

        TownS.g().getConfig().addDefault("FlagConfig.Mob.Disallow",Arrays.asList(EntityType.SKELETON,EntityType.ZOMBIE));

        TownS.g().getConfig().addDefault("Ranks.Assistant",Arrays.asList("Claim","Unclaim","BuildALL","FS","NFS","ContainerALL","FlagSetALL"));
        TownS.g().getConfig().addDefault("Ranks.MVP",Arrays.asList("Change","This","Later","lel"));

        saveConfig();
    }

    public void saveConfig(){
        TownS.g().saveConfig();
    }
    public void loadDisaalowedMobs(){
    this.DAimFlag = (List<EntityType>) TownS.g().getConfig().get("FlagConfig.Mob.Disallow");
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
    }
    public void LoadUp(){
    setUP();
    setupConstraints();
    setupranks();
    loadDisaalowedMobs();
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
        List<String> RanksListed = TownS.g().getConfig().getStringList("Ranks");
       for(String sRank:RanksListed){
           List<String> perms = TownS.g().getConfig().getStringList("Ranks."+sRank);
           new Rank(sRank,perms);
       }
    }
}
