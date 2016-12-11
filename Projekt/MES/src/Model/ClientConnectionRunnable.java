package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Observable;

import javax.microedition.io.StreamConnection;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import Container.InternMessage;
import Model.HeaderWorker.ContentType;
import Services.ServiceManager;

/**
 * This Runnable is used in a thread which respresends a singel Client-Server
 * connection. They are spereated by an id. have their own StreamConnection and
 * every connection has its own serviceManager to create the answers. This
 * Runnable is used to create Threads in the AllConnectionsRunnable. Every
 * connection gets its own Thread. This Runnable is Observed by the
 * AllConnectionsRunanble. It passes every Msg sent and received forward.
 * 
 * @author Nikolas+Nico
 *
 */
public class ClientConnectionRunnable extends Observable implements Runnable {

	private StreamConnection conn;
	private int id;
	private ServiceManager serviceManager;
	private HeaderWorker headerWorker;

	public ClientConnectionRunnable(StreamConnection connection, int id) {
		conn = connection;
		serviceManager = new ServiceManager();
		headerWorker = new HeaderWorker();
		this.id = id;
	}

	@Override
	public void run() {
		try {
			changeData(new InternMessage("Start ConnectionsThread"));
			DataInputStream din = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while (true) {
				byte[] buffer = new byte[(int) Math.pow(10, 3)];
				// String readMessage;
				if (din.available() > 0) {
					try {
						// Read from the InputStream
						int bytes = din.read(buffer);
						String readMessage = new String(buffer, 8, bytes - 8);
						respondToClient(out, buffer, readMessage);
					} catch (Exception e) {
					}
				} else {
					// Thread.sleep(100);
				}
			}
		} catch (Exception e) {
			InternMessage msg = new InternMessage("Stop ConnectionsThread", this.id);
			msg = msg.connectionGone();
			changeData(msg);
		}
	}

	public void startThis() {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * This funktion handles every incoming msg and deceides how to answer. It
	 * also send the Msgs thus it gets the DataOutputStream. What the Server
	 * answers is decided by the ServiceMangaer which uses the Service itself.
	 * After the Answer is created the Observer(AllConnectionsRunnable) will be
	 * informed about this message
	 * 
	 * @param out
	 * @param lineRead
	 * @param readMessage
	 * @throws IOException
	 */
	private void respondToClient(DataOutputStream out, byte[] lineRead, String readMessage) throws IOException {

		String payloadReceived = readMessage;

		byte[] headerToSend = new byte[0];
		byte[] payLoadToSend = new byte[0];
		byte[] messageToSend = new byte[0];
		byte[] headerReceived = Arrays.copyOfRange(lineRead, 0, 8);
		byte[] payloadReceivedBytes = Arrays.copyOfRange(lineRead, 8, lineRead.length);
		System.out.println("convertierungWorks");
		updateObserver(payloadReceivedBytes, headerReceived, true);
		// Check if Checksum is correct
		if (headerWorker.isChecksumCorrect(payloadReceivedBytes,
				headerWorker.extractChecksumFromHeader(headerReceived))) {
			// CHecksum is correct
			payLoadToSend = serviceManager.getAnswer(new String(readMessage));
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.STRING,
					headerWorker.extractIdFromHeader(headerReceived), false);
		} else {
			// Checksum is faulty:
			payLoadToSend = "Error;Faulty Checksum".getBytes();
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.STRING,
					headerWorker.extractIdFromHeader(headerReceived), true);
		}
		messageToSend = new byte[headerToSend.length + payLoadToSend.length];
		System.out.println(messageToSend);
		System.arraycopy(headerToSend, 0, messageToSend, 0, headerToSend.length);
		System.arraycopy(payLoadToSend, 0, messageToSend, headerToSend.length, payLoadToSend.length);
		System.out.println("still");
		out.write(messageToSend);
		updateObserver(payLoadToSend, headerToSend, false);
	}

	private void updateObserver(byte[] payload, byte[] header, boolean from) {
		changeData(new InternMessage("PayLoad: " + new String(payload, StandardCharsets.UTF_8), from, this.id));
		changeData(new InternMessage(header, from, this.id, headerWorker.extractLengthFromHeader(header),
				headerWorker.extractIdFromHeader(header), headerWorker.extractChecksumFromHeader(header),
				headerWorker.extractFaultyBitFromHeader(header)));
	}

	private void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

}
