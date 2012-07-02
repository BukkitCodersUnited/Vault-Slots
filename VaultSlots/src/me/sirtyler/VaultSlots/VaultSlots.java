package me.sirtyler.VaultSlots;

import java.io.File;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultSlots extends JavaPlugin {
	public static VaultSlots plugin;
	private FileConfiguration config;
	public CommandEx myExecutor;
	public Log log;
	public Permission permission = null;
    public Economy economy = null;
    public boolean debug = false;
    
    
	public void onEnable() {
		log = new Log(this);
		myExecutor = new CommandEx(this);
		setupPermissions();
		setupEconomy();
		getServer().getPluginManager().registerEvents(new SlotsListener(this), this);
		checkConfig();
		checkDebug();
		getCommand("slots").setExecutor(myExecutor);
	}
	
	private void checkConfig() {
		String mainDirectory = ("plugins/" + this.getDescription().getName());
		File file = new File(mainDirectory + File.separator + "config.yml");
		try{
			config = this.getConfig();
			if(!file.exists()) { 
				config.options().copyDefaults(true);
				this.saveConfig();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void checkDebug() {
		try {
			debug = config.getBoolean("debug");
		} catch(Exception e){
			if(log.info(e)) return;
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
