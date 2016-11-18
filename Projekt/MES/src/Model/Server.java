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
import Container.ConnectionStorage;
import Container.InternMessage;
import Container.InternMessage.WhatMsg;

/**
 * 
 * @author root
 *
 */
public class Server extends Observable implements Observer {
	private ConnectedDevices connectedDevices;
	private ArrayList<String> debuggMsgs;

	public final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
	public final String name = "Echo Server"; // the name of the service
	public final String url = "btspp://localhost:" + uuid // the service url
			+ ";name=" + name + ";authenticate=false;encrypt=false;";
	LocalDevice local = null;
	StreamConnectionNotifier server = null;
	StreamConnection conn = null;
	private Thread waitThread;
	private AllConnectionsRunnable allConnectionsRunnable;

	public Server() {
		connectedDevices = new ConnectedDevices();
		allConnectionsRunnable = new AllConnectionsRunnable();
		debuggMsgs = new ArrayList<String>();
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

	public int getNumberOfConections() {
		return connectedDevices.size();
	}

	@Override
	public void update(Observable o, Object arg) {
		InternMessage msg = (InternMessage) arg;
		if (msg.whatMsg == WhatMsg.CONNECTIONMSG) {
			if (msg.firstMsg)
				connectedDevices.add(new ConnectionStorage(msg.id));
			else {
				connectedDevices.newMsgById(msg);
			}
		}
		debuggMsgs.add("Debugger: " + msg.msg);
		changeData(arg);
	}

	public ArrayList<String> getAllMsgsFromConnectionByID(int id) {
		return connectedDevices.getMsgsById(id);
	}

	public ArrayList<String> getAllMsgs() {
		return debuggMsgs;
	}

}