package com.rocketmail.vaishnavanil.towns.Commands;

import com.rocketmail.vaishnavanil.towns.Messages.Format;
import com.rocketmail.vaishnavanil.towns.TownS;
import com.rocketmail.vaishnavanil.towns.Towns.TownPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownChatCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        commandSender.sendMessage("This function is currently disabled!");
        return true;

        /*if(commandSender instanceof Player){
            Player sndr  =  (Player) commandSender;
            if(TownS.g().hasTown(sndr)){
                TownPlayer townPlayer = TownS.g().getTownPlayer(sndr);
                townPlayer.toggleTownChat();
                if(townPlayer.townChatActive()){
                    Format.AlrtFrmt.use().a(sndr, "Switched to Town Chat");
                }else{
                    Format.AlrtFrmt.use().a(sndr, "Switched to Global Chat");
                }
            }else {
                Format.CmdErrFrmt.use().a(sndr, "You do not belong to a town yet!");
            }
        }
        return true;*/
    }
}
