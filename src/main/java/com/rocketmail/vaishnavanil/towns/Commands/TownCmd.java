package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Economy.EconomyHandler;
import com.rocketmail.vaishnavanil.towns.GUI.GUI;
import com.rocketmail.vaishnavanil.towns.MapGUI.Map;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Towns.TownPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.Normalizer;

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


                create(sndr, args[1]);
                break;
            /*END CREATION*/
            case "setname":
                if(args.length==2){
                    String town_name = args[1];
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
                    TownS.g().getTown(sndr).setName(town_name);
                    Format.AlrtFrmt.use().a(sndr, "Successfully Set Town Name: "+town_name);
                }else {
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town setname <name>");
                }

                break;
            case "deposit":
                if(args.length == 2){
                    String amount = args[1];
                    try{
                        Double amount_num = Double.parseDouble(amount);
                        if(TownS.g().hasTown(sndr)){
                            if(EconomyHandler.INSTANCE.depositIntoTown(sndr, TownS.g().getTown(sndr), amount_num)){
                                Format.AlrtFrmt.use().a(sndr, "Successfully deposited "+args[1]+"$ into Town Bank");
                                System.out.println(TownS.g().getTown(sndr).getTownBalance());
                            }else{
                                Format.CmdErrFrmt.use().a(sndr, "Depositing "+args[1]+"$ to Town failed due to insufficient funds.");
                            }
                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        }
                    }catch (Exception e){
                        Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town deposit <amount>");
                    }
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town deposit <amount>");
                }
                break;
            case "withdraw":
                if(args.length == 2){
                    String amount = args[1];
                    try{
                        Double amount_num = Double.parseDouble(amount);
                        if(TownS.g().hasTown(sndr)){

                            /* Check for withdraw permission here */
                            Boolean condition = true;
                            /* Check for withdraw permission here */

                            if(condition){
                                if(EconomyHandler.INSTANCE.withdrawFromTown(sndr, TownS.g().getTown(sndr), amount_num)){
                                    Format.AlrtFrmt.use().a(sndr, "Successfully withdrawn "+args[1]+"$ from Town Bank");
                                }else{ Format.CmdErrFrmt.use().a(sndr, "Insufficient funds in Town"); }
                            }

                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        }
                    }catch (Exception e){
                        Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town withdraw <amount>");
                    }
                }else{
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

                deleteTown(sndr);
                Format.AlrtFrmt.use().a(sndr, "Successfully deleted your town!");
                break;
            //TOWN DELETE

            case "map":
                //TODO::MAP GUI
                sndr.openInventory(Map.get.create(sndr));
                Format.CmdInfoFrmt.use().a(sndr, "You have opened the Town Map!");
                break;
            case "spawn":
                if(!TownS.g().hasTown(sndr)){
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }else{
                    Town sender_town = TownS.g().getTown(sndr);
                    if(sender_town.getWarpPoint(sender_town, "spawn") != null){
                        sndr.teleport( sender_town.getWarpPoint(sender_town, "spawn") );
                        Format.AlrtFrmt.use().a(sndr, "Teleported to town spawn");
                    }else{
                        Format.CmdErrFrmt.use().a(sndr, "Warp Point: SPAWN not found.");
                    }
                }
                break;
            case "warp":
                if(args.length==2){
                    if(!TownS.g().hasTown(sndr)){
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }else{
                        String warp_name = args[1];
                        Town sender_town = TownS.g().getTown(sndr);
                        if(sender_town.getWarpPoint(sender_town, warp_name) != null){
                            sndr.teleport( sender_town.getWarpPoint(sender_town, warp_name) );
                            Format.AlrtFrmt.use().a(sndr, "Teleported to Town Warp: "+warp_name.toUpperCase());
                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "Warp Point: "+warp_name.toUpperCase()+" not found.");
                        }
                    }
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town warp <name>");
                }
                break;
            case "setwarp":
                if(args.length==2){
                    String warp_name = args[1];
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
                    if(TownS.g().isClaimed(sndr.getLocation().getChunk()) && TownS.g().getTown(sndr.getLocation().getChunk()).equals(TownS.g().getTown(sndr)) ){
                        Town senders_town = TownS.g().getTown(sndr);
                        senders_town.setWarpPoint(senders_town, warp_name, sndr.getLocation());
                        Format.AlrtFrmt.use().a(sndr, "Successfully set Warp: "+warp_name);
                        return true;
                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "This Area does not belong to your Town");
                    }

                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town setwarp <name>");
                }
                break;
            case "delwarp":
                if(args.length==2){
                    String warp_name = args[1];
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
                    Town senders_town = TownS.g().getTown(sndr);
                    senders_town.deleteWarpPoint(warp_name);
                    Format.AlrtFrmt.use().a(sndr, "Successfully removed Warp: "+warp_name);
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Invalid Format: Use /town delwarp <name>");
                }
                break;
            case "setspawn":

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
                if (TownS.g().getTown(sndr).getMayor() != sndr) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Only the town Mayor may use this command!");
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
                if (TownS.g().getTown(sndr).getMayor() != sndr) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Only the town Mayor may use this command!");
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
                if(TownS.g().getTown(sndr).isSpawnChunk(sndr.getLocation().getChunk())){
                    Format.CmdErrFrmt.use().a(sndr, "You cannot unclaim the town's spawn chunk");
                    return true;
                }else{
                    unclaim(sndr);
                    Format.CmdInfoFrmt.use().a(sndr, "You have unclaimed the current chunk!");
                    return true;
                }
            //UNCLAIM
            //FS
            case "fs":
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                if(isNumber(args[1])){
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
                    fs(sndr, Double.valueOf(args[1]));
                    Format.CmdInfoFrmt.use().a(sndr, "Current chunk is now for sale for $" + args[1] + "/- !");
                }else {
                    Format.CmdErrFrmt.use().a(sndr, "You must specify a cost such as /towns fs 100");
                    return true;
                }
                break;
            //END FS
            //NFS
            case "nfs":
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
                if (!TownS.g().getClaim(sndr.getLocation().getChunk()).isFS()) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "This claim is not for sale at the moment!");
                    return true;
                }
                nfs(sndr);
                Format.CmdInfoFrmt.use().a(sndr, "This claim is no longer available for sale!");
                break;
            case "test":
                sndr.openInventory(GUI.YN.get());
                break;
            case "invite":
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
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                Player p;
                try {
                    p =Bukkit.getPlayer(args[1]);
                }catch (Exception e){
                    Format.CmdErrFrmt.use().a(sndr, "Could not find player with that name!");
                    return true;
                }
                Town t = TownS.g().getTown(sndr);
                TownPlayer tp =TownS.g().getTownPlayer(p);
                if(tp.isInvited(t)){
                    Format.CmdErrFrmt.use().a(sndr, "Player already invited!");
                    return true;
                }
                tp.invite(t);
                Format.CmdInfoFrmt.use().a(sndr,"Player "+args[1]+" was invited to your town!");
                break;
            case "uninvite":
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
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                Player pl;
                try {
                    pl =Bukkit.getPlayer(args[1]);
                }catch (Exception e){
                    Format.CmdErrFrmt.use().a(sndr, "Could not find player with that name!");
                    return true;
                }
                Town to = TownS.g().getTown(sndr);
                TownPlayer tpl =TownS.g().getTownPlayer(pl);
                if(!tpl.isInvited(to)){
                    Format.CmdErrFrmt.use().a(sndr,"Player already not invited to town!");
                    return true;
                }
                tpl.uninvite(to);
                Format.CmdInfoFrmt.use().a(sndr,"Player "+args[1]+" was unInvited from you town!");
                break;
            case"join":
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
                if(TownS.g().getTown(Tname) == null){
                    Format.CmdErrFrmt.use().a(sndr, "Town not found!");
                    return true;
                }
                Town tWn = TownS.g().getTown(Tname);
                TownPlayer plT = TownS.g().getTownPlayer(sndr);
                if(plT.isInvited(tWn)){
                    tWn.addMember(sndr);
                    Format.CmdInfoFrmt.use().a(sndr,"You join the Town " + tWn.getName());
                }else{
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
                if(tLve.getMayor().getUniqueId() == sndr.getUniqueId()){
                    Format.CmdErrFrmt.use().a(sndr, "Mayor shall not leave the town!");
                    return true;
                }
                tLve.removePlayer(sndr);
                Format.CmdInfoFrmt.use().a(sndr,"You left the town " + tLve.getName());
                break;
        }
        return true;
    }

    public void nfs(Player sndr) {
        TownS.g().getClaim(sndr.getLocation().getChunk()).setFS(false, 0D);
    }

    public void fs(Player sndr, double cost) {
        TownS.g().getClaim(sndr.getLocation().getChunk()).setFS(true, cost);
    }

    public void create(Player p, String townName) {

        if(EconomyHandler.INSTANCE.getPlayerBalance(p) >= TownS.TownCost){
            if(EconomyHandler.INSTANCE.changePlayerBalance(p, -TownS.TownCost)){
                Town newT = new Town(townName, p);
                newT.claim(p.getLocation().getChunk(), p);
                newT.setWarpPoint(newT, "spawn", p.getLocation());
                newT.setSpawnChunk(newT, p.getLocation().getChunk());
                Format.AlrtFrmt.use().a(p, "Created new town with name -> " + townName);
            }else{
                Format.CmdErrFrmt.use().a(p, "An Error Occurred while trying to pay for the new Town.");
            }
        }else{
            Format.CmdErrFrmt.use().a(p, "Creating a new Town costs: "+TownS.TownCost.toString()+"$");
        }
    }

    public void deleteTown(Player sndr) {
        TownS.g().getTown(sndr).deleteTown();
    }

    public void claim(Player sndr) {

        if(EconomyHandler.INSTANCE.getPlayerBalance(sndr) >= TownS.PlotCost){
            if(EconomyHandler.INSTANCE.changePlayerBalance(sndr, -TownS.PlotCost)){
                TownS.g().getTown(sndr).claim(sndr.getLocation().getChunk(), sndr);
                Format.CmdInfoFrmt.use().a(sndr, "You have claimed land for your town! -> /towns map <- for neaby claims");
            }else{
                Format.CmdErrFrmt.use().a(sndr, "An Error Occurred while trying to pay for the new area.");
            }
        }else{
            Format.CmdErrFrmt.use().a(sndr, "Claiming a new Area costs: "+TownS.PlotCost.toString()+"$");
        }


    }

    public void claim(Player sndr, String plot_name) {
        if(EconomyHandler.INSTANCE.getPlayerBalance(sndr) >= TownS.PlotCost){
            if(EconomyHandler.INSTANCE.changePlayerBalance(sndr, -TownS.PlotCost)){
                TownS.g().getTown(sndr).claim(sndr.getLocation().getChunk(), sndr);
                TownS.g().getClaim(sndr.getLocation().getChunk()).setName(plot_name);
                Format.CmdInfoFrmt.use().a(sndr, "You have claimed land for your town! -> /towns map <- for neaby claims");
            }else{
                Format.CmdErrFrmt.use().a(sndr, "An Error Occurred while trying to pay for the new area.");
            }
        }else{
            Format.CmdErrFrmt.use().a(sndr, "Claiming a new Area costs: "+TownS.PlotCost.toString()+"$");
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

    public boolean isNumber(String number){
        try{
            Double.parseDouble(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
