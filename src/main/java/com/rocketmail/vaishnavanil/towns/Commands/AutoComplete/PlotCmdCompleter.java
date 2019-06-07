package com.rocketmail.vaishnavanil.towns.Commands.AutoComplete;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlotCmdCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();

        switch (args.length) {
            case 1:
                suggestions.add("claim");
                suggestions.add("border");
                suggestions.add("setname");
                suggestions.add("flag");
                return suggestions;
            case 2:
                switch (args[0]){
                    case "claim":
                        return getEmpty();
                    case "border":
                        return getEmpty();
                    case "setname":
                        suggestions.add("NewName");
                        suggestions.add("Garden");
                        suggestions.add("Market");
                        suggestions.add("Farm");
                        return suggestions;
                    case "flag":
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
