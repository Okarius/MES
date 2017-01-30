package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Arrays;
import java.util.Observable;

import javax.microedition.io.StreamConnection;

import Container.InternMessage;
import Model.HeaderWorker.ContentType;
import Services.ServiceManager;

/**
 * This Runnable is used in a thread which represents a single Client-Server
 * connection. They are separated by an id. have their own StreamConnection and
 * every connection has its own serviceManager to create the answers. This
 * Runnable is used to create Threads in the AllConnectionsRunnable. Every
 * connection gets its own Thread. This Runnable is Observed by the
 * AllConnectionsRunanble. It passes every message sent and received forward.
 * 
 * @author Nikolas+Nico
 *
 */
public class ClientConnectionRunnable extends Observable implements Runnable {

	private StreamConnection conn;
	private int id;
	private ServiceManager serviceManager;
	private HeaderWorker headerWorker;
	private byte[] lastHeaderSend;
	private byte[] lastPayloadSend;
	private int requestCount;
	private long intervallBegin;
	Thread t;

	public ClientConnectionRunnable(StreamConnection connection, int id) {
		conn = connection;
		intervallBegin = System.currentTimeMillis();
		serviceManager = new ServiceManager();
		headerWorker = new HeaderWorker();
		this.id = id;
	}
	/**
	 * Function representing the Connection
	 * It receives messages and calls the functions to process incoming messages
	 */
	@Override
	public void run() {
		changeData(new InternMessage("New Connection, ID: ", this.id));
		try {
			DataInputStream din = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while (true) {
				// Dos Defense. if the server gets more than 10message/2seconds
				if (intervallBegin + 2000 < System.currentTimeMillis()) {
					intervallBegin = System.currentTimeMillis();
					requestCount = 0;
				}
				// Then shut down connection
				if (requestCount > 10)
					t.stop();

				byte[] buffer = new byte[(int) Math.pow(10, 3)];
				// String readMessage;
				if (din.available() > 0) {
					try {
						// Read from the InputStream
						int bytes = din.read(buffer);
						String readMessage = new String(buffer, 8, bytes - 9);
						respondToClient(out, buffer, readMessage);
						requestCount++;
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			InternMessage msg = new InternMessage("Stop ConnectionsThread", this.id);
			msg = msg.connectionGone();
			changeData(msg);
		}
	}

	public void startThis() {
		t = new Thread(this);
		t.start();
	}

	/**
	 * This function handles every incoming message and decides how to answer. It
	 * also send the message thus it gets the DataOutputStream. What the Server
	 * answers is decided by the ServiceMangaer which uses the Service itself.
	 * After the Answer is created the Observer(AllConnectionsRunnable) will be
	 * informed about this message
	 * 
	 * @param out This is the Stream on which the message will be seand
	 * @param lineRead the received message as byte[]
	 * @param readMessage received message as string
	 * @throws IOException
	 */
	private void respondToClient(DataOutputStream out, byte[] lineRead, String readMessage) throws IOException {
		byte[] headerToSend = new byte[0];
		byte[] payLoadToSend = new byte[0];
		byte[] messageToSend = new byte[0];
		byte[] headerReceived = Arrays.copyOfRange(lineRead, 0, 8);
		byte[] payloadReceivedBytes = Arrays.copyOfRange(lineRead, 8, lineRead.length);
		HeaderStorage headerObjectReceived = headerWorker.getHeaderStorageObject(headerReceived);
		updateObserver(new String(payloadReceivedBytes, "UTF-8"), headerReceived, true);
		if (headerObjectReceived.faultyBit) { // did the last message failed?
			// If it failed resend it!
			headerToSend = lastHeaderSend;
			payLoadToSend = lastPayloadSend;
		} else { // Last Package tge server sent was ok
			// Check if Checksum is correct
			if (headerWorker.isChecksumCorrect(payloadReceivedBytes, headerObjectReceived.checkSum)) {
				payLoadToSend = serviceManager.getAnswer(new String(readMessage));
				headerToSend = getHeaderToSend(readMessage, payLoadToSend, headerObjectReceived);
			} else {
				payLoadToSend = "Error;Faulty Checksum".getBytes();
				headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.STRING, headerObjectReceived.id,
						false);
			}
		}
		byte endsymbol = 0;
		payLoadToSend = addElement(payLoadToSend, endsymbol);
		messageToSend = new byte[headerToSend.length + payLoadToSend.length];
		System.arraycopy(headerToSend, 0, messageToSend, 0, headerToSend.length);
		System.arraycopy(payLoadToSend, 0, messageToSend, headerToSend.length, payLoadToSend.length);
		out.write(messageToSend);
		lastHeaderSend = headerToSend;
		lastPayloadSend = payLoadToSend;
		updateObserver(serviceManager.getLastMsgSend(), headerToSend, false);
	}

	private byte[] getHeaderToSend(String readMessage, byte[] payLoadToSend, HeaderStorage headerObjectReceived) {
		byte[] headerToSend = new byte[0];
		// Check if the headerToSend needs string or raw written in
		if (serviceManager.pictureRequested(new String(readMessage))) {
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.RAW, headerObjectReceived.id, false);
		} else {
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.STRING, headerObjectReceived.id, false);
		}
		return headerToSend;
	}
	
	

	private void updateObserver(String MsgSend, byte[] header, boolean from) {
		HeaderStorage headerObjUpdate = headerWorker.getHeaderStorageObject(header);
		changeData(new InternMessage("PayLoad: " + MsgSend, from, this.id));
		changeData(new InternMessage(header, from, this.id, headerObjUpdate.length, headerObjUpdate.id,
				headerObjUpdate.checkSum, headerObjUpdate.faultyBit));
	}

	private void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

	static byte[] addElement(byte[] a, byte e) {
		a = Arrays.copyOf(a, a.length + 1);
		a[a.length - 1] = e;
		return a;
	}

}
