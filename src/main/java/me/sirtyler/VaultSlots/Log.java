package me.sirtyler.VaultSlots;

import java.util.logging.Logger;

public class Log {
	public static VaultSlots plugin;
	private final Logger log;
	private boolean debug;
	public Log(VaultSlots instance) {
		plugin = instance;
		log = plugin.getLogger();
		debug = plugin.debug;
	}
	public boolean debug(String str) {
		if(!debug) return false;
		info("Debug:" + str);
		return true;
	}
	public boolean error(String str) {
		info("Error:" + str);
		return true;
	}
	public boolean info(String str) {
		log.info(str);
		return true;
	}
	public boolean info(Exception e) {
		info("Exception:" + e.getLocalizedMessage());
		return true;
	}
}
