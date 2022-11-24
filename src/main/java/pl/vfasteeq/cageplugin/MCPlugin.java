package pl.vfasteeq.cageplugin;

import org.bukkit.plugin.java.JavaPlugin;
import pl.vfasteeq.cageplugin.command.CageCommand;

/**
 * @author vFasteeQ
 * @since 24.11.2022
 */

public class MCPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.registerListeners();
        MCPluginAPI.setPlugin(this);
    }

    private void registerListeners() {
        new CageCommand(this);
    }
}
