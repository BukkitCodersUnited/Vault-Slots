package me.sirtyler.VaultSlots;

import java.io.File;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultSlots extends JavaPlugin{
	public static VaultSlots plugin;
	private FileConfiguration config;
	public CommandEx myExecutor;
	public Deck deck;
	public Log log;
	public Permission permission = null;
    public Economy economy = null;
    public boolean inDebug = false;
	public VaultSlots() {
		super();
	}
	
	@Override
	public void onDisable() {
		//Disabled
	}

	@Override
	public void onEnable() {
		deck = new Deck(this);
		log = new Log(this);
		myExecutor = new CommandEx(this);
		setupPermissions();
		setupEconomy();
		getServer().getPluginManager().registerEvents(new SlotsListener(this), this);
		checkConfig();
		checkDebug();
		getCommand("slots").setExecutor(myExecutor);
		getCommand("vs").setExecutor(myExecutor);
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
			inDebug = config.getBoolean("debug");
		} catch(Exception e){
			if(log.sendExceptionInfo(e)) return;
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
