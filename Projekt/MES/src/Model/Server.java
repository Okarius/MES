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
 * The server creates the first Thread which waits for connections
 * (ConnectionThread). It observes this Thread and gets Observed by the view.
 * The Controller holds an reference of the Server. When the server gets an
 * Update by the ConnectionThread he notifies the View. To notify the view the
 * Server uses his "Storage" objekt. The Storage holds and manages every massage
 * send by a client and a lot of debug msgs.
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

	/**
	 * Gets called in the Controllers Constructor. Creates the Thread which
	 * waits for new Connections (ConnectionThread). This thread gets observed
	 * by the server.
	 */
	public void newConnection() {
		allConnectionsRunnable.addObserver(this);
		Thread waitThread = new Thread(allConnectionsRunnable);
		waitThread.start();
	}

	/**
	 * Gets update by his Thread. The server passes the threads InternMassage to
	 * his Storage and afterwards updates the View.
	 */
	@Override
	public void update(Observable o, Object arg) {
		InternMessage msg = (InternMessage) arg;
		storage.newConnectionMessage(msg);
		changeData(storage);
	}

	private void changeData(Object data) {
		setChanged();
		notifyObservers(data);
	}

	public Storage getStorage() {
		return storage;
	}

}
