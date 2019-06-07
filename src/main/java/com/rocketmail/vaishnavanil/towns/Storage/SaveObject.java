package com.rocketmail.vaishnavanil.towns.Storage;

import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.System.out;

public class SaveObject {
    public static boolean SaveObject(Object o,String path,String filename){
        File f = new File(path,filename);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                out.println("IOExeption#1 in SAVEOBJECT in TownS. Report to ADudeWithNoJob#1829");

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
            out.println("FileNotFoundExeception#1 in SAVEOBJECT in TownS. Report to ADudeWithNoJob#1829");
            return false;
        } catch (IOException e) {
            out.println("IOExeption#2 in SAVEOBJECT in TownS. Report to ADudeWithNoJob#1829");
            return false;
        }
        return true;
    }
}
