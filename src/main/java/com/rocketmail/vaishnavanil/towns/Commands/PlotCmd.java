package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Economy.EconomyHandler;
import com.rocketmail.vaishnavanil.towns.GUI.SI.FlagGUI;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.Messages.TownsMainMessage;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Rank;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlotCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            //TODO::PLAYER-ONLY COMMAND
            return true;
        }
        Player sndr = (Player) sender;
        Chunk sndr_chunk = sndr.getLocation().getChunk();
        if (!(args.length > 0)) {
            /*MSG ADDED N.E.A.*/
            TownsMainMessage.get.sendSubCommandPLOTS(sndr);
            return true;

        }

        String sub_cmd = args[0].toLowerCase();
        switch (sub_cmd) {
            case "claim":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;

                }
                if (!TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "Your mayor must first claim this chunk for your town!");
                    return true;
                }
                if (TownS.g().getTown(sndr.getLocation().getChunk()) != TownS.g().getTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to this town!");
                    return true;
                }
                if (!TownS.g().getClaim(sndr.getLocation().getChunk()).isFS()) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "This claim is currently Not For Sale! Please ask your mayor for help");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("plotclaim",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                    return true;
                }
                //TODO:: ADD ECO TRANSACTION
                Double plot_cost = TownS.g().getClaim(sndr_chunk).getPlotCost();
                if(EconomyHandler.INSTANCE.depositIntoTown(sndr, TownS.g().getTown(sndr), plot_cost)){
                    plotclaim(TownS.g().getClaim(sndr.getLocation().getChunk()), sndr);
                    Format.AlrtFrmt.use().a(sndr, "You have claimed this plot. Congrats!");
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Claiming this Plot costs: $"+plot_cost);
                }
                break;
            case "border":

                TownS.g().getTownPlayer(sndr).toggleBorder();
                if(TownS.g().getTownPlayer(sndr).showBorder())
                    Format.AlrtFrmt.use().a(sndr, "Plot Borders are now visible");
                else
                    Format.AlrtFrmt.use().a(sndr, "Plot Borders are now hidden");
                break;
            //FS
            case "fs":
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                if (isNumber(args[1])) {
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("fs",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
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
                } else {
                    Format.CmdErrFrmt.use().a(sndr, "You must specify a cost such as /towns fs 100");
                    return true;
                }
                break;
            case "forsale":
                if (!(args.length > 1)) {
                    /*MSG ADDED N.E.A.*/
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    return true;
                }
                if (isNumber(args[1])) {
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("fs", sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
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
                } else {
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
                if (!TownS.g().getTown(sndr).hasPermission("nfs", sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
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
            case "notforsale":
                if (!TownS.g().hasTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("nfs",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not have permission to use this command!");
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
            case "setname":
                if(args.length == 2){
                    String new_name = args[1];
                    if(new_name.toCharArray().length >10){
                        Format.CmdErrFrmt.use().a(sndr, "Town Name cannot be that long!");
                        return true;
                    }
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;

                    }
                    if (!TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "Your mayor must first claim this chunk for your town!");
                        return true;
                    }
                    if (TownS.g().getTown(sndr.getLocation().getChunk()) != TownS.g().getTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to this town!");
                        return true;
                    }
                    if (!TownS.g().getClaim(sndr_chunk).getOwner().equals(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "This Plot does not belong to you");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("plotsetname",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                        return true;
                    }
                    //TODO:: ADD ECO TRANSACTION
                    TownS.g().getClaim(sndr_chunk).setName(new_name);
                    Format.AlrtFrmt.use().a(sndr, "Successfully Changed to new Plot Name: "+new_name);
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                }
                break;
            case "flags":

                if (!TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You must stand in a Claimed Chunk for this command!");
                    return true;
                }
                if (TownS.g().getTown(sndr.getLocation().getChunk()) != TownS.g().getTown(sndr)) {
                    /*MSG ADDED A.I.T.*/
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to this town!");
                    return true;
                }
                if (!TownS.g().getTown(sndr).hasPermission("plotflags",sndr)) {
                    /*MSG ADDED A.I.T.*/
                    if(TownS.g().getClaim(sndr_chunk).getOwner().getUniqueId().equals(sndr.getUniqueId())){
                        sndr.openInventory(new FlagGUI("FlagList",TownS.g().getClaim(sndr_chunk)).get());
                        Format.CmdInfoFrmt.use().a(sndr,"Opening Flag List for this claim!");
                        break;
                    }else{
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                        return true;
                    }
                }else{
                    Claim c = TownS.g().getClaim(sndr.getLocation().getChunk());
                    sndr.openInventory(new FlagGUI("FlagGUI",c).get());
                    Format.CmdInfoFrmt.use().a(sndr,"Opening Flag List for this claim!");
                    break;
                }

            case "access":
                if(TownS.g().hasTown(sndr)){
                    if(TownS.g().isClaimed(sndr_chunk)){
                        if(TownS.g().getTown(sndr).equals(TownS.g().getTown(sndr_chunk))){
                            if (!TownS.g().getTown(sndr).hasPermission("plotaccess",sndr)) {
                                /*MSG ADDED A.I.T.*/
                                Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                                return true;
                            }
                            List<String> PBuildTrusted = new ArrayList<>();
                            List<String> PContainerTrusted = new ArrayList<>();

                            Town sndr_town = TownS.g().getTown(sndr);
                            Claim c_claim = TownS.g().getClaim(sndr_chunk);
                            /* Get Build Trusted Player Names */
                            for(UUID p_uuid: c_claim.getBuildTrusted()){
                                if(Bukkit.getOfflinePlayer(p_uuid).hasPlayedBefore()){
                                    PBuildTrusted.add(Bukkit.getOfflinePlayer(p_uuid).getName());
                                }
                            }
                            /* Get Container Trusted Player Names */
                            for(UUID p_uuid: c_claim.getContainerTrusted()){
                                if(Bukkit.getOfflinePlayer(p_uuid).hasPlayedBefore()){
                                    PContainerTrusted.add(Bukkit.getOfflinePlayer(p_uuid).getName());
                                }
                            }

                            sndr.sendMessage("Build Trusted: "+PBuildTrusted.toString());
                            sndr.sendMessage("Container Trusted: "+PContainerTrusted.toString());
                            sndr.sendMessage("Ranks Build Trusted: "+TownS.g().getClaim(sndr_chunk).getRankBuildTrusted().toString());
                            sndr.sendMessage("Ranks Container Trusted: "+TownS.g().getClaim(sndr_chunk).getRankContainerTrusted().toString());

                            /*  */

                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "You do not belong to this town!");
                        }
                    }else{
                        Format.CmdErrFrmt.use().a(sndr, "Your mayor must first claim this chunk for your town!");
                        return true;
                    }
                }else{
                    Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                    return true;
                }
                break;
            case "resetbuilds":
                if(TownS.g().isClaimed(sndr_chunk)){
                    if(TownS.g().getClaim(sndr_chunk).getOwner().getPlayer().equals(sndr)){

                        Double reset_cost = 20000.0;
                        if(EconomyHandler.INSTANCE.changePlayerBalance(sndr, -reset_cost)){
                            TownS.g().getTown(sndr_chunk).resetClaim(TownS.g().getClaim(sndr_chunk));
                            Format.AlrtFrmt.use().a(sndr, "You paid $"+reset_cost.toString()+" to reset your plot.");
                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "Resetting this Plot costs: $"+reset_cost.toString());
                        }
                        return true;
                    }
                }
                Format.CmdErrFrmt.use().a(sndr, "You lack the power to do this.");
                return true;
            case "allow":
                //TODO:: CLEAR UNNECCESSARY CLAIM TRUSTS ON ENABLE LATER ON ----> VAISHNAV
                if(args.length == 4){
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;

                    }
                    if (!TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "Your mayor must first claim this chunk for your town!");
                        return true;
                    }
                    if (TownS.g().getTown(sndr.getLocation().getChunk()) != TownS.g().getTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to this town!");
                        return true;
                    }
                    if (!TownS.g().getClaim(sndr_chunk).getOwner().equals(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "This Plot does not belong to you");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("plotallow",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                        return true;
                    }
                    Claim thisClm = TownS.g().getClaim(sndr.getLocation().getChunk());

                    if(args[1].equalsIgnoreCase("Build")){
                        if(args[2].equalsIgnoreCase("rank")){
                            String rnk_name = args[3];
                            if(TownS.g().getRank(rnk_name) == null){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find rank with name " + rnk_name);
                                return true;

                            }
                            Rank r = TownS.g().getRank(args[3]);
                            thisClm.BuildTrust(r);
                            Format.CmdInfoFrmt.use().a(sndr,"Now allowing rank " + rnk_name + " to build here!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]);
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }else{
                                if(TBOplayer.getUniqueId().equals(sndr.getUniqueId())){
                                    Format.CmdErrFrmt.use().a(sndr,"You cannot set access to yourself in the plot"+ args[3]);
                                    return true;
                                }
                            }
                            thisClm.BuildTrust(TBOplayer.getUniqueId());
                            Format.CmdInfoFrmt.use().a(sndr,"Now allowing player " + args[3] + " to build here!");

                        }else{
                            Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                            return true;
                        }
                    }else if(args[1].equalsIgnoreCase("container")){
                        if(args[2].equalsIgnoreCase("rank")){
                            String rnk_name = args[3];
                            if(TownS.g().getRank(rnk_name) == null){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find rank with name " + rnk_name);
                                return true;

                            }
                            Rank r = TownS.g().getRank(args[3]);
                            thisClm.ContainerTrust(r);
                            r.addPerm("Allow.Container."+TownS.g().getClaim(sndr.getLocation().getChunk()).getOwnerID()+"."+TownS.getChunkID(sndr.getLocation().getChunk()));
                            Format.CmdInfoFrmt.use().a(sndr,"Now allowing rank " + rnk_name + " to use Containers here!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]);
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }else{
                                if(TBOplayer.getUniqueId().equals(sndr.getUniqueId())){
                                    Format.CmdErrFrmt.use().a(sndr,"You cannot set access to yourself in the plot"+ args[3]);
                                    return true;
                                }
                            }
                            thisClm.ContainerTrust(TBOplayer.getUniqueId());
                            Format.CmdInfoFrmt.use().a(sndr,"Now allowing player " + args[3] + " to use Containers here!");

                        }else{
                            Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                            return true;
                        }
                    }else{
                        Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                        return true;
                    }
                }else{
                    Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                    return true;
                }
                break;
            case "disallow":
                //TODO:: CLEAR UNNECCESSARY CLAIM TRUSTS ON ENABLE LATER ON ----> VAISHNAV
                if(args.length == 4){
                    if (!TownS.g().hasTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
                        return true;

                    }
                    if (!TownS.g().isClaimed(sndr.getLocation().getChunk())) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "Your mayor must first claim this chunk for your town!");
                        return true;
                    }
                    if (TownS.g().getTown(sndr.getLocation().getChunk()) != TownS.g().getTown(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not belong to this town!");
                        return true;
                    }
                    if (!TownS.g().getClaim(sndr_chunk).getOwner().equals(sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "This Plot does not belong to you");
                        return true;
                    }
                    if (!TownS.g().getTown(sndr).hasPermission("plotdisallow",sndr)) {
                        /*MSG ADDED A.I.T.*/
                        Format.CmdErrFrmt.use().a(sndr, "You do not have permission use this command!");
                        return true;
                    }
                    Claim thisClm = TownS.g().getClaim(sndr.getLocation().getChunk());

                    if(args[1].equalsIgnoreCase("Build")){
                        if(args[2].equalsIgnoreCase("rank")){
                            String rnk_name = args[3];
                            if(TownS.g().getRank(rnk_name) == null){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find rank with name " + rnk_name);
                                return true;

                            }
                            Rank r = TownS.g().getRank(args[3]);
                            thisClm.unBuildTrust(r);
                            Format.CmdInfoFrmt.use().a(sndr,"Not allowing rank " + rnk_name + " to build here anymore!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]).getPlayer();
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }
                            thisClm.unBuildTrust(TBOplayer.getUniqueId());
                            Format.CmdInfoFrmt.use().a(sndr,"Not allowing player " + args[3] + " to build here anymore!");

                        }else{
                            Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                            return true;
                        }
                    }else if(args[1].equalsIgnoreCase("container")){
                        if(args[2].equalsIgnoreCase("rank")){
                            String rnk_name = args[3];
                            if(TownS.g().getRank(rnk_name) == null){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find rank with name " + rnk_name);
                                return true;

                            }
                            Rank r = TownS.g().getRank(args[3]);
                            thisClm.unContainerTrust(r);
                            Format.CmdInfoFrmt.use().a(sndr,"Not allowing rank " + rnk_name + " to use Containers here anymore!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]).getPlayer();
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }
                            thisClm.unContainerTrust(TBOplayer.getUniqueId());
                            Format.CmdInfoFrmt.use().a(sndr,"Not allowing player " + args[3] + " to use Containers here anymore!");

                        }else{
                            Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                            return true;
                        }
                    }else{
                        Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                        return true;
                    }
                }else{
                    Format.CmdErrFrmt.use().a(sndr,"Invalid format! use /plot trust <Build/Container> <rank/player> <rank name/player name>");
                    return true;
                }
                break;
        }


        return true;
    }

    public void plotclaim(Claim claim, Player player) {
        claim.setOwner(player);
        claim.setFS(false, 0D);
    }

    public boolean isNumber(String number){
        try{
            Double.parseDouble(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void nfs(Player sndr) {
        TownS.g().getClaim(sndr.getLocation().getChunk()).setFS(false, 0D);
    }

    public void fs(Player sndr, double cost) {
        TownS.g().getClaim(sndr.getLocation().getChunk()).setFS(true, cost);
    }

}
