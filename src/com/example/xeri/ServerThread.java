package com.example.xeri;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * To thread που υλοποιεί τον server
 * 
 * @author Tripkos Athanasios
 */
public class ServerThread extends Thread {
	private final BluetoothServerSocket bluetoothServerSocket;

	public ServerThread(BluetoothAdapter bluetoothAdapter) {
		BluetoothServerSocket tmp = null;
		try {
			tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Xeri",
					UUID.fromString(BluetoothConnection.UUID));
		} catch (IOException e) {
		}
		bluetoothServerSocket = tmp;
	}

	@Override
	public void run() {
		BluetoothSocket socket = null;

		while (true) {
			try {
				socket = bluetoothServerSocket.accept();
			} catch (IOException e) {
				break;
			}
			// Αν υπάρχει σύνδεση
			if (socket != null) {
				// thread που ακούει εισερχόμενα δεδομένα
				CommsThread commsThread = new CommsThread(socket);
				commsThread.run();
			}
		}
	}

	public void cancel() {
		try {
			bluetoothServerSocket.close();
		} catch (IOException e) {
		}
	}
}
