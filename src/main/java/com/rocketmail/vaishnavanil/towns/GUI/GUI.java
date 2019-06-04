package com.rocketmail.vaishnavanil.towns.GUI;

public enum GUI {
    YesNo(new YesNoInterface());
    SimpleInterface ui;
    GUI(SimpleInterface Sif){
        ui = Sif;
    }
}
