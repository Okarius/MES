package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Observable;

import javax.microedition.io.StreamConnection;

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
	private byte[] lastHeaderSend;
	private byte[] lastPayloadSend;
	private int errorCounter;
	private boolean alwaysDouble;

	public ClientConnectionRunnable(StreamConnection connection, int id) {
		conn = connection;
		serviceManager = new ServiceManager();
		headerWorker = new HeaderWorker();
		this.id = id;
		errorCounter = 0;
		alwaysDouble = false;
	}

	@Override
	public void run() {
		changeData(new InternMessage("New Connection, ID: ", this.id));
		try {
			DataInputStream din = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while (true) {
				byte[] buffer = new byte[(int) Math.pow(10, 3)];
				// String readMessage;
				if (din.available() > 0) {
					try {
						// Read from the InputStream
						int bytes = din.read(buffer);
						String readMessage = new String(buffer, 8, bytes - 9);
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
		lastHeaderSend = headerToSend;
		lastPayloadSend = payLoadToSend;
		switch (errorCounter) {
		case 1: // Corrupted Checksum
			System.out.println("Corrupted Checksum");
			headerToSend[5] = (byte) (headerToSend[5] + 1);
			break;
		case 2: // Corrupted Length 0
			System.out.println("Corrupted Length 0");
			headerToSend[0] = (byte) 0;
			headerToSend[1] = (byte) 0;
			headerToSend[2] = (byte) 0;
			headerToSend[3] = (byte) 0;
			break;
		case 3: // Corrupted Length Huge
			System.out.println("Corrupted Length Huge");
			headerToSend[0] = (byte) 0xFF;
			headerToSend[1] = (byte) 0xFF;
			headerToSend[2] = (byte) 0xFF;
			headerToSend[3] = (byte) 0xFF;
			break;
		case 4: // Corrupted Length Size 4
			System.out.println("Corrupted Length 0");
			headerToSend[0] = (byte) 0;
			headerToSend[1] = (byte) 0;
			headerToSend[2] = (byte) 0;
			headerToSend[3] = (byte) 4;
			break;
		case 5:
			// send 100 Messages
			System.out.println("Send 100 Messages");
			for (int i = 0; i < 100; i++) {
				System.arraycopy(headerToSend, 0, messageToSend, 0, headerToSend.length);
				System.arraycopy(payLoadToSend, 0, messageToSend, headerToSend.length, payLoadToSend.length);
				out.write(messageToSend);
			}
		case 6:
			// Send one ID in the future
			System.out.println("Send one ID in the future");
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.STRING,
					(short) (headerObjectReceived.id + 1), false);
		case 7:
			// Send one ID in the past
			System.out.println("Send one ID in the past");
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.STRING,
					(short) (headerObjectReceived.id - 1), false);
		case 8:
			// Send RAW
			System.out.println("Send RAW");
			headerToSend = headerWorker.makeHeader(payLoadToSend, ContentType.RAW, (short) (headerObjectReceived.id),
					false);
		case 9:
			errorCounter = 0;
		default:
			break;
		}

		System.arraycopy(headerToSend, 0, messageToSend, 0, headerToSend.length);
		System.arraycopy(payLoadToSend, 0, messageToSend, headerToSend.length, payLoadToSend.length);

		out.write(messageToSend);
		if (alwaysDouble) {
			out.write(messageToSend);
		}
		updateObserver(serviceManager.getLastMsgSend(), headerToSend, false);
		errorCounter++;

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
