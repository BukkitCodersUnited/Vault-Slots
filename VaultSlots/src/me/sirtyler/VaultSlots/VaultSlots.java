package me.sirtyler.VaultSlots;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultSlots extends JavaPlugin{
	public static VaultSlots plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	private FileConfiguration config;
	private final SignChange signchanger = new SignChange(this);
	private final PlayerUse interact = new PlayerUse(this);
	private CommandEx myExecutor;
	public Permission permission = null;
    public Economy economy = null;

	public VaultSlots() {
		super();
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "]" + " version-" + pdfFile.getVersion() + " is now Disabled.");	
	}

	@Override
	public void onEnable() {
		myExecutor = new CommandEx(this);
		PluginManager pm = getServer().getPluginManager();
		PluginDescriptionFile pdfFile = this.getDescription();
		setupPermissions();
		setupEconomy();
		pm.registerEvent(Event.Type.SIGN_CHANGE, this.signchanger, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.interact, Event.Priority.Highest, this);
		checkConfig();
		getCommand("slots").setExecutor(myExecutor);
		getCommand("vs").setExecutor(myExecutor);
		this.logger.info("[" + pdfFile.getName() + "]" + " version-" + pdfFile.getVersion() + " is now Enabled.");
	}

	private void checkConfig() {
		try{
			config = this.getConfig();
			config.options().copyDefaults(true);
			this.saveConfig();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    private Boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private Boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
}
