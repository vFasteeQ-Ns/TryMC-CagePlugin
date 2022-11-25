package pl.vfasteeq.cageplugin.config;

/**
 * @author vFasteeQ
 * @since 25.11.2022
 */

import org.bukkit.configuration.file.FileConfiguration;
import pl.vfasteeq.cageplugin.MCPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private final MCPlugin plugin;

    public ConfigManager(MCPlugin plugin){
        this.plugin = plugin;
        setValues();
    }

    public static String world;
    public static List<Integer> coordinatesAttacker = new ArrayList<>();
    public static List<Integer> coordinatesDefender = new ArrayList<>();
    public static List<Integer> coordinatesSpawn = new ArrayList<>();

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
        world = "world";
        coordinatesAttacker = Arrays.asList(-10,95,0);
        coordinatesDefender = Arrays.asList(10,95,0);
        coordinatesSpawn = Arrays.asList(0,95,0);
    }
}