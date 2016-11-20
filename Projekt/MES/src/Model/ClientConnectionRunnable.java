package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Observable;

import javax.microedition.io.StreamConnection;

import Container.InternMessage;
import Services.PhoneNumberHandler;
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

	public ClientConnectionRunnable(StreamConnection connection, int id) {
		conn = connection;
		serviceManager = new ServiceManager();
		this.id = id;
	}

	@Override
	public void run() {
		try {
			changeData(new InternMessage("Start ConnectionsThread"));
			DataInputStream din = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while (true) {
				byte[] buffer = new byte[(int) Math.pow(10, 9)];
				String readMessage;
				int bytes;
				if (din.available() > 0) {
					try {
						// Read from the InputStream
						bytes = din.read(buffer);
						readMessage = new String(buffer, 0, bytes);
						changeData(new InternMessage(readMessage, true, this.id));
						respondToClient(out, readMessage);
					} catch (Exception e) {
						changeData(new InternMessage("Stop ConnectionsThreadUpper"));
					}
				} else {
					// Thread.sleep(100);
				}
			}
		} catch (Exception e) {
			changeData(new InternMessage("Stop ConnectionsThreadBottom"));
		}
	}

	public void startThis() {
		Thread t = new Thread(this);
		t.start();
	}

	// Services; 0 == Telefondienst
	/**
	 * This funktion handles every incoming msg and deceides how to answer. It
	 * also send the Msgs thus it gets the DataOutputStream. What the Server
	 * answers is decided by the ServiceMangaer which uses the Service itself.
	 * After the Answer is created the Observer(AllConnectionsRunnable) will be
	 * informed about this message
	 * 
	 * @param out
	 * @param lineRead
	 * @throws IOException
	 */
	private void respondToClient(DataOutputStream out, String lineRead) throws IOException {
		byte[] payLoadToSend = serviceManager.getAnswer(lineRead);
		changeData(new InternMessage(serviceManager.getLastMsgSend(), false, this.id));
		// for (int i = 0; i < payLoadToSend.length; i++) {
		// out.write(payLoadToSend[i]);
		// }
		out.write(payLoadToSend);

	}

	private void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

}
