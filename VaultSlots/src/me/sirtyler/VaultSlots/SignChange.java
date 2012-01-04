package me.sirtyler.VaultSlots;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange extends BlockListener{
	public static VaultSlots plugin;
	private static Permission perm;
	public SignChange(VaultSlots instance) {
		plugin = instance;
	}
	
	public void onSignChange(SignChangeEvent event) {
		perm = plugin.permission;
		Player player = event.getPlayer();
		if(perm.playerHas(player, "vaultslots.build")) {
			if(event.getLine(0).equalsIgnoreCase("[Slots]")) {
				if(!event.getLine(1).isEmpty()){
					String slotName = event.getLine(1);
					event.setLine(0, ChatColor.GREEN + "[Slots]");
					event.setLine(1, ChatColor.GREEN + slotName);
					event.setLine(2, ChatColor.BLUE + "-|-|-");
					player.sendMessage(ChatColor.GOLD + "Slot Machine Made.");
				} else {
					player.sendMessage(ChatColor.GREEN + "[VaultSlots]: Missing Slot Machine Type.");
					player.sendMessage(ChatColor.GREEN + "[VaultSlots]: Use /slots help to see Commands and Sign Format.");
				}
			}
		} else {
			player.sendMessage(ChatColor.RED + "You Do Not Have Permission for That");
		}
	}
}
