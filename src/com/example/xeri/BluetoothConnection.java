package com.example.xeri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Η κλάση που υλοποιεί το παιχνίδι δύο παικτών καθώς και την μεταξύ τους
 * σύνδεση
 * 
 * @author Τρίπκος Αθανάσιος
 */
public class BluetoothConnection extends Activity {
	// String ώστε να μπορούν οι συσκευές να ανταλλάσουν μηνύματα
	public final static String UUID = "3606f360-e4df-11e0-9572-0800200c9a66";

	// Αντικείμενα για την σύνδεση bluetooth
	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver discoverDevicesReceiver;
	private BroadcastReceiver discoveryFinishedReceiver;
	private ListView listOfDevices;

	private ArrayList<BluetoothDevice> discoveredDevices;
	private ArrayList<String> discoveredDevicesNames;
	private ServerThread serverThread;
	private ConnectToServerThread connectToServerThread;

	// Αντικείμενα για την προβολή στοιχείων στον παίκτη
	private static ImageButton[] ksR;
	private static ImageButton[] ksL;
	private static ImageButton playedCards;
	private static ImageButton[] ib;
	private static ImageButton[] ib2;
	private static Card[] ic;

	private static Context context;

	// Αντικείμενα για το παιχνίδι
	private static GamePlay gm;
	private static Player[] players;

	private static Button startthegame;
	private static Button makediscoverable;
	private Button discoverDev;

	// Αντικείμενα για την αρχικοποίηση του παιχνιδιού
	private static int numOfCardsOnHand;
	private static int seed;
	private static int numOfPlayer;
	private static boolean fasi1;
	private static boolean fasi2;

	private static int turn;
	static Button newGameButton;
	private static TextView text1;
	private static TextView text2;

	LinearLayout playerLayout;

