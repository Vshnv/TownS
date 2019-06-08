package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Economy.EconomyHandler;
import com.rocketmail.vaishnavanil.towns.GUI.GUI;
import com.rocketmail.vaishnavanil.towns.GUI.SI.WarpsGUI;
import com.rocketmail.vaishnavanil.towns.MapGUI.MapFullScreen;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Rank;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Towns.TownPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //if (!cmd.getName().equalsIgnoreCase("towns")) return true;
        if (!(sender instanceof Player)) {
            //TODO::PLAYER-ONLY COMMAND
            return true;
        }
        Player sndr = (Player) sender;
        if (!(args.length > 0)) {
            /*MSG ADDED .E.A.*/
            Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
            return true;
        }


        String sub_cmd = args[0].toLowerCase();

        switch (sub_cmd) {
            /*CREATE*/
            case "create":
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                if (TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You already belong to a Town!");
                    return true;
                }
                if (TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Please stand in unclaimed land to use this command! use /towns map for map view");
                    return true;
                }
                if(TownS.g().isNameUsed(args[1])){
                    Format.CmdErrFrmt.use().a(sndr, "Town Name already taken! Please try another name");
                    return true;
                }

                if(args[1].toCharArray().length >10){
                    Format.CmdErrFrmt.use().a(sndr, "Town Name cannot be that long!");
                    return true;
                }
                create(sndr, args[1]);
                break;
            /*END CREATION*/
            case "setname":
                if (args.length == 2) {
                    String town_name = args[1];
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("townsetname",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                        return true;
                    }
                    if(TownS.g().isNameUsed(town_name)){
                        Format.CmdErrFrmt.use().a(sndr, "Town name already taken! Please try another name");
                        return true;
                    }
                    if(town_name.toCharArray().length >10){
                        Format.CmdErrFrmt.use().a(sndr, "Town Name cannot be that long!");
                        return true;
                    }
                    TownS.g().getTown(sndr).setName(town_name);
                    Format.AlrtFrmt.use().a(sndr, "Successfully Set Town Name: " + town_name);
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town setname <name>");
                }

                break;
            case "deposit":
                if (args.length == 2) {
                    String amount = args[1];
                    try {
                        Double amount_num = Double.parseDouble(amount);
                        if (TownS.g().hasTown(sndr)) {
                            if(!TownS.g().getTown(sndr).hasPermission("deposit",sndr)){
                                Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                                return true;
                            }
                            if (EconomyHandler.INSTANCE.depositIntoTown(sndr, TownS.g().getTown(sndr), amount_num)) {
                                Format.AlrtFrmt.use().a(sndr, "Successfully deposited " + args[1] + "$ into Town Bank");

                            } else {
                                Format.CmdErrFrmt.use().a(sndr, "Depositing " + args[1] + "$ to Town failed due to insufficient funds.");
                            }
                        } else {
                            Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        }
                    } catch (Exception e) {
                        Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town deposit <amount>");
                    }
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town deposit <amount>");
                }
                break;
            case "withdraw":
                if (args.length == 2) {
                    String amount = args[1];
                    try {
                        Double amount_num = Double.parseDouble(amount);
                        if (TownS.g().hasTown(sndr)) {

                            /* Check for withdraw permission here */
                            Boolean condition = TownS.g().getTown(sndr).hasPermission("withdraw",sndr);
                            /* Check for withdraw permission here */

                            if (condition) {
                                if (EconomyHandler.INSTANCE.withdrawFromTown(sndr, TownS.g().getTown(sndr), amount_num)) {
                                    Format.AlrtFrmt.use().a(sndr, "Successfully withdrawn " + args[1] + "$ from Town Bank");
                                } else {
                                    Format.CmdErrFrmt.use().a(sndr, "Insufficient funds in Town");
                                }
                            }

                        } else {
                            Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        }
                    } catch (Exception e) {
                        Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town withdraw <amount>");
                    }
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town withdraw <amount>");
                }
                break;
            //TOWN DELETE
            case "delete":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (TownS.g().getTown(sndr).getMayor() != sndr) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Only the town Mayor may use this command!");
                    return true;
                }
                if(args.length == 2){
                    if(args[1].equalsIgnoreCase("ConfirmFromGUI")){
                        Format.AlrtFrmt.use().a(sndr, "Successfully deleted your town!");
                        deleteTown(sndr);
                        return true;
                    }
                }

                Format.CmdInfoFrmt.use().a(sndr,"Are you sure you want to delete your town? [Answer in GUI]");
                sndr.openInventory(GUI.DELETETown.get());
                break;
            //TOWN DELETE

            case "map":
                //TODO::MAP GUI
                sndr.openInventory(MapFullScreen.get.create(sndr));
                Format.CmdInfoFrmt.use().a(sndr, "You have opened the Town Map!");
                break;
            case "spawn":
                if (!TownS.g().hasTown(sndr)) {
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                } else {
                    Town sender_town = TownS.g().getTown(sndr);
                    if (sender_town.getWarpPoint(sender_town, "spawn") != null) {
                        sndr.teleport(sender_town.getWarpPoint(sender_town, "spawn"));
                        Format.AlrtFrmt.use().a(sndr, "Teleported to Town Spawn");
                    } else {
                        Format.CmdErrFrmt.use().a(sndr, "Warp Point: SPAWN not found.");
                    }
                }
                break;
            case "warp":
                if(args.length == 1){

                    if(TownS.g().hasTown(sndr)){
                        if(!TownS.g().getTown(sndr).hasPermission("warp",sndr)){

                            Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                            return true;
                        }
                        sndr.openInventory(new WarpsGUI("Town Warps", sndr).get());
                        return true;
                    }else{
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                }
                if (args.length == 2) {
                    if (!TownS.g().hasTown(sndr)) {
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    } else {
                        if(!TownS.g().getTown(sndr).hasPermission("warp",sndr)){
                            Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                            return true;
                        }
                        String warp_name = args[1];
                        Town sender_town = TownS.g().getTown(sndr);
                        if (sender_town.getWarpPoint(sender_town, warp_name) != null) {
                            sndr.teleport(sender_town.getWarpPoint(sender_town, warp_name));
                            Format.AlrtFrmt.use().a(sndr, "Teleported to Town Warp: " + warp_name.toUpperCase());
                        } else {
                            Format.CmdErrFrmt.use().a(sndr, "Warp Point: " + warp_name.toUpperCase() + " not found.");
                        }
                    }
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town warp <name>");
                }
                break;
            case "setwarp":
                if (args.length == 2) {
                    String warp_name = args[1];
                    if(warp_name.toCharArray().length >10){
                        Format.CmdErrFrmt.use().a(sndr, "Warp Name cannot be that long!");
                        return true;
                    }
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("setwarp",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                        return true;
                    }
                    if (TownS.g().isClaimed(sndr.getLocation().getChunk()) && TownS.g().getTown(sndr.getLocation().getChunk()).equals(TownS.g().getTown(sndr))) {
                        Town senders_town = TownS.g().getTown(sndr);
                        senders_town.setWarpPoint(senders_town, warp_name, sndr.getLocation());
                        Format.AlrtFrmt.use().a(sndr, "Successfully set Warp: " + warp_name);
                        return true;
                    } else {
                        Format.CmdErrFrmt.use().a(sndr, "This Area does not belong to your Town");
                    }

                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town setwarp <name>");
                }
                break;
            case "delwarp":
                if (args.length == 2) {
                    String warp_name = args[1];
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("delwarp",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You dont have permission use this command!");
                        return true;
                    }
                    Town senders_town = TownS.g().getTown(sndr);
                    senders_town.deleteWarpPoint(warp_name);
                    Format.AlrtFrmt.use().a(sndr, "Successfully removed Warp: " + warp_name);
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town delwarp <name>");
                }
                break;
            case "setspawn":

                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("setspawn",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
                    return true;
                }
                Town senders_town = TownS.g().getTown(sndr);
                senders_town.setWarpPoint(senders_town, "spawn", sndr.getLocation());
                senders_town.setSpawnChunk(senders_town, sndr.getLocation().getChunk());
                Format.AlrtFrmt.use().a(sndr, "Successfully set Spawn Point.");
                break;
            //TOWN SETWARP SPAWN

            case "claim":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("claim",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
                    return true;
                }
                if (TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Please stand in unclaimed land to use this command! /towns map for map view");
                    return true;
                }
                if (!nearbyChunkClaimed(sndr.getLocation().getChunk(), TownS.g().getTown(sndr))) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You may only claim chunks adjacent to your claims!");
                    return true;
                }
                claim(sndr);
                break;
            //UNCLAIM
            case "unclaim":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("unclaim",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permissions use this command!");
                    return true;
                }
                if (!TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Please stand in claimed land to use this command! use /towns map for map view");
                    return true;
                }
                if (TownS.g().getTown(sndr.getLocation().getChunk()) != TownS.g().getTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "This claim does not belong to your town!");
                    return true;
                }
                if (TownS.g().getTown(sndr).isSpawnChunk(sndr.getLocation().getChunk())) {
                    Format.CmdErrFrmt.use().a(sndr, "You cannot unclaim the town's spawn chunk");
                    return true;
                } else {
                    unclaim(sndr);
                    Format.CmdInfoFrmt.use().a(sndr, "You have unclaimed the current chunk!");
                    return true;
                }
                //UNCLAIM
            case "test":
                sndr.openInventory(GUI.YN.get());
                break;
            case "invite":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("invite",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
                    return true;
                }
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                Player p;
                try {
                    p = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    Format.CmdErrFrmt.use().a(sndr, "Could not find player with that name!");
                    return true;
                }
                Town t = TownS.g().getTown(sndr);
                TownPlayer tp = TownS.g().getTownPlayer(p);
                if (tp.isInvited(t)) {
                    Format.CmdErrFrmt.use().a(sndr, "Player already invited!");
                    return true;
                }
                tp.invite(t);
                Format.CmdInfoFrmt.use().a(sndr, "Player " + args[1] + " was invited to your town!");
                break;
            case "uninvite":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("uninvite",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
                    return true;
                }
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                Player pl;
                try {
                    pl = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    Format.CmdErrFrmt.use().a(sndr, "Could not find player with that name!");
                    return true;
                }
                Town to = TownS.g().getTown(sndr);
                TownPlayer tpl = TownS.g().getTownPlayer(pl);
                if (!tpl.isInvited(to)) {
                    Format.CmdErrFrmt.use().a(sndr, "Player already not invited to town!");
                    return true;
                }
                tpl.uninvite(to);
                Format.CmdInfoFrmt.use().a(sndr, "Player " + args[1] + " was unInvited from you town!");
                break;
            case "join":
                if (TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You already belong to a town! Leave it to join another town");
                    return true;
                }
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                String Tname = args[1];
                if (TownS.g().getTown(Tname) == null) {
                    Format.CmdErrFrmt.use().a(sndr, "Town not found!");
                    return true;
                }
                Town tWn = TownS.g().getTown(Tname);
                TownPlayer plT = TownS.g().getTownPlayer(sndr);
                if (plT.isInvited(tWn)) {
                    tWn.addMember(sndr);
                    Format.CmdInfoFrmt.use().a(sndr, "You join the Town " + tWn.getName());
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "You are not invited to this town!");

                }
                break;

            case "leave":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                Town tLve = TownS.g().getTown(sndr);
                if (tLve.getMayor().getUniqueId() == sndr.getUniqueId()) {
                    Format.CmdErrFrmt.use().a(sndr, "Mayor shall not leave the town!");
                    return true;
                }
                tLve.removePlayer(sndr);
                Format.CmdInfoFrmt.use().a(sndr, "You left the town " + tLve.getName());
                break;
            case "kick":
                if(args.length == 2){
                    OfflinePlayer opl = Bukkit.getOfflinePlayer(args[1]);
                    if(!opl.hasPlayedBefore()){
                        Format.CmdErrFrmt.use().a(sndr, "Could not find player with name "+ args[1]);
                        return false;
                    }
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }

                    Town kt = TownS.g().getTown(sndr);
                    if(kt.hasPermission("kick",sndr)){
                        if(kt.hasRank(opl.getUniqueId()) && kt.hasRank(sndr) && sndr.getUniqueId() != kt.getMayor().getUniqueId()){
                            Rank cpr = kt.getRank(opl.getUniqueId());
                            if(cpr.isHigherThan(kt.getRank(sndr))){
                                Format.CmdErrFrmt.use().a(sndr, "You cannot kick a player with a higher rank!");

                                return true;
                            }
                            kt.removePlayer(opl);
                            Format.CmdErrFrmt.use().a(sndr, "Successfully kicked player with name " + args[1]);

                        }


                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permissions to use this command!");

                    }
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town kick <name>");

                }
                break;
            case "setmayor":
                if (args.length == 2) {
                    String player_name = args[1];
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                    if (TownS.g().getTown(sndr).getMayor() != sndr) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "Only the town Mayor may use this command!");
                        return true;
                    }
                    if (Bukkit.getOfflinePlayer(player_name).hasPlayedBefore()) {
                        if (TownS.g().getTown(sndr).setOwner(Bukkit.getOfflinePlayer(player_name).getUniqueId())) {
                            Format.AlrtFrmt.use().a(sndr, "Successfully made " + player_name + " the new mayor");
                        } else {
                            Format.CmdErrFrmt.use().a(sndr, "That player does not belong to your Town.");
                        }
                    } else {
                        Format.CmdErrFrmt.use().a(sndr, "Player not Found!");
                    }
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town setmayor <name>");
                }
                break;
            case "setrank":
                if(args.length ==3){
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    } Town twn = TownS.g().getTown(sndr);
                    OfflinePlayer rnk;
                    try{
                        rnk = Bukkit.getOfflinePlayer(args[1]);
                    }catch (Exception e){
                        Format.CmdErrFrmt.use().a(sndr, "Player not found!");
                        return true;
                    }
                    if(rnk.getUniqueId() == twn.getMayor().getUniqueId()){
                        Format.CmdErrFrmt.use().a(sndr, "Cannot set a rank to the mayor!");
                        return true;
                    }
                    if(TownS.g().getRank(args[2]) == null && !args[2].equalsIgnoreCase("none")){
                        Format.CmdErrFrmt.use().a(sndr, "Rank not found!");
                        return true;
                    }

                    if((twn.getRank(sndr) == null)){
                        if(!twn.hasPermission("setrank",sndr)){
                            Format.CmdErrFrmt.use().a(sndr, "You do not have perm for this command!");

                            return true;

                        }
                    }else{
                        Rank r = twn.getRank(sndr);
                        if(twn.hasRank(rnk.getUniqueId())){
                            Rank rnkRank = twn.getRank(rnk.getUniqueId());
                            if(rnkRank.isHigherThan(r)){
                                Format.CmdErrFrmt.use().a(sndr, "You do not have perm for this set rank of that person!");
                                return true;
                            }
                        }
                        if (!r.hasPermission("csetrank")) {
                            if (twn.getMayor().getUniqueId() != sndr.getUniqueId()) {
                                Format.CmdErrFrmt.use().a(sndr, "You do not have perm for this command!");

                                return true;
                            }

                        }
                    }
                    if(args[2].equalsIgnoreCase("none")){
                        twn.setRank(rnk,null);
                        Format.CmdInfoFrmt.use().a(sndr,"Successfully removed rank of " +   args[1]);
                        return true;
                    }
                    twn.setRank(rnk, TownS.g().getRank(args[2]));
                    Format.CmdInfoFrmt.use().a(sndr,"Successfully set rank of " +args[1]+" to "+args[2]);


                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town setrank <player> <rank>");

                }
                break;

        }
        return true;
    }

    public void create(Player p, String townName) {

        if (EconomyHandler.INSTANCE.getPlayerBalance(p) >= TownS.TownCost) {
            if (EconomyHandler.INSTANCE.changePlayerBalance(p, -TownS.TownCost)) {
                Town newT = new Town(townName, p);
                newT.claim(p.getLocation().getChunk(), p);
                newT.setWarpPoint(newT, "spawn", p.getLocation());
                newT.setSpawnChunk(newT, p.getLocation().getChunk());
                TownS.g().getClaim(p.getLocation().getChunk()).setName("Settlement");
                Format.AlrtFrmt.use().a(p, "Created new town with name -> " + townName);
            } else {
                Format.CmdErrFrmt.use().a(p, "An Error Occurred while trying to pay for the new Town.");
            }
        } else {
            Format.CmdErrFrmt.use().a(p, "Creating a new Town costs: " + TownS.TownCost.toString() + "$");
        }
    }

    public void deleteTown(Player sndr) {
        TownS.g().getTown(sndr).deleteTown();
    }

    public void claim(Player sndr) {

        if (EconomyHandler.INSTANCE.getPlayerBalance(sndr) >= TownS.PlotCost) {
            if (EconomyHandler.INSTANCE.changePlayerBalance(sndr, -TownS.PlotCost)) {
                TownS.g().getTown(sndr).claim(sndr.getLocation().getChunk(), sndr);
                Format.CmdInfoFrmt.use().a(sndr, "You have claimed land for your town! -> /towns map <- for neaby claims");
            } else {
                Format.CmdErrFrmt.use().a(sndr, "An Error Occurred while trying to pay for the new area.");
            }
        } else {
            Format.CmdErrFrmt.use().a(sndr, "Claiming a new Area costs: " + TownS.PlotCost.toString() + "$");
        }


    }

    public void claim(Player sndr, String plot_name) {
        if (EconomyHandler.INSTANCE.getPlayerBalance(sndr) >= TownS.PlotCost) {
            if (EconomyHandler.INSTANCE.changePlayerBalance(sndr, -TownS.PlotCost)) {
                TownS.g().getTown(sndr).claim(sndr.getLocation().getChunk(), sndr);
                TownS.g().getClaim(sndr.getLocation().getChunk()).setName(plot_name);
                Format.CmdInfoFrmt.use().a(sndr, "You have claimed land for your town! -> /towns map <- for neaby claims");
            } else {
                Format.CmdErrFrmt.use().a(sndr, "An Error Occurred while trying to pay for the new area.");
            }
        } else {
            Format.CmdErrFrmt.use().a(sndr, "Claiming a new Area costs: " + TownS.PlotCost.toString() + "$");
        }
    }

    public void unclaim(Player sndr) {
        TownS.g().getTown(sndr).unclaim(sndr.getLocation().getChunk());
    }


    public boolean nearbyChunkClaimed(Chunk chunk, Town town) {
        Chunk one = chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ());
        Chunk two = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1);
        Chunk three = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ());
        Chunk four = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1);

        if (TownS.g().isClaimed(one)) {
            if (TownS.g().getClaim(one).getTown() == town) return true;
        }
        if (TownS.g().isClaimed(two)) {
            if (TownS.g().getClaim(two).getTown() == town) return true;
        }
        if (TownS.g().isClaimed(three)) {
            if (TownS.g().getClaim(three).getTown() == town) return true;
        }
        if (TownS.g().isClaimed(four)) {
            if (TownS.g().getClaim(four).getTown() == town) return true;
        }
        return false;
    }

    public boolean isNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
