package com.example.xeri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Η κλάση που υλοποιεί το παιχνίδι της ξερής με αντίπαλο τον υπολογιστή
 * 
 * @author Tripkos Athanasios
 */
public class Game extends Activity {
	// Τα χαρτιά-κουμπιά των παικτών
	private ImageButton[] ib;
	private ImageButton[] ib2;
	// Το τελευταίο χαρτί που έχει παιχτεί
	private ImageButton playedCards;
	// Η τράπουλα
	private ImageButton remPackOfCards;
	// Τα χαρτιά που έχουν γίνει ξερή
	private ImageButton[] ksR;
	private ImageButton[] ksL;
	// Ο αριθμός των χαρτιών που θα κρατάει ο κάθε παίκτης
	private int numOfCardsOnHand;
	// Μετρητής που χρησιμέυει για να αλλάζουμε γύρο
	private int turn;
	// Η δυσκολία του παιχνιδιού
	private double diskolia;

	// Ποιος παίζει πρώτος και ποιος δεύτερος
	private int numOfhuman, numOfcomp;
	private Button iplayfisrt, iplaysecond;
	private TextView textwho;
	private GamePlay gm;

	// Οι δύο παίκτες
	private Player[] players;
	// Πίνακας με τα χαρτιά που κρατάει ο παίκτης-χρήστης σε αντιστοιχία με το
	// ib[] πίνακα

	private Card[] ic;
	private boolean firstTurn;

