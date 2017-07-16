package com.example.xeri;

import java.util.ArrayList;
import java.util.Random;

/**
 * Η τράπουλα του παιχνιδιού
 * 
 * @author Tripkos Athanasios
 * 
 */
public class PackOfCards {

	private ArrayList<Card> packOfCards;

	PackOfCards() {
		packOfCards = new ArrayList<Card>();

		ArrayList<String> number = new ArrayList<String>();
		ArrayList<String> symbol = new ArrayList<String>();

		creator(number, symbol);

		Random r = new Random();

		for (int i = 0; i < symbol.size(); i++) {
			for (int j = 0; j < number.size(); j++) {
				Card tempCard = new Card(number.get(j), symbol.get(i));
				packOfCards.add(r.nextInt(packOfCards.size() + 1), tempCard);
			}
		}

	}

	/**
	 * Χρήση seed για την δημιουργία τυχαίας τράπουλας αλλά κοινής και στους δύο
	 * παίκτες ώστε να μην χρειάζεται η ανταλλαγή μιας ολόκληρης τράπουλας
	 * 
	 * @param seed
	 */

	PackOfCards(int seed) {
		packOfCards = new ArrayList<Card>();

		ArrayList<String> number = new ArrayList<String>();
		ArrayList<String> symbol = new ArrayList<String>();

		creator(number, symbol);

		Random r = new Random(seed);
		// τυχαία τοποθέτηση των χαρτιών στην τράπουλα
		for (int i = 0; i < symbol.size(); i++) {
			for (int j = 0; j < number.size(); j++) {
				Card tempCard = new Card(number.get(j), symbol.get(i));
				packOfCards.add(r.nextInt(packOfCards.size() + 1), tempCard);
			}
		}
	}

	/**
	 * Όλοι οι αριθμοί και τα σύμβολα που υπάρχουν Οι συνδυασμού τους
	 * δημιουργούν όλα τα χαρτιά της τράπουλας
	 * 
	 * @param number
	 * @param symbol
	 */

	public void creator(ArrayList<String> number, ArrayList<String> symbol) {
		number.add("ace");
		number.add("2");
		number.add("3");
		number.add("4");
		number.add("5");
		number.add("6");
		number.add("7");
		number.add("8");
		number.add("9");
		number.add("10");
		number.add("jack");
		number.add("queen");
		number.add("king");
		symbol.add("clubs");
		symbol.add("diamonds");
		symbol.add("hearts");
		symbol.add("spades");

	}

	public Card removeTopCard() {
		return packOfCards.remove(0);
	}

	public int getSizeOfPack() {
		return packOfCards.size();
	}

}
