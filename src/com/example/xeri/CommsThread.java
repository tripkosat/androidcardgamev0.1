package com.example.xeri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
/**
 * Η κλάση για το λήψη και την αποστολή μηνυμάτων από το BluetoothSocket
 * 
 * @author Tripkos Athanasios
 */
public class CommsThread extends Thread {
	final BluetoothSocket bluetoothSocket;
	final InputStream inputStream;
	final OutputStream outputStream; 

	public CommsThread(BluetoothSocket socket) {
		bluetoothSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
		}
		inputStream = tmpIn;
		outputStream = tmpOut;
	}

	@Override
	/**
	 * Αυτή η συνάρτηση ακούει για μηνύματα όταν 
	 * μας στέλνει ο άλλος παίκτης κάτι
	 */
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;
		while (true) {
			try {
				// διάβασμα αν υπάρχει μήνυμα
				bytes = inputStream.read(buffer);

				// παραδίδουμε το μήνυμα στον Handler που υπάρχει στη
				// BluetoothConnnection κλάση
				BluetoothConnection.UIupdater.obtainMessage(0, bytes, -1,
						buffer).sendToTarget();
			} catch (IOException e) {
				break;
			}
		}
	}

	/**
	 * Καλούμε αυτήν την συνάρτηση ώστε να αποστείλουμε ένα μήνυμα
	 * 
	 * @param str
	 */
	public void write(String str) {
		try {
			outputStream.write(str.getBytes());
		} catch (IOException e) {
		}
	}

	/**
	 * Για την ακύρωση του αντικειμένου
	 */
	public void cancel() {
		try {
			bluetoothSocket.close();
		} catch (IOException e) {
		}
	}
}
