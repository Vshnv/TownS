package com.rocketmail.vaishnavanil.towns.Storage;

import org.bukkit.Bukkit;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.System.out;

public class SaveObject {
    public static boolean SaveObject(Object o,String path,String filename){
        Bukkit.broadcastMessage("Saving Data...");
        File f = new File(path,filename);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            FSTObjectOutput FSTo = new FSTObjectOutput(out);
            FSTo.writeObject(o);
            FSTo.flush();
            FSTo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Bukkit.broadcastMessage("Successfully Saved Data....");
        return true;
    }
}
