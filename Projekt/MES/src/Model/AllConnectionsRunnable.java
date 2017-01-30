package Model;

import java.util.Observable;
import java.util.Observer;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import Container.InternMessage;

/**
 * This Runnable is used in the Servers waitThread. This runnable waits for new
 * connections. If a BT-Device tries to set up a connection this class creates a new Thread 
 * which represents the Connection. Thus we have one Connection Thread for each device.
 * This Class Observers its Connection Threads and gets Observed by the Server.
 * @author Niki,Nico
 *
 */
public class AllConnectionsRunnable extends Observable implements Runnable, Observer {
	public final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
	public final String name = "Echo Server"; // the name of the service
	public final String url = "btspp://localhost:" + uuid // the service url
			+ ";name=" + name + ";authenticate=false;encrypt=false;";
	LocalDevice local = null;
	StreamConnectionNotifier server = null;
	StreamConnection conn = null;
	private int lastId;

	public AllConnectionsRunnable() {
		super();
		lastId = 0;
	}

	@Override
	public void run() {
		waitForConnection();
	}


	/**
	 * This Function is Opens Streams and Waits for devices to Connect with the Server
	 * If a Device Connects a new Thread gets oppened. This Threads is the Connection
	 */
	private void waitForConnection() {
		// retrieve the local Bluetooth device object
		LocalDevice local = null;

		StreamConnectionNotifier notifier;
		StreamConnection connection = null;

		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			notifier = (StreamConnectionNotifier) Connector.open(url);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		// waiting for connection
		while (true) {
			try {
				connection = notifier.acceptAndOpen();
				// new connection->create new connection Thread
				ClientConnectionRunnable connThread = new ClientConnectionRunnable(connection, lastId);

				// update view with InterMessage (firstMessage = true)
				lastId++; // update id, thus every connection has different id
				// AllConnectionRunnable observes every connection thread
				connThread.addObserver(this);
				Thread processThread = new Thread(connThread);
				processThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	/**
	 * Update Server by pushing incoming InternMessage to it, 
	 * Or by informingthe Server about new Connections.
	 */
	@Override
	public void update(Observable o, Object arg) {
		changeData(arg);
	}

	private void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

}
