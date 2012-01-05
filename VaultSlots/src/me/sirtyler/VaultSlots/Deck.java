package me.sirtyler.VaultSlots;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Deck {
	public VaultSlots plugin;
    public Economy eco = null;
    private CommandEx cmdEx;
	private String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };
	private String[] cards = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
	Random rand = new Random();
	private Map<String, String> score = new HashMap<String, String>();
	private Map<String, Integer> bets = new HashMap<String, Integer>();
	public Deck(VaultSlots instance) {
		plugin = instance;
	}
	public String drawCard() {
		String card = null;
		int rnd1 = rand.nextInt(cards.length);
		int rnd2 = rand.nextInt(suits.length);
		card = (cards[rnd1] + " of " + suits[rnd2]);
		return card;	
	}
	public void BlackJack(Player p, String play) {
		Player player = p;
		String card = drawCard();
		String card2 = card.split(" of ")[0];
		int points;
		try {
			if(score.containsKey(player.getName())) {
				points = Integer.parseInt(score.get(player.getName()));
			} else {
				points = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			points = 0;
		}
		if(play.equalsIgnoreCase("hit")) {
			if(card2.equals("Ace")) {
				if((points + 11) <= 21) {
					points += 11;
				} else {
					points += 1;
				}
			} else if(checkString(card2)) {
				points += Integer.parseInt(card2);
			} else {
				points += 10;
			}
			try {
				score.put(player.getName(), ("" +points));
			} catch (Exception e) {
				e.printStackTrace();
			}
			player.sendMessage(ChatColor.GREEN + "You got a " + card + " and your score is now " + points);
		} else if(play.equalsIgnoreCase("stay")) {
			player.sendMessage(ChatColor.GREEN +"You stay with a score of " + points);
			int i = BlackJackAuto();
			player.sendMessage(ChatColor.GRAY + "Dealer gets a score of " + i);
			if(i > 21) {
				player.sendMessage(ChatColor.GREEN +"Dealer busted with " + i);
				win(player);
				clearScore(player);
			} else if(i == 21) {
				player.sendMessage(ChatColor.GRAY + "Dealer gets Blackjack.");
				clearScore(player);
			} else if(i > points) {
				player.sendMessage(ChatColor.GRAY + "Dealer beats your " + points + " with a " + i);
				clearScore(player);
			} else if(i < points) {
				player.sendMessage(ChatColor.GREEN + "You beat the Dealer's " + i + " with your " + points);
				win(player);
				clearScore(player);
			} else if(i == points) {
				player.sendMessage(ChatColor.GRAY + "You tie.");
				clearScore(player);
			}
		}
		if(points > 21) {
			clearScore(player);
			player.sendMessage(ChatColor.GRAY + "Aw! you busted.");
		} else if(points == 21) {
			player.sendMessage(ChatColor.GOLD + "Blackjack! Congrats!");
			winDouble(player);
			clearScore(player);
		}
		return;
	}
	public int BlackJackAuto() {
		String card = drawCard();
		String card2 = card.split(" of ")[0];
		int points = 0;
		while(points <= 18) {
			if(card2.equals("Ace")) {
				if((points + 11) <= 21) {
					points += 11;
				} else {
					points += 1;
				}
			} else if(checkString(card2)) {
				points += Integer.parseInt(card2);
			} else {
				points += 10;
			}
		}
		return points;
	}
	public boolean checkString(String str) {
		 try {
			 Integer.parseInt(str);
			 return true;
		 } catch (Exception e) {
			 return false;
		 }	
	}
	public void clearScore(Player p) {
		cmdEx.blackjack.remove(p.getName());
		bets.remove(p.getName());
		score.remove(p.getName());
	}
	public void setGame(Player p, int bet) {
		eco = plugin.economy;
		cmdEx = plugin.myExecutor;
		bets.put(p.getName(), bet);
		bet(p);
	}
	public void bet(Player p) {
		int bet = bets.get(p.getName());
		if(eco.getBalance(p.getName()) >= bet) {
			eco.withdrawPlayer(p.getName(), bet);
			p.sendMessage(ChatColor.GREEN + "You place a bet of $" + bet);
		}
	}
	public void win(Player p) {
		int bet = (bets.get(p.getName()) * 2);
		p.sendMessage(ChatColor.GOLD + "You win $" + bet);
		eco.depositPlayer(p.getName(), bet);
		
	}
	public void winDouble(Player p) {
		int bet = (bets.get(p.getName()) * 4);
		p.sendMessage(ChatColor.GOLD + "You win $" + bet);
		eco.depositPlayer(p.getName(), bet);
	}
}
