package com.rocketmail.vaishnavanil.towns.MapGUI.ItemName;

public class Normalized implements NS {
    @Override
    public String getModifications(String input) {
        char[] arChar = input.toLowerCase().toCharArray();
        arChar[0] = input.toUpperCase().charAt(0);
        return String.copyValueOf(arChar);
    }
}
