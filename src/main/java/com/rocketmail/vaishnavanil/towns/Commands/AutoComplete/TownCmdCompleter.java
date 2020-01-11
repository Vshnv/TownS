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

                if(!TownS.g().hasTown(player)){
                    suggestions.add("create");
                    suggestions.add("join");
                    return suggestions;
                }
                suggestions.add("info");
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
                suggestions.add("setrank");
                suggestions.add("setopen");
                suggestions.add("visit");


                suggestions.add("delwarp");

                suggestions.add("join");
                suggestions.add("leave");
                suggestions.add("kick");

                if(TownS.g().getTown(player).getMayor().getUniqueId().equals(player.getUniqueId())){
                    suggestions.add("delete");
                    suggestions.add("setmayor");
                    suggestions.remove("create");
                }

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
                    case "visit":
                        return getTowns();
                    case "setopen":
                        return getBools();
                    case "join":
                        return getTowns();
                    default:
                        return getEmpty();
                }
            case 3:
                switch (args[0]){
                    case "setrank":
                        if(TownS.g().getRanks().isEmpty()){
                            return getEmpty();
                        }else{
                            suggestions.addAll(TownS.g().getRanks());
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

    public List<String> getBools(){
        List<String> suggestions = new ArrayList<>();
        suggestions.add("true");
        suggestions.add("false");
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
