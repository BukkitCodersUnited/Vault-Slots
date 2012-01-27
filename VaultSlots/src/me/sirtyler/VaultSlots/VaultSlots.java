package me.sirtyler.VaultSlots;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultSlots extends JavaPlugin{
	public static VaultSlots plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
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
		PluginDescriptionFile pdfFile = this.getDescription();
		if(log.sendInfo("[" + pdfFile.getName() + "]" + " version-" + pdfFile.getVersion() + " is now Disabled.")) return;
		this.logger.info("[" + pdfFile.getName() + "]" + " version-" + pdfFile.getVersion() + " is now Disabled.");
	}

	@Override
	public void onEnable() {
		deck = new Deck(this);
		log = new Log(this);
		myExecutor = new CommandEx(this);
		PluginDescriptionFile pdfFile = this.getDescription();
		setupPermissions();
		setupEconomy();
		getServer().getPluginManager().registerEvents(new SlotsListener(this), this);
		checkConfig();
		checkDebug();
		getCommand("slots").setExecutor(myExecutor);
		getCommand("vs").setExecutor(myExecutor);
		if(log.sendInfo("version-" + pdfFile.getVersion() + " is now Enabled.")) return;
		this.logger.info("[" + pdfFile.getName() + "]" + " version-" + pdfFile.getVersion() + " is now Enabled.");
	}
	
	private void checkConfig() {
		try{
			config = this.getConfig();
			config.options().copyDefaults(true);
			this.saveConfig();
		}catch(Exception e){
			if(log.sendExceptionInfo(e)) return;
			e.printStackTrace();
		}
	}
	
	private void checkDebug() {
		try {
			inDebug = config.getBoolean("debug");
		} catch(Exception e){
			if(log.sendExceptionInfo(e)) return;
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
