package pl.vfasteeq.cageplugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.vfasteeq.cageplugin.config.ConfigManager;
import pl.vfasteeq.cageplugin.handler.CageHandler;

/**
 * @author vFasteeQ
 * @since 24.11.2022
 */

public class MCPlugin extends JavaPlugin {

    @Getter
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.registerListener();
        this.initConfiguration();
        MCPluginAPI.setPlugin(this);
    }

    private void registerListener() {
        new CageHandler(this);
    }

    private void initConfiguration(){
        this.configManager = new ConfigManager(this);
        this.configManager.reload();
    }
}
