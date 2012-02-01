package me.sirtyler.VaultSlots;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEx implements CommandExecutor{
	public static VaultSlots plugin;
	private static Deck deck;
	private static Permission perm;
	public Map<String, Boolean> blackjack = new HashMap<String, Boolean>();
	Random rand = new Random();
	private boolean useDebug = false;
	private static Log logger;
	public CommandEx(VaultSlots instance) {
		plugin = instance;
		deck = plugin.deck;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		logger = plugin.log;
		useDebug = plugin.inDebug;
		perm = plugin.permission;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 1){
                String cmd = args[0].toString();
                if(cmd.equalsIgnoreCase("help")) {
            		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                	player.sendMessage(ChatColor.BLUE + "[VaultSlot Help Guide] #Page 1#");
					player.sendMessage(ChatColor.GOLD + "Originaly Created for The Vault RP Server!");
					player.sendMessage(ChatColor.GREEN + "/slots or /vs");
					player.sendMessage(ChatColor.GREEN + "/slots help <page#> - Displays this and other help pages.");
					player.sendMessage(ChatColor.GREEN + "/slots test - Check if VaultSlots is working.");
					player.sendMessage(ChatColor.GREEN + "/slots roll - Roll random number between 1-4.");
					player.sendMessage(ChatColor.GREEN + "/slots card - Pull random card from a Deck.");
					player.sendMessage(ChatColor.GREEN + "/slots blackjack <bet> - Play a game of Blackjack with bet, the default is $10.");
					return true;
                } else if(cmd.equalsIgnoreCase("test")){
                	if(perm.playerHas(player, "vaultslots.test")) {
                		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                		sender.sendMessage(ChatColor.GOLD + "[VaultSlots] is Working!");
                		return true;
                	}else {
            			sender.sendMessage(ChatColor.RED + "[VaultSlots]: You Do Not Have Permission for That.");
            			return true;
            		}
                } else if(cmd.equalsIgnoreCase("roll")){
                	 if(perm.playerHas(player, "vaultslots.roll")) {
                 		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                		 int pickedNumber = rand.nextInt(4) + 1;
                		 sender.sendMessage(ChatColor.GREEN + "" + pickedNumber);
                		 return true;
                	} else {
            			sender.sendMessage(ChatColor.RED + "[VaultSlots]: You Do Not Have Permission for That.");
            			return true;
                	}
                } else if(cmd.equalsIgnoreCase("card")) {
                	if(perm.playerHas(player, "vaultslots.card")) {
                		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                		player.sendMessage(ChatColor.GOLD + deck.drawCard());
                		return true;
                	} else {
                		sender.sendMessage(ChatColor.RED + "[VaultSlots]: You Do Not Have Permission for That.");
            			return true;
                	}
                } else if(cmd.equalsIgnoreCase("poker")) {
                	if(perm.playerHas(player, "vaultslots.poker.access")) {
                		player.sendMessage(ChatColor.GRAY + "Multiplayer Poker coming soon");
                		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                		deck.Poker(player);
                		return true;
                	}
                } else if(cmd.equalsIgnoreCase("blackjack")) {
					if(perm.playerHas(player, "vaultslots.blackjack.access")) {
						player.sendMessage(ChatColor.GRAY + "Multiplayer Blackjack coming soon");
                		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
						if(blackjack.containsKey(player.getName())) {
							player.sendMessage(ChatColor.BLUE + "You are already in a game.");
						} else {
							try {
								blackjack.put(player.getName(), true);
								deck.setGame(player, 10);
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(ChatColor.RED + "[VaultSlots] Error Contact Server Admin.");
								return true;
							}
						}
						return true;
					} 
                } else if(cmd.equalsIgnoreCase("hit")) {
                	if(perm.playerHas(player, "vaultslots.blackjack.access")) {
                		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                		if(blackjack.containsKey(player.getName())) {
                			deck.BlackJack(player, "hit");
                			return true;
                		} else {
                			player.sendMessage(ChatColor.BLUE + "Join a Blackjack game first.");
                			return true;
                		}
                	} else {
                		sender.sendMessage(ChatColor.RED + "[VaultSlots]: You Do Not Have Permission for That.");
						return true;
                	}
                } else if(cmd.equalsIgnoreCase("stay")) {
                	if(perm.playerHas(player, "vaultslots.blackjack.access")) {
                		if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " Command Length:" + args.length);
                		if(blackjack.containsKey(player.getName())) {
                			deck.BlackJack(player, "stay");
                			return true;
                		} else {
                			player.sendMessage(ChatColor.BLUE + "Join a Blackjack game first.");
                			return true;
                		}
                	} else {
                		sender.sendMessage(ChatColor.RED + "[VaultSlots]: You Do Not Have Permission for That.");
						return true;
                	}
                }
			} else if(args.length == 2) {
				String cmd = args[0].toString();
				String arg = args[1].toString();
				if(cmd.equalsIgnoreCase("help")) {
					if(arg.equalsIgnoreCase("1")) {
						if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " " + arg + " Command Length:" + args.length);
						player.sendMessage(ChatColor.BLUE + "[VaultSlot Help Guide] #Page 1#");
						player.sendMessage(ChatColor.GOLD + "Originaly Created for The Vault RP Server!");
						player.sendMessage(ChatColor.GREEN + "/slots or /vs");
						player.sendMessage(ChatColor.GREEN + "/slots help <page#> - Displays this and other help pages.");
						player.sendMessage(ChatColor.GREEN + "/slots test - Check if VaultSlots is working.");
						player.sendMessage(ChatColor.GREEN + "/slots roll - Roll random number between 1-4.");
						player.sendMessage(ChatColor.GREEN + "/slots card - Pull random card from a Deck.");
						player.sendMessage(ChatColor.GREEN + "/slots blackjack <bet> - Play a game of Blackjack with bet, the default is $10.");
						return true;
					} else if(arg.equalsIgnoreCase("2")) {
						if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " " + arg + " Command Length:" + args.length);
						player.sendMessage(ChatColor.BLUE + "[VaultSlot Help Guide] #Page 2#");
						player.sendMessage(ChatColor.GOLD + "Originaly Created for The Vault RP Server!");
						player.sendMessage(ChatColor.GREEN + "/slots hit - Used in a game of Blackjack to hit.");
						player.sendMessage(ChatColor.GREEN + "/slots stay - Used in a game of Blackjack to stay.");
						player.sendMessage(ChatColor.GREEN + "/slots poker - Play a game of Poker (in progress)");
						player.sendMessage(ChatColor.GREEN + "Slot Machine Format:");
						player.sendMessage(ChatColor.GREEN + "- Line1: [Slots]");
						player.sendMessage(ChatColor.GREEN + "- Line2: Type");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "Sorry, no Help page Avaiable.");
						return true;
					}
				} else if(cmd.equalsIgnoreCase("blackjack")) {
					if(perm.playerHas(player, "vaultslots.blackjack.access")) {
						if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command: " + cmd + " " + arg + " Command Length:" + args.length);
						if(blackjack.containsKey(player.getName())) {
							player.sendMessage(ChatColor.BLUE + "You are already in a game.");
						} else {
							try {
								int i = Integer.parseInt(arg);
								blackjack.put(player.getName(), true);
								deck.setGame(player, i);
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(ChatColor.RED + "[VaultSlots] Error: Expected Number arguement.");
								player.sendMessage(ChatColor.RED + "[VaultSlots] Use /slots help for help.");
								return true;
							}
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "[VaultSlots]: You Do Not Have Permission for That.");
						return true;
					}
				}
			} else {
				if(useDebug) logger.sendDebugInfo("Command Sender:" + player.getName() + " Command Length :" + args.length);
				sender.sendMessage(ChatColor.RED + "[VaultSlots] expected a command.");
				sender.sendMessage(ChatColor.RED + "[VaultSlots] Use /slots help to see Commands and Sign Format.");
				return true;
			}
		}
		return false;
	}
}
