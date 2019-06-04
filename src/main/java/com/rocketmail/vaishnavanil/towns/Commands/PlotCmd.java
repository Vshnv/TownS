package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.Claim;
import com.rocketmail.vaishnavanil.towns.Towns.Town;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            //TODO::PLAYER-ONLY COMMAND
            return true;
        }
        Player sndr = (Player) sender;
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
                plotclaim(TownS.g().getClaim(sndr.getLocation().getChunk()), sndr);
                Format.AlrtFrmt.use().a(sndr, "You have claimed this plot. Congrats!");
                break;
            case "border":
                TownS.g().getTownPlayer(sndr).toggleBorder();
                Bukkit.broadcastMessage(  String.valueOf(TownS.g().getTownPlayer(sndr).showBorder())  );
                if(TownS.g().getTownPlayer(sndr).showBorder())
                    Format.AlrtFrmt.use().a(sndr, "Plot Borders are now visible");
                else
                    Format.AlrtFrmt.use().a(sndr, "Plot Borders are now hidden");
                break;
        }


        return true;
    }

    public void plotclaim(Claim claim, Player player) {
        claim.setOwner(player);
        claim.setFS(false, 0D);
    }
}
