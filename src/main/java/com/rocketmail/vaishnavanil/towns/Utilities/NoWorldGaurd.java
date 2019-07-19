package com.rocketmail.vaishnavanil.towns.Utilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NoWorldGaurd implements WGUtil {
    @Override
    public boolean isPlotOverride(Location p) {
        return false;
    }
}
