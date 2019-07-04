package com.rocketmail.vaishnavanil.towns.Utilities;

import com.rocketmail.vaishnavanil.towns.Configurations.ConfigManager;
import com.rocketmail.vaishnavanil.towns.Economy.EcoUpkeep;
import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.scheduler.BukkitRunnable;

public enum DailyBackupHandler {
    get;

    public void runTimer(){
        TownS.g().regTask(new BukkitRunnable(){

            @Override
            public void run() {


                if(ConfigManager.get.shouldBackUP()){
                    TownS.g().BackupData();
                    EcoUpkeep.EcoUK.doUpkeep();
                }else if(ConfigManager.get.nearBackUP()){
                    EcoUpkeep.EcoUK.warnUpkeep();
                }
                TownS.g().saveTown();
                TownS.g().saveClaims();
            }
        }.runTaskTimer(TownS.g(),10000,36000));
    }
}
