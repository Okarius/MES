package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Observable;

import javax.microedition.io.StreamConnection;

import Container.InternMessage;

/**
 * 
 * @author root
 *
 */
public class ClientConnectionRunnable extends Observable implements Runnable {

	private StreamConnection conn;
	private int id;

	public ClientConnectionRunnable(StreamConnection connection, int id) {
		conn = connection;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			changeData(new InternMessage("Start ConnectionsThread"));
			DataInputStream din = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			boolean firstMsg = true;
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
						if (readMessage != null)
							if (!firstMsg) {
								respondToClient(out, readMessage);
							}
						if (firstMsg) {
							out.write(("0;TelefonDienst").getBytes());
							changeData(new InternMessage("0;TelefonDienst", false, this.id));
							firstMsg = false;
						}
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
	private void respondToClient(DataOutputStream out, String lineRead) throws IOException {
		String sendMe = "";
		PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler();
		String service = lineRead.split(";")[0];
		switch (service) {
		case "0":
			String NumberName = lineRead.split(";")[1];
			sendMe = (phoneNumberHandler.getNumberByFirstName(NumberName) + "\n");
			break;
		case "Service":
			sendMe = ("0;TelefonDienst");
			break;
		default:
			sendMe = ("UNKOWN SERVICE " + service + "\n");
			break;
		}
		changeData(new InternMessage(sendMe, false, this.id));

		out.write(sendMe.getBytes());

	}

	private void changeData(Object data) {
		setChanged(); // the two methods of Observable class
		notifyObservers(data);
	}

}