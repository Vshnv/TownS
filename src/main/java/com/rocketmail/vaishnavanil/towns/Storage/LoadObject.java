package com.rocketmail.vaishnavanil.towns.Storage;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;

import static java.lang.System.out;

public class LoadObject {
    public static Object LoadObject(String path,String filename){
        File f = new File(path,filename);
        if(!f.getParentFile().exists()){

            f.getParentFile().mkdirs();
            return null;
        }
        if(!f.exists()){
            try {
                f.createNewFile();

            } catch (IOException e) {
                out.println("IOExeption#1 in LOADOBJECT in TownS. Report to ADudeWithNoJob#1829");

                e.printStackTrace();
                return false;
            }
        }
        try {
            FileInputStream in = new FileInputStream(f);
            FSTObjectInput FSTi = new FSTObjectInput(in);
            Object o = FSTi.readObject();
            FSTi.close();
            return o;
        } catch (FileNotFoundException e) {
            out.println("FileNotFoundExeception#1 in LOADOBJECT in TownS. Report to ADudeWithNoJob#1829");
            return false;
        } catch (IOException e) {
            out.println("IOExeption#2 in LOADOBJECT in TownS. Report to ADudeWithNoJob#1829");
            return false;
        } catch (Exception e) {
            out.println("Exeption#1 in LOADOBJECT in TownS. Report to ADudeWithNoJob#1829");

        }
        return null;
    }
}
