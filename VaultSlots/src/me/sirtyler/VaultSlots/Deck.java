package me.sirtyler.VaultSlots;

import java.util.Random;

public class Deck {
	public VaultSlots plugin;
	private String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };
	private String[] cards = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
	Random rand = new Random();
	public Deck(VaultSlots instance) {
		plugin = instance;
	}
	public String drawCard() {
		String card = null;
		int rnd1 = rand.nextInt(cards.length) +1;
		int rnd2 = rand.nextInt(suits.length) + 1;
		card = (cards[rnd1] + " of " + suits[rnd2]);
		return card;	
	}
}
