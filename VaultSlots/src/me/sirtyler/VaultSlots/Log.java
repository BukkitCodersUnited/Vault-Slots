package me.sirtyler.VaultSlots;

import java.util.logging.Logger;

public class Log {
	public static VaultSlots plugin;
	private final Logger log;
	public Log(VaultSlots instance) {
		plugin = instance;
		log = plugin.getLogger();
	}
	public boolean sendDebugInfo(String str) {
		log.info("Debug:" + str);
		return true;
	}
	public boolean sendErrorInfo(String str) {
		log.info("Error:" + str);
		return true;
	}
	public boolean sendInfo(String str) {
		log.info(str);
		return true;
	}
	public boolean sendExceptionInfo(Exception e) {
		log.info("Exception:" + e);
		return true;
	}
}
