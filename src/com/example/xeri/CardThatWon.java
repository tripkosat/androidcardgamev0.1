package com.example.xeri;

import java.util.ArrayList;

/**
 * Τα χαρτιά που έχει κερδίσει ο παίκτης καθώς και οι ξερές που έχει κάνει
 * 
 * @author Tripkos Athanasios
 */
public class CardThatWon {
	private ArrayList<Card> cardThatWon;
	private ArrayList<Card> kseres;

	public CardThatWon() {
		cardThatWon = new ArrayList<Card>();
		kseres = new ArrayList<Card>();
	}

	public void addCards(ArrayList<Card> c) {
		cardThatWon.addAll(c);
	}

	public void addKseri(Card c) {
		kseres.add(c);

	}

	/**
	 * Υπολογισμός των συνολικών πόντων που έχει κερδίσει ο παίκτης
	 * 
	 * @return
	 */
	public int pointsOfCards() {

		int points = 0;
		for (Card c : cardThatWon) {
			if (c.getNumber().equals("jack") || c.getNumber().equals("ace")
					|| c.getNumber().equals("queen")
					|| c.getNumber().equals("king")) {
				points++;
			} else if (c.getNumber().equals("10")) {

				if (c.getSymbol().equals("diamonds")) {
					points += 2;
				} else {
					points++;
				}
			} else if (c.getNumber().equals("2")
					&& c.getSymbol().equals("clubs")) {
				points++;
			}
		}

		if (cardThatWon.size() > 26) {
			points += 3;
		}
		return pointsofkseres() + points;
	}

	public ArrayList<Card> getKseres() {
		return kseres;
	}

	/**
	 * Υπολογισμός των κερδισμένων πόντων από ξερή
	 * 
	 * @return
	 */
	public int pointsofkseres() {
		int points = 0;
		for (Card c : kseres) {
			if (c.getNumber().equals("jack")) {
				points += 20;
			} else {
				points += 10;
			}
		}
		return points;
	}

	public int numOfKseres() {
		return kseres.size();

	}
}
