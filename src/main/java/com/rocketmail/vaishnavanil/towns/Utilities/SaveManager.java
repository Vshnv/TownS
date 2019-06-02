package com.rocketmail.vaishnavanil.towns.Utilities;

import com.rocketmail.vaishnavanil.towns.TownS;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public enum SaveManager {
    use;



    public void Save(Object object,String sub,String FileName){
        if(!TownS.g().getDataFolder().exists()){
            TownS.g().getDataFolder().mkdirs();
        }
        File crte = new File(TownS.g().getDataFolder().getPath() + "\\"+sub);
        if(!crte.exists())crte.mkdirs();
        File f = new File(TownS.g().getDataFolder().getPath()+"\\" + sub ,FileName);
        if(!f.exists()){
            try {

                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            BukkitObjectOutputStream stream = new BukkitObjectOutputStream(new FileOutputStream(f));
            stream.writeObject(object);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
