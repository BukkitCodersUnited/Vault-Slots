package me.sirtyler.VaultSlots;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;

public class Deck {
	public VaultSlots plugin;
	private String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };
	private String[] cards = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
	Random rand = new Random();
	private Map<String, String> score = new HashMap<String, String>();
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
	public String BlackJack(Player player) {
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
		return ("You got a " + card + " and your score is now " + points);
	}
	public boolean checkString(String str) {
		 try {
			 Integer.parseInt(str);
			 return true;
		 } catch (Exception e) {
			 return false;
		 }	
	}
}
