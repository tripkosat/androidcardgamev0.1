package com.example.xeri;

import java.util.ArrayList;

/**
 * Τα χαρτιά που κρατάει ο παίκτης στα χέρια του
 * 
 * @author Tripkos Athanasios
 */
public class CardsOnHand {
	private ArrayList<Card> c;

	public CardsOnHand() {
		c = new ArrayList<Card>();
	}

	public void removeall() {
		c = new ArrayList<Card>();
	}

	public void addCard(Card a) {
		c.add(a);
	}

	public Card removeCard(int index) {
		return c.remove(index);
	}

	/**
	 * Ο αριθμός που υπάρχει το number ενός χαρτιού στα χαρτιά
	 * 
	 * @param s
	 * @return
	 */
	public int timesOfACard(Card s) {
		int size = 0;
		for (Card a : c) {
			if (a.equals(s)) {
				size++;
			}
		}
		return size;
	}

	public int size() {
		return c.size();
	}

	public Card getCard(int index) {
		return c.get(index);
	}

	/**
	 * Ελέγχει αν υπάρχει ίδιο χαρτί(ίδιο νούμερο) στα χαρτιά του παίκτη
	 * 
	 * @param a
	 * @return Αν δεν υπάρχει επιστρέφει -1 αλλιώς 0 και θετικούς
	 */
	public int existACard(Card a) {
		return c.indexOf(a);
	}

}
