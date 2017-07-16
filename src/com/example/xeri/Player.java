package com.example.xeri;

import java.util.ArrayList;

/**
 * Ο παίκτης του παιχνιδιού
 * 
 * @author Tripkos Athanasios
 */

abstract public class Player {

	protected String name;
	private CardsOnHand cardOfPlayer;
	private CardThatWon winCards;

	/**
	 * Δημιουργία-αρχικοποίηση του παίκτη
	 */
	Player() {
		name = "Player1";
		cardOfPlayer = new CardsOnHand();
		winCards = new CardThatWon();
	}

	/**
	 * Δημιουργία-αρχικοποίηση του παίκτη
	 * 
	 * @param s
	 *            Το όνομα του παίκτη
	 */
	Player(String s) {
		name = s;
		cardOfPlayer = new CardsOnHand();
		winCards = new CardThatWon();
	}

	/**
	 * 
	 */
	public void addKseri(Card c) {
		winCards.addKseri(c);
	}

	/**
	 * @return
	 */
	public CardsOnHand getCardsOnHand() {
		return cardOfPlayer;
	}

	/**
	 * 
	 */
	public void removeAllCards() {
		cardOfPlayer.removeall();
	}

	/**
	 * @param a
	 */
	public void addCardsOnHand(Card a) {
		cardOfPlayer.addCard(a);
	}

	/**
	 * Προσθέτει τα χαρτιά που κέρδισε ο παίκτης
	 * 
	 * @param cards
	 */
	public void addWinCards(ArrayList<Card> cards) {
		winCards.addCards(cards);
	}

	public int getNumOfkseres() {
		return winCards.numOfKseres();
	}

	public int getpoints() {
		return winCards.pointsOfCards();
	}

	public String getName() {
		return name;
	}

	public ArrayList<Card> getKseres() {
		return winCards.getKseres();

	}

	/**
	 * Ελέγχει αν οι παίκτες έχουν ίδιο όνομα
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
