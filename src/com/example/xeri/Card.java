package com.example.xeri;

import java.util.Scanner;

/**
 * H κλάση χαρτί του παιχνιδιού
 * 
 * @author Tripkos Athanasios
 */
public class Card {

	// Το number αντιπροσωπεύει το αριθμό του χαρτίου π.χ. 10,9,jack
	private String number;
	// Το symbol; αντιπροσωπεύει την κλάση π.χ. clubs, diamonds
	private String symbol;

	Card(String number, String symbol) {
		this.number = number;
		this.symbol = symbol;
	}

	Card() {
		this.number = "black";
		this.symbol = "joker";
	}

	/**
	 * Αυτός ο constructor δημιουργεί το χαρτί από την ονομασία του string που
	 * χρησιμοποιείται για την εκτύπωση και την αποστολή των χαρτιών
	 * 
	 * @param c
	 */
	Card(String c) {
		c = c.replace('_', ' ');
		Scanner s = new Scanner(c);

		s.next();
		number = s.next();
		s.next();
		symbol = s.next();
		s.close();
	}

	public String getNumber() {
		return number;
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

	/**
	 * Μετατρέπει το number σε έναν αριθμό θεωρώντας τον άσσο 1 και τον βασιλιά
	 * 13 * @return
	 */
	public int cardNumToNum() {
		int n = 0;

		if (getNumber().equals("ace")) {
			n = 0;
		} else if (getNumber().equals("jack")) {
			n = 10;
		} else if (getNumber().equals("queen")) {
			n = 11;
		} else if (getNumber().equals("king")) {
			n = 12;
		} else {
			n = Integer.parseInt(getNumber()) - 1;
		}
		return n;
	}

	/**
	 * Μετατρέπει το σύμβολο της κάρτας σε ένα int σύμφωνα με μια προκαθορισμένη
	 * σειρά
	 * 
	 * @return
	 */
	public int cardSymToNum() {
		int s = 0;
		if (getSymbol().equals("clubs")) {
			s = 0;
		} else if (getSymbol().equals("diamonds")) {
			s = 1;
		} else if (getSymbol().equals("hearts")) {
			s = 2;
		} else if (getSymbol().equals("spades")) {
			s = 3;
		}
		return s;
	}

	/**
	 * Επιστροφή ενός string με το όνομα του χαρτιού, ώστε να μπορεί να
	 * εκτυπωθεί
	 * 
	 */
	public String toString() {
		return "card_" + number + "_of_" + symbol;

	}

}
