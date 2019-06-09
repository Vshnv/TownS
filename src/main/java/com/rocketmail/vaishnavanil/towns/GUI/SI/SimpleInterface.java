package com.rocketmail.vaishnavanil.towns.GUI.SI;


import com.rocketmail.vaishnavanil.towns.GUI.FunctionRunner;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import com.rocketmail.vaishnavanil.towns.MapGUI.ItemName.NameStyle;
import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public abstract class SimpleInterface {
    StackFunc[] inv;
    int size;
    String name;
    Inventory gui;
    boolean removeESett = false;
    public  SimpleInterface(String n){
        name = NameStyle.DESIGNED.use(n);
    }
    public void init(){

        if(inv.length>9){
            if(inv.length%9 == 0){
                size = inv.length;
            }else {
                size = inv.length + (9 - inv.length % 9);
            }
        }else{
            size =9;
        }
        FunctionRunner runner = FunctionRunner.get();
        for(StackFunc func:inv){
            runner.register(func.getStack(),func.getFunction(),name,removeESett);
        }
    }

    public Inventory get(){
        gui = Bukkit.createInventory(null,size,name);
        for(int i = 0;i<=inv.length-1;i++){
            gui.setItem(i,inv[i].getStack());
        }
        return gui;
    }

    public void update(Player player){
        for(int i = 0;i<=inv.length-1;i++){
            gui.setItem(i,inv[i].getStack());
        }
        player.updateInventory();
    }

}
