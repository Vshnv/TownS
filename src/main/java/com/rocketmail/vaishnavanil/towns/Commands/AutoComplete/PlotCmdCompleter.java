package com.rocketmail.vaishnavanil.towns.Commands.AutoComplete;

import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlotCmdCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if(!(commandSender instanceof Player)) {return null; }
        Player player = (Player) commandSender;
        switch (args.length) {
            case 1:
                suggestions.add("claim");
                suggestions.add("border");
                suggestions.add("setname");
                suggestions.add("flags");
                suggestions.add("allow");

                suggestions.add("fs");
                suggestions.add("nfs");

                suggestions.add("disallow");
                suggestions.add("access");
                return suggestions;
            case 2:
                switch (args[0]){
                    case "claim":
                    case "border":
                    case "flags":
                        return getEmpty();
                    case "setname":
                        suggestions.add("NewName");
                        suggestions.add("Garden");
                        suggestions.add("Market");
                        suggestions.add("Farm");
                        return suggestions;
                    case "allow":
                    case "disallow":
                        suggestions.add("Build");
                        suggestions.add("Container");
                        return suggestions;
                    case "forsale":
                    case "fs":
                        return getNumbers();
                    default:
                        return getEmpty();
                }
            case 3:
                switch (args[0]){
                    case "allow":
                    case "disallow":
                        suggestions.add("Player");
                        suggestions.add("Rank");
                        return suggestions;
                    default:
                        return getEmpty();
                }
            case 4:

                if(TownS.g().getClaim(player.getLocation().getChunk()) == null){
                    return getEmpty();
                }

                if(!TownS.g().getTown(player.getLocation().getChunk()).equals(TownS.g().getTown(player))){
                    suggestions.add("Not Authorized");
                    return suggestions;
                }

                if( args[2].equalsIgnoreCase("player")){
                        if(args[1].equalsIgnoreCase("build")){
                            if(args[0].equalsIgnoreCase("allow")){
                                return playerToSuggestAllowBuild();
                            }
                            if(args[0].equalsIgnoreCase("disallow")){
                                return playerToSuggestDisAllowBuild(TownS.g().getClaim(player.getLocation().getChunk()));
                            }
                            return getEmpty();
                        }

                        if(args[1].equalsIgnoreCase("container")){
                            if(args[0].equalsIgnoreCase("allow")){
                                return playerToSuggestAllowContainer();
                            }
                            if(args[0].equalsIgnoreCase("disallow")){
                                return playerToSuggestDisAllowContaienr(TownS.g().getClaim(player.getLocation().getChunk()));
                            }
                            return getEmpty();
                        }
                        return getEmpty();
                }

                if( args[2].equalsIgnoreCase("rank") ){

                    if(args[1].equalsIgnoreCase("build")){
                        if(args[0].equalsIgnoreCase("allow")){
                            return ranksToSuggestAllowBuild(TownS.g().getClaim(player.getLocation().getChunk()));
                        }
                        if(args[0].equalsIgnoreCase("disallow")){
                            return rankToSuggestDisAllowBuild(TownS.g().getClaim(player.getLocation().getChunk()));
                        }
                        return getEmpty();
                    }

                    if(args[1].equalsIgnoreCase("container")){
                        if(args[0].equalsIgnoreCase("allow")){
                            return rankToSuggestAllowContainer(TownS.g().getClaim(player.getLocation().getChunk()));
                        }
                        if(args[0].equalsIgnoreCase("disallow")){
                            return rankToSuggestDisAllowContainer(TownS.g().getClaim(player.getLocation().getChunk()));
                        }
                        return getEmpty();
                    }

                    return getEmpty();

                }
                return getEmpty();

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


    public List<String> ranksToSuggestAllowBuild(Claim claim){
        List<String> suggestion = new ArrayList<>();
        List<String> alreadyTrustedRanks = claim.getRankBuildTrusted();
        for(String rank: TownS.g().getRanks()){
            if(!(TownS.g().getRank(rank).hasPermission("BuildALL"))){
                suggestion.add(rank);
            }
        }
        suggestion.removeAll(alreadyTrustedRanks);
        return suggestion;
    }

    public List<String> rankToSuggestDisAllowBuild(Claim claim){
        return claim.getRankBuildTrusted();
    }
    public List<String> rankToSuggestDisAllowContainer(Claim claim){
        return claim.getRankContainerTrusted();
    }


    public List<String> rankToSuggestAllowContainer(Claim claim){
        List<String> suggestion = new ArrayList<>();
        List<String> alreadyTrustedRanks = claim.getRankContainerTrusted();
        for(String rank: TownS.g().getRanks()){
            if(!(TownS.g().getRank(rank).hasPermission("ContainerALL"))){
                suggestion.add(rank);
            }
        }
        suggestion.removeAll(alreadyTrustedRanks);
        return suggestion;
    }

    public List<String> playerToSuggestAllowBuild(){
        return null;
    }

    public List<String> playerToSuggestDisAllowBuild(Claim claim){
        List<String> suggestion = new ArrayList<>();
        for(UUID player_uuid: claim.getBuildTrusted()){
            suggestion.add(Bukkit.getOfflinePlayer(player_uuid).getName());
        }
        return suggestion;
    }

    public List<String> playerToSuggestAllowContainer(){
        return null;
    }

    public List<String> playerToSuggestDisAllowContaienr(Claim claim){
        List<String> suggestion = new ArrayList<>();
        for(UUID player_uuid: claim.getContainerTrusted()){
            suggestion.add(Bukkit.getOfflinePlayer(player_uuid).getName());
        }
        return suggestion;
    }

}
