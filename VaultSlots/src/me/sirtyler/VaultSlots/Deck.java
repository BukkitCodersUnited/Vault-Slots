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
	private boolean useDebug = false;
	private static Log logger;
	public Deck(VaultSlots instance) {
		plugin = instance;
	}
	public String drawHand() {
		logger = plugin.log;
		if(useDebug) logger.sendDebugInfo("Drawing Hand");
		String hand = null;
		String card1 = drawCard();
		String card2 = drawCard();
		String card3 = drawCard();
		String card4 = drawCard();
		String card5 = drawCard();
		hand = (card1 + ", " + card2 + ", " + card3 + ", " + card4 + ", " + card5);
		if(useDebug) logger.sendDebugInfo("Drew Hand:" + hand);
		return hand;
	}
	public String drawCard() {
		logger = plugin.log;
		if(useDebug) logger.sendDebugInfo("Drawing Card");
		String card = null;
		int rnd1 = rand.nextInt(cards.length);
		int rnd2 = rand.nextInt(suits.length);
		card = (cards[rnd1] + " of " + suits[rnd2]);
		if(useDebug) logger.sendDebugInfo("Drew Card:" + card);
		return card;	
	}
	public void BlackJack(Player p, String play) {
		logger = plugin.log;
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
			logger.sendExceptionInfo(e);
			points = 0;
		}
		if(play.equalsIgnoreCase("hit")) {
			if(useDebug) logger.sendDebugInfo("Hit: " + player.getName());
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
				logger.sendExceptionInfo(e);
			}
			player.sendMessage(ChatColor.GREEN + "You got a " + card + " and your score is now " + points);
		} else if(play.equalsIgnoreCase("stay")) {
			if(useDebug) logger.sendDebugInfo("Stay: " + player.getName());
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
		logger = plugin.log;
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
		logger = plugin.log;
		cmdEx.blackjack.remove(p.getName());
		bets.remove(p.getName());
		score.remove(p.getName());
	}
	public void setGame(Player p, int bet) {
		logger = plugin.log;
		eco = plugin.economy;
		cmdEx = plugin.myExecutor;
		bets.put(p.getName(), bet);
		bet(p);
	}
	public void bet(Player p) {
		logger = plugin.log;
		int bet = bets.get(p.getName());
		if(eco.getBalance(p.getName()) >= bet) {
			eco.withdrawPlayer(p.getName(), bet);
			p.sendMessage(ChatColor.GREEN + "You place a bet of $" + bet);
		}
	}
	public void win(Player p) {
		logger = plugin.log;
		int bet = (bets.get(p.getName()) * 2);
		p.sendMessage(ChatColor.GOLD + "You win $" + bet);
		eco.depositPlayer(p.getName(), bet);
		
	}
	public void winDouble(Player p) {
		logger = plugin.log;
		int bet = (bets.get(p.getName()) * 4);
		p.sendMessage(ChatColor.GOLD + "You win $" + bet);
		eco.depositPlayer(p.getName(), bet);
	}
	public void Poker(Player p) {
		logger = plugin.log;
		Player player = p;
		String hand = drawHand();
		player.sendMessage(ChatColor.GREEN + "You got a Hand of:");
		player.sendMessage(ChatColor.GOLD + hand);
		player.sendMessage(ChatColor.BLUE + checkHand(hand));
	}
	public String checkHand(String hand) {
		String handnew = hand.replace(" ", "");
		String[] cards = handnew.split(",");
		String[] rank = new String[cards.length];
		String[] suit = new String[cards.length];
		for(int i = 1; i <= cards.length; i++) {
			rank[i-1] = cards[i-1].split("of")[0];
			suit[i-1] = cards[i-1].split("of")[1];
		}
		String rank1 = rank[0];
		String rank2 = rank[1];
		String rank3 = rank[2];
		String rank4 = rank[3];
		String rank5 = rank[4];
		String suit1 = suit[0];
		String suit2 = suit[1];
		String suit3 = suit[2];
		String suit4 = suit[3];
		String suit5 = suit[4];
		if(suit1.equalsIgnoreCase(suit2) && suit2.equalsIgnoreCase(suit3) && suit3.equalsIgnoreCase(suit4) && suit4.equalsIgnoreCase(suit5)) {
			if(checkConsecutive(rank1,rank2,rank3,rank4,rank5)) {
				if((rank1.equalsIgnoreCase("Ace") && rank2.equalsIgnoreCase("King") && rank3.equalsIgnoreCase("Queen") && rank4.equalsIgnoreCase("Jack") && rank5.equalsIgnoreCase("10")) || (rank1.equalsIgnoreCase("10") && rank2.equalsIgnoreCase("Jack") && rank3.equalsIgnoreCase("Queen") && rank4.equalsIgnoreCase("King") && rank5.equalsIgnoreCase("Ace"))) {
					return "Royal Flush";
				} else {
					return "Straight Flush";
				}
			}
			return "Flush";
		} else if(suit1.equalsIgnoreCase(suit2) && suit2.equalsIgnoreCase(suit3) && suit3.equalsIgnoreCase(suit4)) {
			return "Four of a Kind";
		} else if(suit1.equalsIgnoreCase(suit2) && suit2.equalsIgnoreCase(suit3) && suit4.equalsIgnoreCase(suit5)) {
			return "Full House";
		} else if(checkConsecutive(rank1,rank2,rank3,rank4,rank5)) {
			return "Straight";
		} else if(rank1.equalsIgnoreCase(rank2) && rank2.equalsIgnoreCase(rank3) && rank3.equalsIgnoreCase(rank4)|| rank2.equalsIgnoreCase(rank3) && rank3.equalsIgnoreCase(rank4) && rank4.equalsIgnoreCase(rank5)) {
			return "Three of a Kind";
		} else if(rank1.equalsIgnoreCase(rank2) && rank3.equalsIgnoreCase(rank4) || rank2.equalsIgnoreCase(rank3) && rank3.equalsIgnoreCase(rank4) || rank3.equalsIgnoreCase(rank4) && rank4.equalsIgnoreCase(rank5)) {
			return "Two Pair";
		}else if(rank1.equalsIgnoreCase(rank2) || rank2.equalsIgnoreCase(rank3) || rank3.equalsIgnoreCase(rank4) || rank4.equalsIgnoreCase(rank5)) {
			return "One Pair";
		}
		return "High Card";
	}
	public int getCardInt(String card) {
		if(card.equalsIgnoreCase("Ace")) {
			return 0;
		}
		if(card.equalsIgnoreCase("2")) {
			return 1;
		}
		if(card.equalsIgnoreCase("3")) {
			return 2;
		}
		if(card.equalsIgnoreCase("4")) {
			return 3;
		}
		if(card.equalsIgnoreCase("5")) {
			return 4;
		}
		if(card.equalsIgnoreCase("6")) {
			return 5;
		}
		if(card.equalsIgnoreCase("7")) {
			return 6;
		}
		if(card.equalsIgnoreCase("8")) {
			return 7;
		}
		if(card.equalsIgnoreCase("9")) {
			return 8;
		}
		if(card.equalsIgnoreCase("10")) {
			return 9;
		}
		if(card.equalsIgnoreCase("Jack")) {
			return 10;
		}
		if(card.equalsIgnoreCase("Queen")) {
			return 11;
		}
		if(card.equalsIgnoreCase("King")) {
			return 12;
		}
		return -1;
	}
	public boolean checkConsecutive(String card1, String card2, String card3, String card4, String card5) {
		int i1 = getCardInt(card1);
		int i2 = getCardInt(card2);
		int i3 = getCardInt(card3);
		int i4 = getCardInt(card4);
		int i5 = getCardInt(card5);
		if(((i1 + 1)==i2) && ((i2 + 1)==i3) && ((i3 + 1)==i4) && ((i3 + 1)==i4) && ((i4 + 1)==i5)) {
			return true;
		}
		if(((i1 - 1)==i2) && ((i2 - 1)==i3) && ((i3 - 1)==i4) && ((i3 - 1)==i4) && ((i4 - 1)==i5)) {
			return true;
		}
		return false;
	}
}
