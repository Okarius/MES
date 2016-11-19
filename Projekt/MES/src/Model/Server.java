package Model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import Container.ConnectedDevices;
import Container.InternMessage;
import Container.Storage;
import Container.InternMessage.WhatMsg;

/**
 * 
 * @author Nikolas
 *
 */
public class Server extends Observable implements Observer {
	private Storage storage;
	private Thread waitThread;
	private AllConnectionsRunnable allConnectionsRunnable;

	public Server() {
		allConnectionsRunnable = new AllConnectionsRunnable();
		storage = new Storage();
	}

	public void newConnection() {
		allConnectionsRunnable.addObserver(this);
		Thread waitThread = new Thread(allConnectionsRunnable);
		waitThread.start();
	}

	private void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

	@Override
	public void update(Observable o, Object arg) {
		InternMessage msg = (InternMessage) arg;
		storage.newConnectionMessage(msg);
		changeData(storage);
	}

	public Storage getStorage() {
		return storage;
	}

}