	/**
	 * Σε αυτήν την συνάρτηση αρχικοποιούνται οι μεταβλητές του παιχνιδιού και
	 * ξεκινά η όλη διαδικασία της σύνδεσης
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayerxml);
		discoveredDevices = new ArrayList<BluetoothDevice>();
		discoveredDevicesNames = new ArrayList<String>();

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		listOfDevices = (ListView) findViewById(R.id.multidevicelist);
		context = getApplicationContext();
		Random r = new Random();
		seed = r.nextInt(2000000);
		turn = 0;
		numOfCardsOnHand = 6;
		numOfPlayer = 0;
		fasi1 = false;
		fasi2 = false;
		makediscoverable = (Button) findViewById(R.id.multimakeDisc);
		discoverDev = (Button) findViewById(R.id.multidiscDev);
		startthegame = (Button) findViewById(R.id.multistartthegame11);
		ib = new ImageButton[numOfCardsOnHand];
		ic = new Card[numOfCardsOnHand];
		ib2 = new ImageButton[numOfCardsOnHand];

		ib[0] = (ImageButton) findViewById(R.id.multiim1);
		ib[1] = (ImageButton) findViewById(R.id.multiim2);
		ib[2] = (ImageButton) findViewById(R.id.multiim3);
		ib[3] = (ImageButton) findViewById(R.id.multiim4);
		ib[4] = (ImageButton) findViewById(R.id.multiim61);
		ib[5] = (ImageButton) findViewById(R.id.multiim62);

		ib2[0] = (ImageButton) findViewById(R.id.multiim5);
		ib2[1] = (ImageButton) findViewById(R.id.multiim6);
		ib2[2] = (ImageButton) findViewById(R.id.multiim7);
		ib2[3] = (ImageButton) findViewById(R.id.multiim8);
		ib2[4] = (ImageButton) findViewById(R.id.multiim65);
		ib2[5] = (ImageButton) findViewById(R.id.multiim66);

		ksR = new ImageButton[6];
		ksL = new ImageButton[6];

		ksR[0] = (ImageButton) findViewById(R.id.multiksR0);
		ksR[1] = (ImageButton) findViewById(R.id.multiksR1);
		ksR[2] = (ImageButton) findViewById(R.id.multiksR2);
		ksR[3] = (ImageButton) findViewById(R.id.multiksR3);
		ksR[4] = (ImageButton) findViewById(R.id.multiksR4);
		ksR[5] = (ImageButton) findViewById(R.id.multiksR5);

		ksL[0] = (ImageButton) findViewById(R.id.multiksL0);
		ksL[1] = (ImageButton) findViewById(R.id.multiksL1);
		ksL[2] = (ImageButton) findViewById(R.id.multiksL2);
		ksL[3] = (ImageButton) findViewById(R.id.multiksL3);
		ksL[4] = (ImageButton) findViewById(R.id.multiksL4);
		ksL[5] = (ImageButton) findViewById(R.id.multiksL5);
		newGameButton = (Button) findViewById(R.id.multinewgame);
		text1 = (TextView) findViewById(R.id.multitextView1);
		text2 = (TextView) findViewById(R.id.multitextView2);
		playedCards = (ImageButton) findViewById(R.id.multiim0);

		initConnectionButtons();
	}

	/**
	 * Αρχικοποίηση του παιχνιδιού και δημιουργία παικτών
	 * */
	private void init() {
		visAll();

		gm = new GamePlay(numOfCardsOnHand, seed);
		players = new Player[2];

		// δύο παίκτες-ορισμός ονόματος ανάλογα με την σειρά που παίζει ο
		// καθένας
		if (numOfPlayer == 0) {
			players[0] = new HumanPlayer("Εσύ");
			players[1] = new HumanPlayer("Ο αντίπαλος σου");
		} else {
			players[1] = new HumanPlayer("Εσύ");
			players[0] = new HumanPlayer("Ο αντίπαλος σου");
		}

		invAll();
		newTurn();

		// ---Προβολή των πρώτων χαρτιών που βρίσκονται
		// στο τραπέζι για 3 δευτερόλεπτα---
		ArrayList<Card> k = gm.get4Card();

		playerLayout = (LinearLayout) findViewById(R.id.multitempL);
		ImageButton[] tempCC = new ImageButton[4];
		tempCC[0] = (ImageButton) findViewById(R.id.multitempCard1);
		tempCC[1] = (ImageButton) findViewById(R.id.multitempCard2);
		tempCC[2] = (ImageButton) findViewById(R.id.multitempCard3);
		tempCC[3] = (ImageButton) findViewById(R.id.multitempCard4);

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
				initGameButtons();
			}
		}, 3000);

	}

	/**
	 * Η συνάρτηση στην οποία γίνεται η εξερεύνηση για συσκευές προς σύνδεση
	 * 
	 * @param view
	 */

	public void DiscoverDevices(View view) {
		// Τοποθετούμε τις συσκευές που έχουμε εντοπίζει σε μια λίστα ώστε
		// ο χρήστης να μπορεί να επιλέξει με ποια θα συνδεθεί
		if (discoverDevicesReceiver == null) {
			discoverDevicesReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						if (!discoveredDevices.contains(device)) {
							discoveredDevices.add(device);
							discoveredDevicesNames.add(device.getName());
							listOfDevices.setAdapter(new ArrayAdapter<String>(
									getBaseContext(),
									android.R.layout.simple_list_item_1,
									discoveredDevicesNames));
							initConnectionButtons();
						}
					}
				}
			};
		}
		// Η εξερεύνηση έχει τελειώσει και μπορούμε να
		// επιλέξουμε την συσκευή με την οποία θέλουμε να συνδεθούμε
		if (discoveryFinishedReceiver == null) {
			discoveryFinishedReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					Toast.makeText(
							getBaseContext(),
							"Η εξερεύνηση τελείωσε. Επέλεξε συσκευή ώστε να αρχίσει το παιχνίδι.",
							Toast.LENGTH_SHORT).show();

					unregisterReceiver(discoveryFinishedReceiver);
					listOfDevices.setEnabled(true);
				}

			};
		}
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		IntentFilter filter2 = new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		registerReceiver(discoverDevicesReceiver, filter1);
		registerReceiver(discoveryFinishedReceiver, filter2);

		listOfDevices.setEnabled(false);
		Toast.makeText(getBaseContext(),
				"Εξερεύνηση σε εξέλιξη. Παρακαλώ περιμένετε...",
				Toast.LENGTH_SHORT).show();

		bluetoothAdapter.startDiscovery();
	}

	/**
	 * Ο χειριστής των λαμβανόμενων μηνυμάτων ανάλογα με το μήνυμα που λαμβάνει
	 * πραγματοποιεί κάποια ενέγεια
	 */
	public static Handler UIupdater = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int numOfBytesReceived = msg.arg1;
			byte[] buffer = (byte[]) msg.obj;
			String strReceived = new String(buffer);

			strReceived = strReceived.substring(0, numOfBytesReceived);
			// λήψη χαρτιού
			if (strReceived.contains("card")) {
				Card c = new Card(strReceived);
				printACard(playedCards, c);

				int nop = 0;
				if (numOfPlayer == 0) {
					nop = 1;
				}
				ib2[(int) turn / 2].setVisibility(View.INVISIBLE);
				gm.checkOfCard(c, players[nop]);
				infKseri();
				c = gm.getTopCard();

				final Handler handler1 = new Handler();
				handler1.postDelayed(new Runnable() {
					@Override
					public void run() {
						printACard(playedCards, gm.getTopCard());
						infKseri();
						turn++;
						ennAll();
						result();
					}
				}, 400);

			} else if (strReceived.contains("startthegame")) {
				// λήψη αρχικοποίησης παιχνιδιού
				// λήψη του seed

				if (fasi2 == false) {

					Scanner scan = new Scanner(strReceived);
					fasi2 = true;
					scan.next();
					int seed2 = scan.nextInt();// 1
					// αρχικοποίηση σειράς
					if (seed2 > seed) {
						seed = seed2;
						numOfPlayer = 1;
					}
					scan.close();
					startthegame.setText("Αρχικοποίηση Παιχνιδιού");
				}
				if (fasi1 == true && fasi2 == true) {
					startthegame.setText("Ξεκινήστε Το Παιχνίδι");
				}

			} else if (strReceived.contains("newgame")) {
				/*
				 * λήψη αιτήματος νέου παιχνιδιού αφού έχει ολοκληρωθεί ήδη ένα
				 */
				text1.setVisibility(View.INVISIBLE);
				text2.setVisibility(View.INVISIBLE);
				newGameButton.setVisibility(View.INVISIBLE);
				Random r = new Random();

				seed = r.nextInt(2000000);
				turn = 0;
				numOfPlayer = 0;
				fasi1 = false;
				fasi2 = false;

				startthegame.setVisibility(View.VISIBLE);
				startthegame.setText("Αρχικοποίηση Παιχνιδιού");

			} else if (strReceived.contains("error")) {
				Toast.makeText(
						context,
						"Η άλλη συσκευή παρουσίασε πρόβλημα ή διακόπηκε η σύνδεση. Το παιχνίδι δεν μπορεί να συνεχιστεί.",
						Toast.LENGTH_LONG).show();
			}

		}
	};

	/**
	 * Η αποστολή ενός string στην συσκευή με την οποία υπάρχει σύνδεση
	 * 
	 */
	private class WriteTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... args) {
			try {
				connectToServerThread.commsThread.write(args[0]);
			} catch (Exception e) {

			}
			return null;
		}
	}

	/**
	 * Αρχικοποίηση των κουμπιών που απαιτούνται για την σύνδεση
	 * 
	 */
	private void initConnectionButtons() {

		// Επιλογή συσκευής προς σύνδεση
		listOfDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (connectToServerThread != null) {

					try {
						connectToServerThread.bluetoothSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				BluetoothDevice deviceSelected = discoveredDevices
						.get(position);
				connectToServerThread = new ConnectToServerThread(
						deviceSelected, bluetoothAdapter);

				connectToServerThread.start();
				Toast.makeText(
						getApplicationContext(),
						"Έχεις συνδεθεί με το "
								+ discoveredDevices.get(position).getName(),
						Toast.LENGTH_SHORT).show();

				invBlButtonAndList();
				startthegame.setVisibility(View.VISIBLE);
				initStartGameButton();
			}
		});
		// κάνε την εξερεύνηση
		discoverDev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DiscoverDevices(v);
			}
		});
		// κάνε το bluetooth εντοπίσιμο
		makediscoverable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
				startActivity(i);
			}
		});
	}

	/**
	 * Αρχικοποίηση των κουμπιών που απαιτούνται για την αρχικοποίηση του
	 * παιχνιδιού
	 */
	private void initStartGameButton() {
		startthegame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fasi1 == false) {
					// αποστολή του seed
					if (connectToServerThread != null) {
						String tmps = "startthegame " + seed + " ex "
								+ numOfPlayer + " dd";
						new WriteTask().execute(tmps);
					}
					fasi1 = true;
					startthegame.setText("...Περιμένετε...");
				}
				if (fasi1 == true && fasi2 == true) {
					// έχει ολοκληρωθεί η αρχικοποίηση
					startthegame.setVisibility(View.INVISIBLE);//
					// αποστολή πάλι του seed για την περίπτωση που έχει υπάρξει
					// κάποιο λάθος
					if (connectToServerThread != null) {
						String tmps = "startthegame " + seed + " ex "
								+ numOfPlayer + " dd";
						new WriteTask().execute(tmps);
					}

					init();
				}

			}

		});
		// Αποστολή αιτήματος νέου παιχνιδιού

		newGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (connectToServerThread != null) {
					new WriteTask().execute("newgame");
				}

				text1.setVisibility(View.INVISIBLE);
				text2.setVisibility(View.INVISIBLE);
				newGameButton.setVisibility(View.INVISIBLE);
				Random r = new Random();

				seed = r.nextInt(2000000);
				turn = 0;
				numOfPlayer = 0;
				fasi1 = false;
				fasi2 = false;

				startthegame.setVisibility(View.VISIBLE);
				startthegame.setText("Αρχικοποίηση Παιχνιδιού");
			}
		});
	}

	/**
	 * Αρχικοποίση των χαρτιών και των απαραίτητων για το παιχνίδι κουμπιών
	 */
	private void initGameButtons() {
		ib[0].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				playTheCard(0);
			}
		});

		ib[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				playTheCard(1);
			}
		});

		ib[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				playTheCard(2);
			}
		});

		ib[3].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				playTheCard(3);
			}
		});

		ib[4].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				playTheCard(4);
			}
		});

		ib[5].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				playTheCard(5);
			}
		});

	}

	/**
	 * κάνει αόρατα τα κουμπιά σύνδεσης
	 * 
	 */
	private void invBlButtonAndList() {
		discoverDev.setVisibility(View.INVISIBLE);
		makediscoverable.setVisibility(View.INVISIBLE);
		listOfDevices.setVisibility(View.INVISIBLE);
	}

	/**
	 * κάνει όρατα τα κουμπιά παιχνιδιού
	 * 
	 */
	private void visAll() {
		LinearLayout l = new LinearLayout(getApplicationContext());
		l = (LinearLayout) findViewById(R.id.multilinearPL2);
		l.setVisibility(View.VISIBLE);
		l.setEnabled(true);
		l = (LinearLayout) findViewById(R.id.multilinearPL1);
		l.setVisibility(View.VISIBLE);
		l.setEnabled(true);

		for (int i = 0; i < numOfCardsOnHand; i++) {
			ib[i].setColorFilter(Color.argb(0, 0, 0, 0), Mode.SRC_ATOP);
			ib[i].setVisibility(View.VISIBLE);
			ib[i].setEnabled(true);
			ib2[i].setVisibility(View.VISIBLE);
		}
		playedCards.setVisibility(View.VISIBLE);
		invBlButtonAndList();
	}

	/**
	 * Το παίξιμο της κάρτας από το παίκτη και η αποστολή του στον άλλον παίκτη
	 * 
	 * @param cardn
	 *            αριθμός κάρτας στον πίνακα ic, ο αριθμός της κάρτας προς
	 *            παίξιμο
	 */
	private void playTheCard(int cardn) {
		Card c = ic[cardn];
		turn++;//
		if (connectToServerThread != null) {
			new WriteTask().execute(c.toString());
		}
		printACard(playedCards, c);
		// έλεγχος του χαρτιού που παίχτηκε
		gm.checkOfCard(c, players[numOfPlayer]);
		printACard(playedCards, gm.getTopCard());
		infKseri();
		disAll();
		result();
	}

	/**
	 * Αν οι παίκτες δεν έχουν άλλα χαρτιά μπροστά τους Παρουσίαση αποτελεσμάτων
	 * αν δεν υπάρχουν άλλα χαρτιά προς μοίρασμα Νέος γύρος αν υπάρχουν
	 */
	private static void result() {
		if (turn == numOfCardsOnHand * 2 && gm.getSizeOfPack() == 0) {
			gm.endOfGame(players);
			invAll();

			String s1 = players[0].getName() + ": " + players[0].getpoints()
					+ " πόντους" + "\n" + "Ξερές: "
					+ players[0].getNumOfkseres();

			String s2 = players[1].getName() + ": " + players[1].getpoints()
					+ " πόντους" + "\n" + "Ξερές: "
					+ players[1].getNumOfkseres();

			text1.setVisibility(View.VISIBLE);
			text2.setVisibility(View.VISIBLE);
			text1.setText(s1);
			text2.setText(s2);
			newGameButton.setVisibility(View.VISIBLE);
			newGameButton.setEnabled(true);

		} else if (turn == numOfCardsOnHand * 2) {
			newTurn();
		}

	}

	/**
	 * Νέος γύρος με μοίρασμα νέων χαρτιών
	 * */
	private static void newTurn() {
		turn = 0;
		gm.moirasmaKartwn(players);

		cardOnHandToArray(players[numOfPlayer].getCardsOnHand());
		printACard(playedCards, gm.getTopCard());

		for (int l = 0; l < numOfCardsOnHand; l++) {
			ib2[l].setVisibility(View.VISIBLE);
			printACard(ib[l], players[numOfPlayer].getCardsOnHand().getCard(l));
		}

		printACard(playedCards, gm.getTopCard());

		if (numOfPlayer == 1) {
			disAll();
		}
	}

	/**
	 * η εκτύπωση ενός χαρτιού σε ένα ImageButton
	 */
	private static void printACard(ImageButton imb, Card c) {
		// Αν το χαρτί έχει αριθμό μηδέν, τότε το ImageButton γίνεται αόρατο
		if (c.getNumber().equals("0")) {
			imb.setVisibility(View.INVISIBLE);
		} else {
			imb.setVisibility(View.VISIBLE);
			imb.setImageResource(context.getResources().getIdentifier(
					"drawable/" + c.toString(), null, context.getPackageName()));
		}
	}

	public static void ennAll() {
		for (int i = 0; i < numOfCardsOnHand; i++) {
			ib[i].setEnabled(true);
			ib[i].setColorFilter(Color.argb(0, 0, 0, 0), Mode.SRC_ATOP);
		}
	}

	public static void disAll() {
		for (int i = 0; i < numOfCardsOnHand; i++) {
			ib[i].setEnabled(false);
			ib[i].setColorFilter(Color.argb(150, 0, 0, 0), Mode.SRC_ATOP);
		}
	}

	private static void invAll() {

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

	private static void cardOnHandToArray(CardsOnHand cot) {
		for (int i = 0; i < numOfCardsOnHand; i++) {
			ic[i] = cot.getCard(i);
		}
	}

	/**
	 * Εμφάνιση των χαρτιών που έχουν γίνει ξερή
	 */
	private static void infKseri() {
		int i = 0;
		for (Card c : players[numOfPlayer].getKseres()) {
			if (i < 6) {
				printACard(ksL[i], c);
				i++;
			}
		}

		i = 0;
		int nop = 0;
		if (numOfPlayer == 0) {
			nop = 1;
		}
		for (Card c : players[nop].getKseres()) {
			if (i < 6) {

				printACard(ksR[i], c);
				i++;
			}
		}
	}

	@Override
	/**
	 * Τι γίνεται όταν πατιέται το κουμπί back
	 */
	public void onBackPressed() {
		destroyConnection();
		super.onBackPressed();
	}

	@Override
	/**
	 * Τι συμβαίνει κατά την καταστροφή της εφαρμογής
	 */
	public void onDestroy() {
		destroyConnection();
		super.onDestroy();
	}

	@Override
	/**
	 * Οταν η εφαρμογή σταματάει για να εκτελεστεί κάποια άλλη λειτουργία π.χ.
	 * Ενεργοποίηση bluetooth
	 */
	public void onPause() {
		super.onPause();
		cancelConnection();
		if (this.isFinishing()) {
			destroyConnection();
		}
	}

	@Override
	/**
	 * Συνέχιση εφαρμογής από την onPause
	 */
	public void onResume() {

		super.onResume();
		serverThread = new ServerThread(bluetoothAdapter);
		serverThread.start();
	}

	/**
	 * Κλάση που χρσιμοποιείται κατή την καταστροφή και το back πλήκτρο ώστε να
	 * διακοπούν οι συνδέσεις, να απενεργοποιηθεί το bluetooth και να
	 * αρχικοποιηθούν οι μεταβλητές
	 */
	public void destroyConnection() {
		if (connectToServerThread != null) {
			new WriteTask().execute("error");
		}

		Random r = new Random();
		seed = r.nextInt(2000000);
		turn = 0;
		numOfPlayer = 0;
		fasi1 = false;
		fasi2 = false;
		Toast.makeText(context, "Η σύνδεση διακόπηκε.", Toast.LENGTH_SHORT)
				.show();
		cancelConnection();
		bluetoothAdapter.disable();
	}
/**
 * Χρησιμποιείται για την ακύρωση των threads που χρησιμοποιούνται για την επικοινωνία.
 */
	public void cancelConnection(){
		
		bluetoothAdapter.cancelDiscovery();
		if (discoverDevicesReceiver != null) {
			try {
				unregisterReceiver(discoverDevicesReceiver);
			} catch (Exception e) {

			}
		}
		if (connectToServerThread != null) {
			try {
				connectToServerThread.bluetoothSocket.close();
			} catch (IOException e) {

			}
		}
		if (serverThread != null) {
			serverThread.cancel();
		}
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
