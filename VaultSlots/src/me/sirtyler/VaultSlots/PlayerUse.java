package me.sirtyler.VaultSlots;

import java.util.Random;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerUse extends PlayerListener{
		public static VaultSlots plugin;
		private FileConfiguration config; 
		public Permission perm = null;
	    public Economy eco = null;
		Random rand = new Random();
		public PlayerUse(VaultSlots instance) {
			plugin = instance;
		}
		@SuppressWarnings("deprecation")
		public void onPlayerInteract(PlayerInteractEvent event) { 
			Player player = event.getPlayer();
			config = plugin.getConfig();
			Action action = event.getAction();
			Block block = event.getClickedBlock();
			perm = plugin.permission;
			eco = plugin.economy;
			if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;
			if(block.getType() == Material.SIGN || (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)) {
				if(action == Action.RIGHT_CLICK_BLOCK && (block.getType() == Material.SIGN || (block.getType() == Material.SIGN_POST  || block.getType() == Material.WALL_SIGN))) {
					if (block.getState() instanceof Sign) {
						Sign sign = (Sign) block.getState();
						String name = ChatColor.stripColor(sign.getLine(0));
						if(name.equalsIgnoreCase("[Slots]")) {
							if(perm.playerHas(player, "vaultslots.access")) {
								String type = ChatColor.stripColor(sign.getLine(1));
								if(config.contains("type." + type)){
									String label = config.getString("type." + type + ".labels");
									String[] labels = label.split(":");
									String jackpotLID = config.getString("type." + type + ".jackpotLabel");
									String payoutID = config.getString("type." + type + ".payout");
									String[] payout = null;
									int payout2 = 0;
									int payoutItm = 0;
									int payoutNum = 0;
									boolean Pecon = false;
									if(payoutID.contains("$")) {
										Pecon = true;
										payout2 = Integer.parseInt(payoutID.substring(1));
									} else {
										Pecon = false;
										payout = payoutID.split(":");
										payoutItm = Integer.parseInt(payout[0]);
										payoutNum = Integer.parseInt(payout[1]);
									}
									String jackpotID = config.getString("type." + type + ".jackpot");
									String[] jackpot = null;
									int jackpot2 = 0;
									int jackpotItm = 0;
									int jackpotNum = 0;
									boolean Jecon = false;
									if(jackpotID.contains("$")) {
										Jecon = true;
										jackpot2 = Integer.parseInt(jackpotID.substring(1));
									} else {
										Jecon = false;
										jackpot = jackpotID.split(":");
										jackpotItm = Integer.parseInt(jackpot[0]);
										jackpotNum = Integer.parseInt(jackpot[1]);
									}
									String costId = config.getString("type." + type + ".cost");
									String[] cost = null;
									int cost2 = 0;
									int costItm = 0;
									int costNum = 0;
									boolean Cecon = false;
									if(costId.contains("$")) {
										Cecon = true;
										cost2 = Integer.parseInt(costId.substring(1));
									} else {
										Cecon = false;
										cost = costId.split(":");
										costItm = Integer.parseInt(cost[0]);
										costNum = Integer.parseInt(cost[1]);
									}
									boolean econ = false;
									if((Pecon == true && Jecon == true) && (Cecon == true)) {
										econ = true;
									}
									if(econ == false) {
										if(player.getInventory().contains(costItm,costNum)) {
											ItemStack costing = new ItemStack(costItm,costNum);
											player.getInventory().removeItem(costing);
											player.sendMessage(ChatColor.BLUE + "You payed " + costing.getAmount() + " of " + costing.getType());
											int rNum1 = rand.nextInt(labels.length) + 1;
											int rNum2 = rand.nextInt(labels.length) + 1;
											int rNum3 = rand.nextInt(labels.length) + 1;
											if(rNum1 == rNum2 && rNum2 == rNum3) {
												String rNum = labels[(rNum1-1)];
												try {
													Inventory i = player.getInventory();
													if(rNum.equals(jackpotLID)) {
														ItemStack give = new ItemStack(jackpotItm,jackpotNum);
														player.sendMessage(ChatColor.GOLD + "You Won the Jackpot of " + give.getAmount() + " of " + give.getType());
														i.addItem(give);
													} else {
														ItemStack give = new ItemStack(payoutItm,payoutNum);
														player.sendMessage(ChatColor.GOLD + "You Won " + give.getAmount() + " of " + give.getType());
														i.addItem(give);
													}
												} catch (Exception e) {
													plugin.logger.info("[VaultSlots] Error: " + e);
												}
											} else {
												player.sendMessage(ChatColor.GRAY + "Aw, you Lost.");
											}
											String rNumA = labels[(rNum1-1)];
											String rNumB = labels[(rNum2-1)];
											String rNumC = labels[(rNum3-1)];
											sign.setLine(2, ChatColor.BLUE + "" + rNumA + "|" + rNumB + "|" + rNumC);
											sign.update();
											player.updateInventory();
										} else {
											player.sendMessage(ChatColor.GRAY + "Insufficient Funds");
										}
									} else if (econ == true) {
										if(eco.has(player.getName(), cost2)) {
											eco.withdrawPlayer(player.getName(), cost2);
											player.sendMessage(ChatColor.BLUE + "You payed $" + cost2 + " and pulled the lever...");
											int rNum1 = rand.nextInt(labels.length) + 1;
											int rNum2 = rand.nextInt(labels.length) + 1;
											int rNum3 = rand.nextInt(labels.length) + 1;
											if((rNum1 == rNum2) && (rNum2 == rNum3)) {
												String rNum = labels[(rNum1-1)];try {
													if(rNum.equals(jackpotLID)) {
														eco.depositPlayer(player.getName(), jackpot2);
														player.sendMessage(ChatColor.GOLD + "You Won the Jackpot of $" + jackpot2);
													} else {
														eco.depositPlayer(player.getName(), payout2);
														player.sendMessage(ChatColor.GOLD + "You Won $" + payout2);
													}
												} catch (Exception e) {
													plugin.logger.info("[VaultSlots] Error: " + e);
												}
											} else {
												player.sendMessage(ChatColor.GRAY + "Aw, you Lost.");
											}
											String rNumA = labels[(rNum1-1)];
											String rNumB = labels[(rNum2-1)];
											String rNumC = labels[(rNum3-1)];
											sign.setLine(2, ChatColor.BLUE + "" + rNumA + "|" + rNumB + "|" + rNumC);
											sign.update();
										} else {
											player.sendMessage(ChatColor.GRAY + "Insufficient Funds");
										}
									} else {
										player.sendMessage(ChatColor.RED + "Error, contact Server Admin.");
									}
								} else {
									player.sendMessage(ChatColor.RED + "Slots Type Unavaible, Contact Server Admin.");
								}
							} else {
								player.sendMessage(ChatColor.RED + "You Do Not Have Permission for That");
							}
						}
					}
				}
			}
		}
}