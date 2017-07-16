package com.example.xeri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Οι επιλογες-ρυθμίσεις ποθ μπορεί να κάνει ο χρήστης στο παιχνίδι
 * 
 * @author Tripkos Athanasios
 */
public class Options extends Activity {

	private int numOfCardsOnHand;
	private double diskolia;
	private SeekBar skb1;
	private TextView skbst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionxml);

		// Προκαθορισμένες τιμές
		numOfCardsOnHand = 6;
		diskolia = 0.7;

		// ---Αν υπάρχει αρχείο διαβάζει τις
		// τιμές και ανάλογα καθορίζει το γραφικό περιβάλλον---
		String FILENAME = "option";
		boolean existsFile = true;

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

		skb1 = (SeekBar) findViewById(R.id.skb);
		skbst = (TextView) findViewById(R.id.skbstatus);
		confRadioButton();
		// αρχικοποίηση της seekBar
		skb1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				// αυτή η μπάρα παίρνει τιμές από 0-100 και με την διαίρεση με
				// 100,
				// παίρνουμε εύρος 0-1 για τον καθορισμό της δυσκολίας
				diskolia = progresValue / 100.0;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				confRadioButton();
			}
		});

	}

	// Αρχικοποίηση των RadioButton
	void confRadioButton() {
		if (numOfCardsOnHand == 4) {
			RadioButton r1 = (RadioButton) findViewById(R.id.radioA1);
			r1.setChecked(true);
		} else if (numOfCardsOnHand == 6) {
			RadioButton r1 = (RadioButton) findViewById(R.id.radioA2);
			r1.setChecked(true);
		}

		RadioButton rb1 = (RadioButton) findViewById(R.id.radioB1);

		RadioButton rb2 = (RadioButton) findViewById(R.id.radioB2);
		RadioButton rb3 = (RadioButton) findViewById(R.id.radioB3);

		if (diskolia == 0.4) {
			rb1.setChecked(true);
			rb2.setChecked(false);
			rb3.setChecked(false);
		} else if (diskolia == 0.7) {
			rb1.setChecked(false);
			rb2.setChecked(true);
			rb3.setChecked(false);
		} else if (diskolia == 1) {
			rb1.setChecked(false);
			rb2.setChecked(false);
			rb3.setChecked(true);
		} else {
			rb1.setChecked(false);
			rb2.setChecked(false);
			rb3.setChecked(false);

		}
		skbst.setText(diskolia + "/" + skb1.getMax() / 100.0);

		skb1.setProgress((int) (diskolia * 100));
	}

	/**
	 * RadioButton για τον αριθμό των χαρτιών
	 * 
	 */
	public void onRadioButtonClicked(View view) {
		// Έλεγχος ποιο RadioButton είναι επελεγμένο
		boolean checked = ((RadioButton) view).isChecked();

		// Ανάλογος καθορισμός των ρυθμίσεων
		switch (view.getId()) {
		case R.id.radioA1:
			if (checked) {
				numOfCardsOnHand = 4;
			}
			break;
		case R.id.radioA2:
			if (checked) {
				numOfCardsOnHand = 6;
			}
			break;
		}
		confRadioButton();
	}

	/**
	 * RadioButton για την δυσκολία
	 * 
	 */
	public void onRadioButtonClicked1(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.radioB1:
			if (checked) {
				diskolia = 0.4;
			}
			break;
		case R.id.radioB2:
			if (checked) {
				diskolia = 0.7;
			}
			break;
		case R.id.radioB3:
			if (checked) {
				diskolia = 1;
			}
			break;
		}
		confRadioButton();
	}

	/**
	 * Αποθήκευση των αλλαγών σε αρχείο
	 * 
	 * @param view
	 * @throws IOException
	 */
	public void onButtonClick(View view) throws IOException {

		String FILENAME = "option";
		String string = "str " + numOfCardsOnHand + " " + diskolia + " str";
		FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		fos.write(string.getBytes());
		fos.close();
		Intent i = new Intent(Options.this, MainActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
