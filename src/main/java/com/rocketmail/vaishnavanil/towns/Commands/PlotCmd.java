package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Economy.EconomyHandler;
import com.rocketmail.vaishnavanil.towns.MapGUI.FlagShow;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
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
            Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
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
            case "setname":
                if(args.length == 2){
                    String new_name = args[1];
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
                sndr.openInventory(FlagShow.get.create(sndr,TownS.g().getClaim(sndr.getLocation().getChunk())));
                Format.CmdInfoFrmt.use().a(sndr,"Opening Flag List for this claim!");
                break;
            case "access":
                if(TownS.g().hasTown(sndr)){
                    if(TownS.g().isClaimed(sndr_chunk)){
                        if(TownS.g().getTown(sndr).equals(TownS.g().getTown(sndr_chunk))){
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
                    if(args[1].equalsIgnoreCase("Build")){
                        if(args[2].equalsIgnoreCase("rank")){
                            String rnk_name = args[3];
                            if(TownS.g().getRank(rnk_name) == null){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find rank with name " + rnk_name);
                                return true;

                            }
                            Rank r = TownS.g().getRank(args[3]);
                            r.addPerm("Allow.Build:"+TownS.g().getClaim(sndr.getLocation().getChunk()).getOwnerID()+":"+TownS.getChunkID(sndr.getLocation().getChunk()));
                            Format.CmdInfoFrmt.use().a(sndr,"Now allowing rank " + rnk_name + " to build here!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]);
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }
                            Claim thisClm = TownS.g().getClaim(sndr.getLocation().getChunk());
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
                            r.addPerm("Allow.Container."+TownS.g().getClaim(sndr.getLocation().getChunk()).getOwnerID()+"."+TownS.getChunkID(sndr.getLocation().getChunk()));
                            Format.CmdInfoFrmt.use().a(sndr,"Now allowing rank " + rnk_name + " to use Containers here!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]);
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }
                            Claim thisClm = TownS.g().getClaim(sndr.getLocation().getChunk());
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
                    if(args[1].equalsIgnoreCase("Build")){
                        if(args[2].equalsIgnoreCase("rank")){
                            String rnk_name = args[3];
                            if(TownS.g().getRank(rnk_name) == null){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find rank with name " + rnk_name);
                                return true;
                            }
                            Rank r = TownS.g().getRank(args[3]);
                            r.removePerm("Allow.Build."+TownS.g().getClaim(sndr.getLocation().getChunk()).getOwnerID()+"."+TownS.getChunkID(sndr.getLocation().getChunk()));
                            Format.CmdInfoFrmt.use().a(sndr,"Not allowing rank " + rnk_name + " to build here anymore!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]);
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }
                            Claim thisClm = TownS.g().getClaim(sndr.getLocation().getChunk());
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
                            r.removePerm("Allow.Container."+TownS.g().getClaim(sndr.getLocation().getChunk()).getOwnerID()+"."+TownS.getChunkID(sndr.getLocation().getChunk()));
                            Format.CmdInfoFrmt.use().a(sndr,"Not allowing rank " + rnk_name + " to use Containers here anymore!");
                            return true;
                        }else if (args[2].equalsIgnoreCase("player")){
                            OfflinePlayer TBOplayer = Bukkit.getOfflinePlayer(args[3]);
                            if(!TBOplayer.hasPlayedBefore()){
                                Format.CmdErrFrmt.use().a(sndr,"Could not find player with name "+ args[3]);
                                return true;
                            }
                            Claim thisClm = TownS.g().getClaim(sndr.getLocation().getChunk());
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

}
