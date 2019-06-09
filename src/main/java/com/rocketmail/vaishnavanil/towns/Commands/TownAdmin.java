package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.RegenBuilder;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import com.rocketmail.vaishnavanil.towns.Utilities.LoadManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sun.security.krb5.Config;

public class TownAdmin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player sndr = (Player) commandSender;
            Chunk sndr_chunk = sndr.getLocation().getChunk();

            if (!(args.length > 0)) {
                /*MSG ADDED .E.A.*/
                Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                return true;
            }
            String subcmd = args[0].toLowerCase();

            switch (subcmd){



                /*REGEN*/
                case "regen":
                    if (ConfigManager.get.isRegenWorld(sndr.getLocation().getWorld())) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new RegenBuilder((Material[][][]) LoadManager.get.loadObject("ChunkSaves", sndr_chunk.getX() + "TT" + sndr_chunk.getZ() + "TT" + sndr_chunk.getWorld().getName() + ".dat"), sndr_chunk);
                            }
                        }.runTask(TownS.g());
                        Format.AlrtFrmt.use().a(sndr, "Performing Regen on Chunk at location.");
                    } else {
                        Format.CmdErrFrmt.use().a(sndr, "Regen Disabled in this world");

                    }

                    break;
                /*REGEN*/

                /*UNCLAIM*/
                case "unclaim":
                    if(TownS.g().isClaimed(sndr_chunk)){
                        TownS.g().getTown(sndr_chunk).unclaim(sndr_chunk);
                        Format.AlrtFrmt.use().a(sndr, "Unclaimed Current Chunk");
                        return true;
                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "Chunk at this location is not claimed.");
                    }
                    break;
                /*UNCLAIM*/

                case "setspawn":
                    if(TownS.g().isClaimed(sndr_chunk)){
                        Town town = TownS.g().getTown(sndr_chunk);
                        town.setWarpPoint(town, "spawn", sndr.getLocation());
                        Format.AlrtFrmt.use().a(sndr, "Successfully Set Spawn Point for Town: "+town.getName());
                    }else{
                        Format.CmdErrFrmt.use().a(sndr, "Chunk at this location is not claimed.");
                    }
                    break;

                /*SETBALANCE*/
                case "setbalance":
                    if(args.length==3){
                        if(isNumber(args[2])){
                            String town_name = args[1];
                            Double balance = Double.parseDouble(args[2]);
                            if(TownS.g().getTown(town_name)!=null){
                                TownS.g().getTown(town_name).setTownBalance(balance);
                                Format.AlrtFrmt.use().a(sndr, "Set Balance of Town: "+town_name+ ". New Balance: $"+TownS.g().getTown(town_name).getTownBalance().toString());
                            }else{
                                Format.CmdErrFrmt.use().a(sndr, "Town Not Found");
                            }
                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "Invalid Format: /ta setbalance town <number>");
                        }
                    }else{ Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!"); }
                    break;
                /*SETBALANCE*/

                case "setname":
                    if(args.length==3){
                        String town_name = args[1];
                        String new_town_name = args[2];
                            if(TownS.g().getTown(town_name)!=null){
                                TownS.g().getTown(town_name).setName(new_town_name);
                                Format.AlrtFrmt.use().a(sndr, "Successfully changed Town Name to: "+new_town_name);
                            }else{
                                Format.CmdErrFrmt.use().a(sndr, "Town Not Found");
                            }

                    }else{ Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!"); }
                    break;

                case "setplotname":
                    if(args.length==2){
                        String new_name = args[1];
                        if(TownS.g().isClaimed(sndr_chunk)){
                            TownS.g().getClaim(sndr_chunk).setName(new_name);
                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "Plot Not Claimed");
                        }
                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    }
                    break;
                case "setplotowner":
                    if(args.length==2){
                        String player_name = args[1];
                        if(Bukkit.getPlayer(player_name)!=null){
                            if(TownS.g().isClaimed(sndr_chunk)){
                                TownS.g().getClaim(sndr_chunk).setOwner(Bukkit.getPlayer(player_name));
                                Format.AlrtFrmt.use().a(sndr, "Successfully changed Plot owner to: "+player_name);
                            }else{
                                Format.CmdErrFrmt.use().a(sndr, "Plot Not Claimed");
                            }
                        }else{
                            Format.CmdErrFrmt.use().a(sndr, "Cannot find that Player.");
                        }
                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    }
                    break;
                case "deletetown":
                    if(args.length==2){
                        String town_name = args[1];
                        if(TownS.g().getTown(town_name) != null){
                            TownS.g().getTown(town_name).deleteTown();
                            Format.AlrtFrmt.use().a(sndr, "Successfully deleted Town: "+town_name);
                        }else {
                            Format.CmdErrFrmt.use().a(sndr, "Town Not Found");
                        }
                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    }
                    break;
                case "settownowner":
                    if(args.length==3){
                        String town_name = args[1];
                        String player_name = args[2];
                        if(TownS.g().getTown(town_name)!=null){
                            if(Bukkit.getPlayer(player_name)!=null){
                                if(TownS.g().getTown(town_name).setOwner(Bukkit.getPlayer(player_name))){
                                    Format.AlrtFrmt.use().a(sndr, "Successfully set Mayor of Town: "+town_name+" to: "+player_name);
                                }else {
                                    Format.CmdErrFrmt.use().a(sndr, "Player does not belong to that town.");
                                }
                            }else {
                                Format.CmdErrFrmt.use().a(sndr, "Player Not Found");
                            }
                        }else {
                            Format.CmdErrFrmt.use().a(sndr, "Town Not Found");
                        }
                    }else {
                        Format.CmdErrFrmt.use().a(sndr, "Not enough arguments!");
                    }
                    break;
                case"savetowns":
                    TownS.g().saveTown();
                    Format.CmdInfoFrmt.use().a(sndr,"Saved all town Data!");
                    break;
                case"saveclaims":
                    TownS.g().saveClaims();
                    Format.CmdInfoFrmt.use().a(sndr,"Saved all claim data");
                    break;
            }
        }
        return true;
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
