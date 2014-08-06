package me.sirtyler.VaultSlots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEx implements CommandExecutor {
	public static VaultSlots plugin;
	private static Permission perm;
	private Map<Integer, List<String>> help = new HashMap<Integer, List<String>>();
	
	public CommandEx(VaultSlots _plugin) {
		plugin = _plugin;
		perm = _plugin.permission;
		buildHelp();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
		if(args.length >= 1) {
			String cmd = args[0];
			int page = 1;
			if(cmd.equalsIgnoreCase("help")) {
				if(!perm.has(sender, "vaultslots.commands.help")) return false;
				sender.sendMessage(ChatColor.BLUE + String.format("[VaultSlot Help Guide] #PAGE:%s#",page));
				sender.sendMessage((String[]) help.get(page).toArray());
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "[VaultSlots] Expected a command and recieved none.");
		return false;
	}
	
	private void buildHelp() {
		List<String> list = new ArrayList<String>();
		list.add(ChatColor.GREEN + "/slots help <page#> - Display Help pages");
		help.put(1, list);
	}
}