	private LinearLayout playerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamexml);
		numOfCardsOnHand = 6;
		diskolia = 0.7;

		boolean existsFile = true;
		/*
		 * Αν υπάρχει αρχείο με ρυθμίσεις, διαβάζουμε τα δεδομένα του και
		 * αρχικοποιούμε το παιχνίδι αλλιώς χρησιμοποιούμε τις προκαθορισμένες
		 * ρυθμίσεις
		 */
		String FILENAME = "option";
		byte[] bytes = new byte[1024];

		FileInputStream fos = null;
		try {
			fos = openFileInput(FILENAME);

		} catch (FileNotFoundException e) {
			existsFile = false;
		}
		if (existsFile) {
			try {
				fos.read(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String str = new String(bytes);
			Scanner s = new Scanner(str);
			s.next();
			String str1 = s.next();
			String str2 = s.next();
			numOfCardsOnHand = Integer.parseInt(str1);
			diskolia = Double.parseDouble(str2);

			s.close();
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		firstTurn = true;

		turn = 0;

		ib = new ImageButton[numOfCardsOnHand];
		ic = new Card[numOfCardsOnHand];
		ib2 = new ImageButton[numOfCardsOnHand];
		// Αρχικοποίηση των ImageButton των χαρτιών
		ib[0] = (ImageButton) findViewById(R.id.im1);
		ib[1] = (ImageButton) findViewById(R.id.im2);
		ib[2] = (ImageButton) findViewById(R.id.im3);
		ib[3] = (ImageButton) findViewById(R.id.im4);

		ib2[0] = (ImageButton) findViewById(R.id.im5);
		ib2[1] = (ImageButton) findViewById(R.id.im6);
		ib2[2] = (ImageButton) findViewById(R.id.im7);
		ib2[3] = (ImageButton) findViewById(R.id.im8);

		// Αρχικοποίηση των ImageButton που χρησιμεύουν για την προβολή των
		// χαρτιών που έχουν γίνει ξερή
		ksR = new ImageButton[6];
		ksL = new ImageButton[6];

		ksR[0] = (ImageButton) findViewById(R.id.ksR0);
		ksR[1] = (ImageButton) findViewById(R.id.ksR1);
		ksR[2] = (ImageButton) findViewById(R.id.ksR2);
		ksR[3] = (ImageButton) findViewById(R.id.ksR3);
		ksR[4] = (ImageButton) findViewById(R.id.ksR4);
		ksR[5] = (ImageButton) findViewById(R.id.ksR5);

		ksL[0] = (ImageButton) findViewById(R.id.ksL0);
		ksL[1] = (ImageButton) findViewById(R.id.ksL1);
		ksL[2] = (ImageButton) findViewById(R.id.ksL2);
		ksL[3] = (ImageButton) findViewById(R.id.ksL3);
		ksL[4] = (ImageButton) findViewById(R.id.ksL4);
		ksL[5] = (ImageButton) findViewById(R.id.ksL5);

		remPackOfCards = (ImageButton) findViewById(R.id.im12);
		playedCards = (ImageButton) findViewById(R.id.im0);

		if (numOfCardsOnHand == 6) {
			ib2[4] = (ImageButton) findViewById(R.id.im65);
			ib2[5] = (ImageButton) findViewById(R.id.im66);
			ib[4] = (ImageButton) findViewById(R.id.im61);
			ib[5] = (ImageButton) findViewById(R.id.im62);

		} else {
			/*
			 * Αν το παιχνίδι είναι τεσσάρων χαρτιών, αυτά τα χαρτιά δεν
			 * χρειάζονται και γίνονται αόρατα
			 */
			ImageButton tempBut = (ImageButton) findViewById(R.id.im65);
			tempBut.setVisibility(View.INVISIBLE);
			tempBut = (ImageButton) findViewById(R.id.im66);
			tempBut.setVisibility(View.INVISIBLE);
			tempBut = (ImageButton) findViewById(R.id.im61);
			tempBut.setVisibility(View.INVISIBLE);
			tempBut = (ImageButton) findViewById(R.id.im62);
			tempBut.setVisibility(View.INVISIBLE);
			/*
			 * εδώ αλλάζει η διάταξή τους ώστε να μην καλύπτει το ένα το άλλο
			 * αφού υπάρχει περισσότερος χώρος
			 */
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ib2[0]
					.getLayoutParams();
			layoutParams.leftMargin = 3;
			for (int i = 0; i < 4; i++) {
				ib[i].setLayoutParams(layoutParams);
				ib2[i].setLayoutParams(layoutParams);
			}
		}
		playerOrder();
	}

	public void init() {
		gm = new GamePlay(numOfCardsOnHand);

		players = new Player[2];

		players[numOfhuman] = new HumanPlayer("Εσύ");
		players[numOfcomp] = new ComputerPlayer(diskolia);

		for (Card temp : gm.get4Card()) {
			((ComputerPlayer) players[numOfcomp]).addCardToList(temp);
		}

		invAll();
		onClickButton();

	}

	public void printACard(ImageButton imb, Card c) {
		String com = new String("com.example.xeri");
		if (c.getNumber().equals("0")) {
			imb.setVisibility(View.INVISIBLE);
		} else {
			imb.setVisibility(View.VISIBLE);
			imb.setImageResource(getResources().getIdentifier(c.toString(),
					"drawable", com));
		}
	}

	/**
	 * παρουσίαση των αποτελεσματων αν έχει τελειώσει το παιχνίδι ή νέος γύρος
	 * αν δεν έχουν οι παίκτες χαρτιά στα χέρια τους
	 */
	private void result() {
		if (turn == numOfCardsOnHand * 2 && gm.getSizeOfPack() == 0) {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					gm.endOfGame(players);
					invAll();
					String s1 = players[0].getName() + ": "
							+ players[0].getpoints() + " πόντους" + "\n"
							+ "Ξερές: " + players[0].getNumOfkseres();
					String s2 = players[1].getName() + ": "
							+ players[1].getpoints() + " πόντους" + "\n"
							+ "Ξερές: " + players[1].getNumOfkseres();

					TextView text1 = (TextView) findViewById(R.id.player1status);
					TextView text2 = (TextView) findViewById(R.id.player2status);
					text1.setVisibility(View.VISIBLE);
					text2.setVisibility(View.VISIBLE);
					text1.setText(s1);
					text2.setText(s2);
				}
			}, 800);
		} else if (turn == numOfCardsOnHand * 2) {
			remPackOfCards.setEnabled(true);
			remPackOfCards
					.setColorFilter(Color.argb(0, 0, 0, 0), Mode.SRC_ATOP);

		}
	}

	/**
	 * Η συνατηση που υλοποιεί την απαραίτητη επανάληψη που χρειάζεται το
	 * παιχνλιδι της ξερής
	 * 
	 * @param cardn
	 *            Ο αριθμός του χαρτιού στον πίνακα ic[] που παίχτηκε
	 * @throws Throwable
	 */

	private void playTheCard(int cardn) throws Throwable {

		Card c = ic[cardn];
		firstTurn = false;
		((ComputerPlayer) players[numOfcomp]).addCardToList(c);
		printACard(playedCards, c);// εφμάνιση της κάρτας ως παιγμένη
		gm.checkOfCard(c, players[numOfhuman]);
		printACard(playedCards, gm.getTopCard());
		infKseri();
		disAll();
		turn++;

		if (turn != numOfCardsOnHand * 2) {
			compPlaysACard();
		} else {
			infKseri();
			ennAll();
		}
		result();

	}

	public void compPlaysACard() {
		/*
		 * Η χρησιμότητα των Handlers είναι να καθυστερούν την εμφάνιση του
		 * χαρτιού ώστε ο παίκτης να βλέπει τι χαρτί έριξε ο αντίπαλος
		 */

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				// Η σειρά του υπολογιστή να παίξει
				Card c2 = ((ComputerPlayer) players[numOfcomp])
						.selectTheCardToPlay(gm.getTopCard(),
								gm.numOfCardOnTable(), numOfCardsOnHand);

				((ComputerPlayer) players[numOfcomp]).addCardToList(c2);
				printACard(playedCards, c2);
				gm.checkOfCard(c2, players[numOfcomp]);

				ib2[(int) turn / 2].setVisibility(View.INVISIBLE);

				final Handler handler1 = new Handler();
				handler1.postDelayed(new Runnable() {
					@Override
					public void run() {
						turn++;
						printACard(playedCards, gm.getTopCard());
						infKseri();
						ennAll();
						result();

					}
				}, 400);
			}
		}, 400);
	}

	/**
	 * Καθορισμός συμπεριφοράς των btuoon-χαρτιών
	 * 
	 */
	private void onClickButton() {
		ib[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

				try {
					playTheCard(0);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		ib[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

				try {
					playTheCard(1);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		ib[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

				try {
					playTheCard(2);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		ib[3].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

				try {
					playTheCard(3);
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
		});

		if (numOfCardsOnHand == 6) {
			ib[4].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setVisibility(View.INVISIBLE);

					try {
						playTheCard(4);
					} catch (Throwable e) {
						e.printStackTrace();
					}

				}
			});

			ib[5].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setVisibility(View.INVISIBLE);

					try {
						playTheCard(5);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});
		}

		remPackOfCards.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				turn = 0;
				gm.moirasmaKartwn(players);
				for (int i = 0; i < numOfCardsOnHand; i++) {
					ic[i] = players[numOfhuman].getCardsOnHand().getCard(i);
				}

				playedCards.setVisibility(View.VISIBLE);
				printACard(playedCards, gm.getTopCard());

				for (int l = 0; l < numOfCardsOnHand; l++) {
					ib2[l].setVisibility(View.VISIBLE);
					ib[l].setVisibility(View.VISIBLE);
					printACard(ib[l], players[numOfhuman].getCardsOnHand()
							.getCard(l));
				}
				if (gm.getSizeOfPack() == 0) {
					remPackOfCards.setVisibility(View.INVISIBLE);
				}
				remPackOfCards.setEnabled(false);
				// σκουρένει το συγκεκριμένο ImageButton ώστε να φαίνεται
				// απενεργοποιημένο
				remPackOfCards.setColorFilter(Color.argb(150, 0, 0, 0),
						Mode.SRC_ATOP);

				if (firstTurn == true && numOfhuman == 1) {

					disAll();
					ArrayList<Card> k = gm.get4Card();

					playerLayout = (LinearLayout) findViewById(R.id.tempL);
					ImageButton[] tempCC = new ImageButton[4];
					tempCC[0] = (ImageButton) findViewById(R.id.tempCard1);
					tempCC[1] = (ImageButton) findViewById(R.id.tempCard2);
					tempCC[2] = (ImageButton) findViewById(R.id.tempCard3);
					tempCC[3] = (ImageButton) findViewById(R.id.tempCard4);

					playerLayout.setVisibility(View.VISIBLE);

					for (int i = 0; i < k.size(); i++) {
						tempCC[i].setVisibility(View.VISIBLE);
						printACard(tempCC[i], k.get(i));
					}
					for (int i = k.size(); i < 4; i++) {
						tempCC[i].setVisibility(View.INVISIBLE);
					}
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							playerLayout.setVisibility(View.INVISIBLE);
							ennAll();
							firstTurn = false;
							disAll();
							compPlaysACard();

						}
					}, 3000);
				} else if (firstTurn == true && numOfhuman == 0) {
					Toast.makeText(
							getApplicationContext(),
							"Πάτα τα κεντρικά φύλλα ώστε να δεις τα 4 προηγούμενα φύλλα",
							Toast.LENGTH_LONG).show();
				} else if (numOfcomp == 0) {
					disAll();
					compPlaysACard();

				}

			}
		});

		playedCards.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (firstTurn == true) {
					/*
					 * μόνο στο πρώτο μοίαρασμα ο παίκτη μπορεί να δει τα χαρτιά
					 * που υπάρχουν στο τραπέζι-οθόνη
					 */
					disAll();
					playerLayout = (LinearLayout) findViewById(R.id.tempL);
					ImageButton[] tempCC = new ImageButton[4];
					tempCC[0] = (ImageButton) findViewById(R.id.tempCard1);
					tempCC[1] = (ImageButton) findViewById(R.id.tempCard2);
					tempCC[2] = (ImageButton) findViewById(R.id.tempCard3);
					tempCC[3] = (ImageButton) findViewById(R.id.tempCard4);

					playerLayout.setVisibility(View.VISIBLE);
					ArrayList<Card> k = new ArrayList<Card>();
					k = gm.get4Card();
					if (k.size() > 0) {
						for (int i = 0; i < k.size(); i++) {
							tempCC[i].setVisibility(View.VISIBLE);
							printACard(tempCC[i], k.get(i));
						}
						for (int i = k.size(); i < 4; i++) {
							tempCC[i].setVisibility(View.INVISIBLE);
						}
					}
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							playerLayout.setVisibility(View.INVISIBLE);
							ennAll();
						}
					}, 2000);
				}
			}
		});
	}

	/**
	 * Επιλογή πότε θα παίζει ο παίκτης
	 * 
	 */
	public void playerOrder() {
		invAll();
		remPackOfCards.setVisibility(View.INVISIBLE);
		iplayfisrt = (Button) findViewById(R.id.playfirst);
		iplaysecond = (Button) findViewById(R.id.playsecond);
		textwho = (TextView) findViewById(R.id.whoplaysfirst);

		iplayfisrt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iplayfisrt.setVisibility(View.INVISIBLE);
				iplaysecond.setVisibility(View.INVISIBLE);
				textwho.setVisibility(View.INVISIBLE);
				remPackOfCards.setVisibility(View.VISIBLE);
				numOfhuman = 0;
				numOfcomp = 1;
				init();
			}

		});

		iplaysecond.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iplayfisrt.setVisibility(View.INVISIBLE);
				iplaysecond.setVisibility(View.INVISIBLE);
				textwho.setVisibility(View.INVISIBLE);
				remPackOfCards.setVisibility(View.VISIBLE);
				numOfhuman = 1;
				numOfcomp = 0;
				init();
			}

		});

	}

	private void invAll() {
		for (int i = 0; i < numOfCardsOnHand; i++) {
			ib[i].setVisibility(View.INVISIBLE);
			ib2[i].setVisibility(View.INVISIBLE);

		}
		for (int i = 0; i < 6; i++) {
			ksR[i].setVisibility(View.INVISIBLE);
			ksL[i].setVisibility(View.INVISIBLE);

		}
		playedCards.setVisibility(View.INVISIBLE);
	}

	/**
	 * Απενεργοποίηση των χαρτιών του παίκτη
	 */
	private void disAll() {
		for (int i = 0; i < numOfCardsOnHand; i++) {
			ib[i].setEnabled(false);
			ib[i].setColorFilter(Color.argb(150, 0, 0, 0), Mode.SRC_ATOP);
			// κάνει τα χαρτιά πιο σκούρα ώστε να φαίνεται ότι είναι
			// απενεργοποιημένα
		}
	}

	/**
	 * Ενεργοποίηση των χαρτιών του παίκτη
	 */
	private void ennAll() {
		for (int i = 0; i < numOfCardsOnHand; i++) {
			ib[i].setEnabled(true);
			ib[i].setColorFilter(Color.argb(0, 0, 0, 0), Mode.SRC_ATOP);
		}
	}

	/**
	 * Εμφάνιση των χαρτιών που έχουν γίνει ξερή
	 */
	private void infKseri() {
		int i = 0;
		for (Card c : players[numOfhuman].getKseres()) {
			if (i < 6) {
				printACard(ksL[i], c);
				i++;
			}
		}
		i = 0;
		for (Card c : players[numOfcomp].getKseres()) {
			if (i < 6) {
				printACard(ksR[i], c);
				i++;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
