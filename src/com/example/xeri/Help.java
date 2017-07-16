package com.example.xeri;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

/**
 * Προβολή των κανόνων του παιχνιδιού
 * 
 * @author Tripkos Athanasios
 */
public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpxml);
		TextView v1 = (TextView) findViewById(R.id.helptext1);
		v1.setText("Πως παίζεται η Ξερή");
		TextView v2 = (TextView) findViewById(R.id.helptext2);
		v2.setText("   Παίζετε με 2 ή 4 άτομα και χρησιμοποιείται μια τράπουλα των 52 φύλλων.\n"
				+ "Πριν αρχίσει ο πρώτος γύρος ρίχνονται 4 κάρτες στο τραπέζι η μία πάνω στην άλλη.");
		TextView v3 = (TextView) findViewById(R.id.helptext3);
		v3.setText("   Οι δύο παίκτες παίζουν εναλλάξ, ρίχνοντας ένα χαρτί από αυτά που τους έχουν μοιραστεί στο τραπέζι. "
				+ "Αν ένας παίκτης ρίξει χαρτί με ίδιο αριθμό με αυτό που είχε ριχτεί τελευταίο, μαζεύει τα χαρτιά που βρίσκονται στο τραπέζι. Αν στο τραπέζι βρισκόταν ένα μόνο χαρτί, κάνει «ξερή». "
				+ "Αν ένας παίκτης ρίξει βαλέ, παίρνει όλα τα χαρτιά που βρίσκονται στο τραπέζι."
				+ "Νικητής είναι αυτός που στο τέλος του παιχνιδιού θα έχει τους περισσότερους πόντους.");
		TextView v4 = (TextView) findViewById(R.id.helptext4);
		v4.setText("    ΠΟΝΤΟΙ:\n" + "Ξερή: 10 πόντοι \n"
				+ "Ξερή σε βαλέ: 20 πόντοι \n"
				+ "Άσσοι, K, Q, J, 10, 2 σπαθί: 1 πόντος\n"
				+ "10 καρό: 2 πόντοι");
		TextView v5 = (TextView) findViewById(R.id.helptext5);

		v5.setText("Το μυστικό στην ξερή ειναι να θυμάσαι τα φύλλα που έχουν περάσει για να μπορείς"
				+ " να μαντεύεις εύκολα τι φύλλα μπορεί να κρατάει ο αντίπαλος παίχτης. ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

}
