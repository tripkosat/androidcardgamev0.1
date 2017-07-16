package com.example.xeri;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * Η σύνδεση με τον server 
 * Κύρια λειτουργία η επίτευξη σύνδεσης για την απόστολή
 * μηνυμάτων
 * 
 * @author Tripkos Athanasios
 * 
 */
public class ConnectToServerThread extends Thread {
	public CommsThread commsThread;

	public BluetoothSocket bluetoothSocket;
	private BluetoothAdapter bluetoothAdapter;

	public ConnectToServerThread(BluetoothDevice device,
			BluetoothAdapter btAdapter) {
		BluetoothSocket tmp = null;
		bluetoothAdapter = btAdapter;
		try {
			// η δημιουργία της σύνδεσης σύμφωνα με το κοινό UUID
			tmp = device.createRfcommSocketToServiceRecord(UUID
					.fromString(BluetoothConnection.UUID));
		} catch (IOException e) {
			Log.d("ConnectToServerThread", e.getLocalizedMessage());
		}
		bluetoothSocket = tmp;
	}

	@Override
	public void run() {
		bluetoothAdapter.cancelDiscovery();
		try {
			bluetoothSocket.connect();
			// το κανάλι επικοινωνίας
			commsThread = new CommsThread(bluetoothSocket);
			commsThread.start();
		} catch (IOException connectException) {
			// αποτυχία σύνδεσης
			try {
				bluetoothSocket.close();
			} catch (IOException closeException) {
				Log.d("ConnectToServerThread",
						closeException.getLocalizedMessage());
			}
			return;
		}
	}

	public void cancel() {
		try {
			bluetoothSocket.close();
			if (commsThread != null)
				commsThread.cancel();
		} catch (IOException e) {
			Log.d("ConnectToServerThread", e.getLocalizedMessage());
		}
	}
}