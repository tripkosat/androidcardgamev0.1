package com.example.xeri;

import java.util.ArrayList;

/**
 * H κλάση που αντιπροσωπεύει τα χαρτιά που έχουν απιχτεί και βρίσκονται στην
 * οθόνη-τραπέζι
 * 
 * @author Tripkos Athanasios
 */
public class CardOnTable {
	private ArrayList<Card> c;

	CardOnTable() {
		c = new ArrayList<Card>();
	}

	public void addCard(Card a) {
		c.add(0, a);
	}

	public int sizeOfCardOnTable() {
		return c.size();
	}

	/**
	 * Επιστροφή του χαρτιού που βρίσκεται στην κορυφή, αν δεν υπάρχει τέτοιο
	 * χαρτί επιστρέφει χαρτί με number και symbol ίσον με το "0"
	 * 
	 * @return
	 */
	public Card getTopCard() {
		if (c.isEmpty()) {
			return new Card("0", "0");
		}
		return c.get(0);
	}

	/**
	 * Αφαίρεση όλων των χαρτιών
	 * 
	 * @return ArrayList με χαρτιά
	 */

	public ArrayList<Card> removeAllCards() {
		ArrayList<Card> temp = c;
		c = new ArrayList<Card>();
		return temp;
	}

	/**
	 * Επιστροφή των 4 κορυφαίων χαρτιών αν υπάρχουν
	 * 
	 * @return
	 */
	public ArrayList<Card> get4Card() {
		ArrayList<Card> x = new ArrayList<Card>();
		for (int i = 0; i < 4 && i < c.size(); i++) {
			x.add(i, c.get(i));
		}
		return x;
	}

}
