package com.rocketmail.vaishnavanil.towns.Utilities;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public enum LoadManager {
    get;

    public Object loadObject(String sub,String file){
        if(!TownS.g().getDataFolder().exists()){
            TownS.g().getDataFolder().mkdirs();
        }
        File f = new File(TownS.g().getDataFolder().getPath()+"\\" + sub,file);
        if(!f.exists())return null;

        try {
            BukkitObjectInputStream stream = new BukkitObjectInputStream(new FileInputStream(f));
            Object o = stream.readObject();
            stream.close();
            return o;
        } catch(EOFException e){
            //IGNORE
        } catch (IOException e) {
            //IGNORE
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
