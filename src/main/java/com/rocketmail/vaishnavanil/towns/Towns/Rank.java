package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Rank implements Serializable {
    public static final long serialVerisionUID = 9458L;

    private String name;
    private int HierLevel;
    private Collection<String> permissions = new HashSet<>();

    public Rank(String rank,int hierarchiel,Collection<String> perms){
        name = rank;
        for(String s:perms){
            permissions.add(s.toLowerCase());
        }
        HierLevel = hierarchiel;
        TownS.g().RegisterRanks(this);
    }

    public int getLevel(){
        return  HierLevel;
    }

    public boolean isHigherThan(Rank rank){
        return getLevel() > rank.getLevel();
    }
    public String getName(){
        return name;
    }
    public boolean hasPermission(String perm){
        return permissions.contains(perm.toLowerCase());
    }

    public void addPerm(String perm){
        if(permissions.contains(perm))return;
        permissions.add(perm.toLowerCase());
        TownS.g().getConfig().set("Ranks."+name,permissions);
        TownS.g().saveConfig();
    }

    public void removePerm(String perm){
        permissions.remove(perm.toLowerCase());
        TownS.g().getConfig().set("Ranks."+name,permissions);
        TownS.g().saveConfig();
    }


}
