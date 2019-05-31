package com.rocketmail.vaishnavanil.towns.Towns;

import com.rocketmail.vaishnavanil.towns.TownS;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Rank {
    private String name;
    private Collection<String> permissions = new HashSet<>();

    public Rank(String rank,Collection<String> perms){
        name = rank;
        permissions = perms;
        TownS.g().RegisterRanks(this);
    }


    public String getName(){
        return name;
    }
    public boolean hasPermission(String perm){
        return permissions.contains(perm.toLowerCase());
    }

    public void addPerm(String perm){
        permissions.add(perm.toLowerCase());
    }
    public void removePerm(String perm){
        permissions.remove(perm.toLowerCase());
    }


}
