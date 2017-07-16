package com.example.xeri;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Η κλάση στην οποία ο χρήστης πρέπει να ενεργοποιήσει το bluetooth ώστε να
 * μπορεί να παίξει με αντίπαλο κάποιον άλλο παίκτη
 * 
 * @author Tripkos Athanasios
 * 
 */
public class BluetoothEnabler extends Activity {
	BluetoothAdapter bluetoothAdapter;
	Button b;
	Button cont;
	Button back;
	boolean isDisc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_enablexml);
		b = (Button) findViewById(R.id.enablebluetooth);
		cont = (Button) findViewById(R.id.continue11);
		back = (Button) findViewById(R.id.gotomenu);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		isDisc = false;
		TextView v = (TextView) findViewById(R.id.infforbl);
		v.setText("Παιχνίδι δύο χρηστών με την χρήση bluetooth.\n\n "
				+ "Για να παίξετε πρέπει το bluetooth σας να είναι ενεργοποιημένο και ορατό. "
				+ "Μπορείτε να το ενεργοποιήσετε εύκολα παρακάτω.");

		init();
	}

	void init() {
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent makediscov = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				makediscov.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
				startActivity(makediscov);
				isDisc = true;
			}
		});
		cont.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (bluetoothAdapter.isEnabled()) {
					Intent i = new Intent(BluetoothEnabler.this,
							BluetoothConnection.class);
					startActivity(i);
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Το bluetooth σας δεν είναι ενργοποιημένο. Ενεργοποιήστε το και πατήστε Συνέχεια.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
