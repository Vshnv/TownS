package com.rocketmail.vaishnavanil.towns.GUI.SI;

import com.rocketmail.vaishnavanil.towns.GUI.Function;
import com.rocketmail.vaishnavanil.towns.GUI.FunctionRunner;
import com.rocketmail.vaishnavanil.towns.GUI.StackFunc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class SimpleInterface {
    StackFunc[] inv;
    int size;
    String name;
    public SimpleInterface(String n){
name = n;

    }
    public void init(){
        if(inv.length>0){
            if(inv.length >=9) {
                size = (inv.length - inv.length % 9)+9;
            }else{
                size = 9;
            }
        }else {

            size = inv.length;
        }
        FunctionRunner runner = FunctionRunner.get();
        for(StackFunc func:inv){
            runner.register(func.getStack(),func.getFunction(),name);
        }
    }
    public Inventory get(){
        Inventory gui = Bukkit.createInventory(null,size,name);
        for(int i = 0;i<=inv.length-1;i++){
            gui.setItem(i,inv[i].getStack());
        }
        return gui;
    }

}
