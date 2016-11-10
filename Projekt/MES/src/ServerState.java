import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.Observable;
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

public class ServerState extends Observable {
	private ArrayList<String> inquiredDevices;

	public final UUID uuid = new UUID("0000110100001000800000805F9B34FB", false); // it
																					// can
																					// be
																					// generated
																					// randomly
	public final String name = "Echo Server"; // the name of the service
	public final String url = "btspp://localhost:" + uuid // the service url
			+ ";name=" + name + ";authenticate=false;encrypt=false;";
	LocalDevice local = null;
	StreamConnectionNotifier server = null;
	StreamConnection conn = null;

	public ServerState() {
		inquiredDevices = new ArrayList<String>();

	}

	/*
	 * This funktion performs inquire It is called after "inquire" button is
	 * pressed
	 */
	public void inquire() {
		/* Create Vector variable */
		final ArrayList<String> devicesDiscovered = new ArrayList<String>();
		try {
			final Object inquiryCompletedEvent = new Object();
			/* Clear Vector variable */
			devicesDiscovered.clear();
			/* Create an object of DiscoveryListener */
			DiscoveryListener listener = new DiscoveryListener() {
				public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
					// Get devices paired with system or in range(Without Pair)
					try {
						devicesDiscovered.add(btDevice.getFriendlyName(true));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				public void inquiryCompleted(int discType) {
					/* Notify thread when inquiry completed */
					synchronized (inquiryCompletedEvent) {
						inquiryCompletedEvent.notifyAll();
					}
				}

				/* To find service on bluetooth */
				public void serviceSearchCompleted(int transID, int respCode) {
				}

				/* To find service on bluetooth */
				public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				}
			};

			synchronized (inquiryCompletedEvent) {
				/* Start device discovery */
				boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,
						listener);
				if (started) {
					System.out.println("wait for device inquiry to complete...");
					inquiryCompletedEvent.wait();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* Return list of devices */
		inquiredDevices.addAll(devicesDiscovered);
		changeData(this.inquiredDevices);
		System.out.println(devicesDiscovered);
	}

	// this funktion resets model
	public void reset() {
		inquiredDevices.clear();
		changeData(this.inquiredDevices);
	}

	void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

	public void echoServer() {
		try {
			System.out.println("Setting device to be discoverable...");
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			System.out.println("Start advertising service...");
			server = (StreamConnectionNotifier) Connector.open(url);
			System.out.println("Waiting for incoming connection...");
			conn = server.acceptAndOpen();
			System.out.println("Client Connected...");
			DataInputStream din = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			System.out.println("got input");
			boolean firstMessage = true;
			while (true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(din));
				String lineRead = br.readLine();
				if (lineRead != null)
					if(!firstMessage){
						respondToClient(out,lineRead);
//						out.write((lineRead.toUpperCase()+"\n").getBytes());
					}
				if (firstMessage) {
					echoServer();
					out.write(("1;TelefonDienst"+"\n").getBytes());
					firstMessage = false;
				}

			}
		} catch (Exception e) {
			System.out.println("Exception Occured: " + e.toString());
		}
	}

	private void respondToClient(DataOutputStream out, String lineRead) throws IOException {
		String service = lineRead.split(";")[0];
		switch (lineRead) {
		case "Telefondienst":
			out.write(("10123125124"+"\n").getBytes());
			break;

		default:
			break;
		}
	}

}
