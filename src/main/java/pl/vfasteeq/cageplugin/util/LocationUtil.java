package pl.vfasteeq.cageplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {
    public static Location locationFromString(String s) {
        String[] ss = s.split(",");
        Location l = new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        l.setWorld(Bukkit.getWorld(ss[0]));
        l.setX(Double.parseDouble(ss[1]));
        l.setY(Double.parseDouble(ss[2]));
        l.setZ(Double.parseDouble(ss[3]));
        l.setYaw(Float.parseFloat(ss[4]));
        l.setPitch(Float.parseFloat(ss[5]));
        return l;
    }

    public static String locationToString(Location l) {
        return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
    }
}
