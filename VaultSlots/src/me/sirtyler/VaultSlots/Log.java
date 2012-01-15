package me.sirtyler.VaultSlots;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;

public class Log {
	public static VaultSlots plugin;
	private final Logger log;
	private final PluginDescriptionFile pd;
	public Log(VaultSlots instance) {
		plugin = instance;
		log = Logger.getLogger("Minecraft");
		pd = plugin.getDescription();
	}
	public boolean sendDebugInfo(String str) {
		log.info("[" + pd.getName()+ "]Debug:" + str);
		return true;
	}
	public boolean sendErrorInfo(String str) {
		log.info("[" + pd.getName()+ "]Error:" + str);
		return true;
	}
	public boolean sendInfo(String str) {
		log.info("[" + pd.getName()+ "]:" + str);
		return true;
	}
	public boolean sendExceptionInfo(Exception e) {
		log.info("[" + pd.getName()+ "]Exception:" + e);
		return true;
	}
}
