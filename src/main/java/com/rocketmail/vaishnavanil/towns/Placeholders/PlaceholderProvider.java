package com.rocketmail.vaishnavanil.towns.Placeholders;

import com.rocketmail.vaishnavanil.towns.TownS;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderProvider extends PlaceholderExpansion {


    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        switch (identifier){

            case (String) "name":
                if(!TownS.g().hasTown(player)){ return ""; }
                return TownS.g().getTown(player).getName();
            case (String) "balance":
                if(!TownS.g().hasTown(player)){ return "0"; }
                return TownS.g().getTown(player).getTownBalance().toString();
            default:
                return null;
        }
    }


    @Override
    public String getIdentifier(){
        return "towns";
    }

    @Override
    public String getVersion(){
        return TownS.g().getDescription().getVersion();
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return TownS.g().getDescription().getAuthors().toString();
    }



}
