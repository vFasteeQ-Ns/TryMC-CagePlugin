package pl.vfasteeq.cageplugin.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import pl.vfasteeq.cageplugin.MCPlugin;
import pl.vfasteeq.cageplugin.util.LocationUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vFasteeQ
 * @since 25.11.2022
 */

public class ConfigManager {

    private final MCPlugin plugin;

    public ConfigManager(MCPlugin plugin){
        this.plugin = plugin;
        setValues();
    }

    public static String spawnLocation;

    public static String cageFirstPoint;

    public static String cageSecondPoint;

    public void load(){
        try{
            this.plugin.saveDefaultConfig();
            FileConfiguration fileConfiguration = this.plugin.getConfig();
            for(Field field : ConfigManager.class.getFields()){
                if(Modifier.isPrivate(field.getModifiers())){
                    continue;
                }
                if(fileConfiguration.isSet("Config."+field.getName())){
                    field.set(null, fileConfiguration.get("Config."+field.getName()));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void save(){
        try{
            FileConfiguration fileConfiguration = this.plugin.getConfig();
            for(Field field : ConfigManager.class.getFields()){
                if(Modifier.isPrivate(field.getModifiers())){
                    continue;
                }
                fileConfiguration.set("Config."+field.getName(), field.get(null));
            }
            this.plugin.saveConfig();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void reload(){
        this.plugin.reloadConfig();
        load();
        save();
    }

    private void setValues(){
        spawnLocation = LocationUtil.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation());
        cageFirstPoint = LocationUtil.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation());
        cageSecondPoint = LocationUtil.locationToString(Bukkit.getWorlds().get(0).getSpawnLocation());
    }
}