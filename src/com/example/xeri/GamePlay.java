package com.example.xeri;

import java.util.ArrayList;

/**
 * Η υλοποίηση των κανόνων του παιχνιδιού
 * 
 * @author Tripkos Athanasios
 * 
 */

public class GamePlay {
	// ---Η τράπουλα του παιχνιδιού
	private PackOfCards packOfCards;
	// ---Τα χαρτιά που έχουν παιχτεί
	private CardOnTable cot;
	// ---Ο τελευτείος παίκτης που πήρε χαρτιά. Ώστε να πάρει τα χαρτιά που
	// θα απομείνουν στο τέλος
	private Player lastTaker;
	// ---Ο αριθμός των χαρτιών που θα παίρνει ο παίχτης
	private int NumOfCardsOnHand;

	GamePlay(int x) {

		packOfCards = new PackOfCards();
		cot = new CardOnTable();
		newCardsOnT();
		lastTaker = new ComputerPlayer(0.7);
		NumOfCardsOnHand = x;
	}

	GamePlay(int x, int seed) {

		packOfCards = new PackOfCards(seed);
		cot = new CardOnTable();
		newCardsOnT();
		lastTaker = new ComputerPlayer(0.7);
		NumOfCardsOnHand = x;

	}

	/**
	 * Επιστρέφει των αριθμό των χαρτιών που απομένουν στην τράπουλα
	 * 
	 * @return
	 */
	public int getSizeOfPack() {
		return packOfCards.getSizeOfPack();
	}

	public void moirasmaKartwn(Player players[]) {
		for (int i = 0; i < 2; i++) {
			players[i].removeAllCards();
		}
		for (int i = 0; i < NumOfCardsOnHand; i++) {
			players[0].addCardsOnHand(packOfCards.removeTopCard());
			players[1].addCardsOnHand(packOfCards.removeTopCard());
		}
	}

	/**
	 * Προσθήκη νέων καρτών στο τραπέζι
	 */
	public void newCardsOnT() {
		for (int i = 0; i < 4; i++) {
			cot.addCard(packOfCards.removeTopCard());
		}
	}

	public Card getTopCard() {
		return cot.getTopCard();
	}

	/**
	 * Γίνεται ο έλεγχος όταν παίκτης παίζει ένα χαρτί
	 * 
	 * @param card
	 * @param p
	 */
	public void checkOfCard(Card card, Player p) {
		if (cot.sizeOfCardOnTable() == 0) {// δεν υπάρχει κάρτα στο τραπέζι
			cot.addCard(card);
		} else if (card.equals(cot.getTopCard())) {
			// ίδια κάρτα με την κορυφαία
			if (cot.sizeOfCardOnTable() == 1) {
				// ξερή
				cot.addCard(card);
				p.addKseri(card);
				removeAllCardsofTable(p);
			} else {
				cot.addCard(card);
				removeAllCardsofTable(p);
			}
		} else {

			// περίπτωση βαλέ
			if (card.getNumber().equals("jack")) {
				cot.addCard(card);
				removeAllCardsofTable(p);
			} else {

				// απλή προσθήκη κάρτας
				cot.addCard(card);
			}
		}
	}

	/**
	 * Αφαίρεση των χαρτιών από τα παιγμένα χαρτιά και προσθήκη στα κερδισμένα
	 * χαρτιά του παίκτη ενημέρωση του παίκτη που πήρε τελευταίος τα χαρτιά
	 * 
	 * @param p
	 */
	private void removeAllCardsofTable(Player p) {
		p.addWinCards(cot.removeAllCards());
		lastTaker = p;
	}


	/**
	 * Ο τελευταίος παίκτης που πήρε τα χαρτιά, παίρνει τα χαρτιά που
	 * περισσεύουν στο τέλος του παιχνιδιού
	 * 
	 * @param p
	 */
	public void endOfGame(Player p[]) {
		for (Player a : p) {
			if (a.equals(lastTaker)) {
				removeAllCardsofTable(a);
			}
		}
	}

	/**
	 * Επιστρέφει τα 4 τελευταία χαρτιά που παίχτηκαν
	 * 
	 * @return
	 */
	public ArrayList<Card> get4Card() {
		return cot.get4Card();
	}

	
	
	/**
	 * Επιστρέφει τον αριθμό των καρτών στο τραπέζι
	 * 
	 * @return Ο αριθμός των χαρτιών στο τραπέζι
	 */
	public int numOfCardOnTable() {

		return cot.sizeOfCardOnTable();
	}
}
