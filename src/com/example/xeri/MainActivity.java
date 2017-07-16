package com.example.xeri;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Η κλάση που είναι το αρχικό μενού και αναλαμβανει την μεταφορά από μια οθόνη
 * στην αλλή σύμφωνα με την επιλογή του χρήστη
 * 
 * @author Tripkos Athanasios
 * 
 */
public class MainActivity extends Activity {

	private Button newgame;
	private Button option;
	private Button helping;
	private Button multiplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		newgame = (Button) findViewById(R.id.newgame1);
		option = (Button) findViewById(R.id.options1);
		helping = (Button) findViewById(R.id.help1);
		multiplay = (Button) findViewById(R.id.twoplayer_game);
		init();
	}

	private void init() {

		newgame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(MainActivity.this, Game.class);
				startActivity(i);

			}
		});

		option.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Options.class);
				startActivity(i);
			}
		});

		helping.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Help.class);
				startActivity(i);
			}
		});

		multiplay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, BluetoothEnabler.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
