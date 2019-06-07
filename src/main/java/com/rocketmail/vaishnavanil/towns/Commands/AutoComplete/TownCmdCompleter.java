package com.rocketmail.vaishnavanil.towns.Commands.AutoComplete;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TownCmdCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();
        Player player = (Player) commandSender;

        switch (args.length) {
            case 1:
                suggestions.add("create");
                suggestions.add("spawn");
                suggestions.add("map");
                suggestions.add("warp");
                suggestions.add("deposit");
                suggestions.add("withdraw");
                suggestions.add("claim");
                suggestions.add("unclaim");

                suggestions.add("invite");
                suggestions.add("uninvite");

                suggestions.add("setwarp");
                suggestions.add("setname");
                suggestions.add("setspawn");
                suggestions.add("setmayor");
                suggestions.add("setrank");

                suggestions.add("delwarp");


                suggestions.add("fs");
                suggestions.add("nfs");

                suggestions.add("test");

                suggestions.add("join");
                suggestions.add("leave");
                suggestions.add("delete");
                suggestions.add("kick");
                return suggestions;
            case 2:
                switch (args[0]){


                    case "create":
                        suggestions.add("TownName");
                        return suggestions;

                    case "warp":
                    case "delwarp":
                        if(TownS.g().hasTown(player)){
                            suggestions.addAll(TownS.g().getTown(player).getTownWarpKeys() == null ? getEmpty(): TownS.g().getTown(player).getTownWarpKeys());
                            return suggestions;
                        }else{
                            return getEmpty();
                        }

                    case "deposit" :
                    case "withdraw":
                    case "fs":
                        return getNumbers();

                    case "invite":
                    case "setmayor":
                    case "setrank":
                    case "uninvite":
                    case "kick":
                        return null;

                    case "setwarp":
                        suggestions.add("NewWarpName");
                        return suggestions;

                    case "setname":
                        suggestions.add("NewName");
                        return suggestions;

                    case "join":
                        return getTowns();
                    default:
                        return getEmpty();
                }
            case 3:
                switch (args[0]){
                    case "setrank":
                        if(TownS.g().getRanks().isEmpty()){
                            System.out.println("ranks empty");
                            return getEmpty();
                        }else{
                            suggestions.addAll(TownS.g().getRanks());
                            System.out.println(suggestions.toString());
                            return suggestions;
                        }
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
