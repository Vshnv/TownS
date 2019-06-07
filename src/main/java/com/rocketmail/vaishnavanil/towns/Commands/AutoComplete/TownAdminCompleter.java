package com.rocketmail.vaishnavanil.towns.Commands.AutoComplete;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TownAdminCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();
        System.out.println(args.length);
        switch (args.length) {

            case 1:
                suggestions.add("regen");
                suggestions.add("unclaim");
                suggestions.add("setspawn");
                suggestions.add("setbalance");
                suggestions.add("setname");
                suggestions.add("setplotname");
                suggestions.add("setname");
                suggestions.add("setplotowner");
                suggestions.add("deletetown");
                suggestions.add("setplotowner");
                suggestions.add("settownowner");
                suggestions.add("setplotowner");
                return suggestions;
            case 2:
                switch (args[0]){
                    case "regen":
                        return getEmpty();
                    case "unclaim":
                        return getEmpty();
                    case "setspawn":
                        return getEmpty();
                    case "setbalance":
                        return getTowns();
                    case "setname":
                        return getTowns();
                    case "setplotname":
                        suggestions.add("Enter Name");
                        suggestions.add("Garden");
                        suggestions.add("Market");
                        suggestions.add("Farm");
                        return suggestions;
                    case "setplotowner":
                        return null;
                    case "deletetown":
                        return getTowns();
                    case "settownowner":
                        return null;
                    default:
                        return getEmpty();
                }
            case 3:
                switch (args[0]){
                    case "regen":
                        return getEmpty();
                    case "unclaim":
                        return getEmpty();
                    case "setspawn":
                        return getEmpty();
                    case "setbalance":
                        return getNumbers();
                    case "setname":
                        suggestions.add("Name");
                        return suggestions;
                    case "setplotname":
                        return getEmpty();
                    case "setplotowner":
                        return getEmpty();
                    case "deletetown":
                        return getEmpty();
                    case "settownowner":
                        return getEmpty();
                    default:
                        return getEmpty();
                }
            default:
                return getEmpty();
        }
    }

    public List<String> getEmpty(){
        List<String> suggestions = new ArrayList<>();
        suggestions.add("");
        return suggestions;
    }

    public List<String> getTowns(){
        List<String> suggestions = new ArrayList<>();
        Set<String> townuuids = TownS.g().getAlLTownUUID();
        for(String townuuid: townuuids){
            suggestions.add(TownS.g().getTown(UUID.fromString(townuuid)).getName());
        }
        return suggestions;
    }

    public List<String> getNumbers(){
        List<String> suggestions = new ArrayList<>();
        suggestions.add("0");
        suggestions.add("10");
        suggestions.add("100");
        suggestions.add("1000");
        suggestions.add("10000");
        suggestions.add("100000");
        return suggestions;
    }


}
