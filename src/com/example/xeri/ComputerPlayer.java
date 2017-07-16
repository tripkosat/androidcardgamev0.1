package com.example.xeri;

import java.util.Random;

/**
 * Ο παίκτης-υπολογιστής είναι ο αντίπαλος του παίκτη και στόσος του είναι να
 * παίζει όσο πιο αποδοτικά πορεί σε συσχειση με την δυσκολία που επιλέγουμε
 * 
 * @author Tripkos Athanasios
 * 
 */
public class ComputerPlayer extends Player {

	private int[] ar;// παιγμένες κάρτες
	private int nop;// αριθμός παιγμένων καρτών
	private double prob;// δυσκολία

	public ComputerPlayer(double d) {
		super();
		name = "Υπολογιστής";
		nop = 0;
		ar = new int[52];
		for (int i = 0; i < 52; i++) {
			ar[i] = 0;
		}
		prob = d;
	}

	/**
	 * Προσθέτει μια κάρτα στον πίνακα των παιγμένων καρτών
	 * 
	 * @param a
	 */
	public void addCardToList(Card a) {
		nop++;
		ar[a.cardNumToNum()]++;
	}

	/**
	 * Επιλέγει την πιο συμφέρουσα κάρτα για να παίξει
	 * 
	 * @param onTop
	 * @param sizeOfDeck
	 * @param numOfCards
	 * @return
	 */
	public Card selectTheCardToPlay(Card onTop, int sizeOfDeck, int numOfCards) {
		Card c = new Card();
		CardsOnHand coh = getCardsOnHand();

		// περίπτωση να μας κάνει ξερή
		// Δεν υπάρχει χαρτί στο τραπέζι
		if (onTop.getNumber().equals("0")) {

			c = coh.removeCard(maxCard());

			// περίπτωση που έχουμε ίδιο χαρτί με την top
		} else if (coh.existACard(onTop) >= 0) {

			c = coh.removeCard(coh.existACard(onTop));

		} else {

			// περίπτωση βαλέ
			if (coh.existACard(new Card("jack", "jack")) >= 0) {

				if (sameNumber(new Card("jack", "jack")) > 2 && sizeOfDeck < 4) {
					c = coh.removeCard(maxCard());
				} else {
					c = coh.removeCard(coh.existACard(new Card("jack", "jack")));
				}

				// περίπτωση που δεν έχουμε συμβατό χαρτί
			} else {
				// ---στους πρώτους γύρους κρατάει το χαρτί που θεωρεί ότι έχει
				// παιχτεί
				// περισσότερες φορές ώστε να μπορεί να αποφύγει την πιθανότητα
				// μιας ξερής---
				// ---έτσι ρίχνει την δεύτερη πιο παιγμένη κάρτα
				if (nop > 30) {
					c = coh.removeCard(maxCard());
				} else {
					c = coh.removeCard(secondMax());
				}
			}
		}
		return c;
	}

	/**
	 * Επιστρέφει τον αριθμό των παιγμένων καρτών
	 * 
	 * @return
	 */
	public int numOfCards() {
		return nop;
	}

	/**
	 * Βρίσκει πόσες φορές έχει παιχτεί κάρτα με τον ίδιο αριθμό
	 * 
	 * @param a
	 * @return
	 */
	public int sameNumber(Card a) {

		Random random = new Random();
		double r = random.nextDouble();
		int realN = ar[a.cardNumToNum()];
		switch (realN) {
		case 0:
			if (r <= prob) {
				return 0;
			} else if (r <= prob + 5 * (1 - prob) / 10) {
				return 1;
			} else if (r <= prob + 9 * (1 - prob) / 10) {
				return 2;
			} else {
				return 3;
			}
		case 1:
			if (r <= prob) {
				return 1;
			} else if (r <= prob + 4 * (1 - prob) / 9) {
				return 2;
			} else if (r <= prob + 8 * (1 - prob) / 9) {
				return 0;
			} else {
				return 3;
			}

		case 2:
			if (r <= prob) {
				return 2;
			} else if (r <= prob + 4 * (1 - prob) / 9) {
				return 3;
			} else if (r <= prob + 8 * (1 - prob) / 9) {
				return 1;
			} else {
				return 0;
			}
		case 3:
			if (r <= prob) {
				return 3;
			} else if (r <= prob + 5 * (1 - prob) / 10) {
				return 2;
			} else if (r <= prob + 9 * (1 - prob) / 10) {
				return 1;
			} else {
				return 0;
			}
		}
		return realN;

	}

	/**
	 * βρίσκει την θέση της κάρτας που έχει χρησιμοποιθεί περισσότερες φορές ή
	 * την έχουμε στο χέρι μας
	 * 
	 * @return
	 */
	public int maxCard() {
		Random r = new Random();
		int thesi = 0;
		int max = -1;
		int k = 0;
		CardsOnHand coh = getCardsOnHand();

		for (int i = 0; i < coh.size(); i++) {
			k = sameNumber(coh.getCard(i));
			k += coh.timesOfACard(coh.getCard(i));

			if (k > max) {
				// Αποφεύγει την χρήση βαλέ.
				if (coh.getCard(i).getNumber().equals("jack")) {
					// Ο Βαλές παίζεται μόνο αν είναι τέταρτος
					// και αν δεν υπάρχει ισότιμη κάρτα
					if (k == 4) {
						max = k;
						thesi = i;
					}
				} else {
					max = k;
					thesi = i;
				}
			} else if (k == max && !coh.getCard(i).getNumber().equals("jack")) {

				if (coh.getCard(thesi).getNumber().equals("jack")) {
					thesi = i;
				} else if (r.nextDouble() > 0.5) {
					thesi = i;
				}
			}

		}
		return thesi;
	}

	/**
	 * Επιστρέφει την θέση της κάρτας που έχει χρησιμοποιηθεί δεύτερη σε
	 * κατάταξη περισσότερες φορές (μπορεί να έχει χρησιμοποιηθεί και ίσες)
	 * 
	 * @return
	 */
	public int secondMax() {
		Random r = new Random();
		int thesi1 = 0;
		int thesi2 = 0;
		int max1 = -1;
		int max2 = -1;
		int k = 0;
		CardsOnHand coh = getCardsOnHand();

		for (int i = 0; i < coh.size(); i++) {

			if (coh.getCard(i).getNumber().equals("jack")) {
				// Αν το χαρτί είναι βαλές, δεν το υπολογίζει
			} else {

				k = sameNumber(coh.getCard(i));
				k += coh.timesOfACard(coh.getCard(i));
				if (k > max2) {

					thesi2 = i;
					max2 = i;

					if (k > max1) {
						thesi2 = thesi1;
						thesi1 = i;
						max2 = max1;
						max1 = k;
					} else if (k == max1 && r.nextDouble() > 0.5) {
						max2 = max1;
						thesi2 = thesi1;
						thesi1 = i;

					} else if (k == max2 && r.nextDouble() > 0.5) {
						thesi2 = i;
					}
				}
			}

		}
		return thesi2;

	}
}
