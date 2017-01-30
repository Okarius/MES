package Model;

import java.util.Observable;
import java.util.Observer;
import Container.InternMessage;
import Container.Storage;

/**
 * The server creates the first Thread which waits for connections
 * (ConnectionThread). It observes this Thread and gets Observed by the view.
 * The Controller holds an reference of the Server. When the server gets an
 * Update by the ConnectionThread he notifies the View. To notify the view the
 * Server uses his "Storage" Object. The Storage holds and manages every massage
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
	 * The server get updated by its allConnectionRunable which it observes.
	 * The server passes the threads InternMassage to
	 * his Storage and afterwards updates the View.
	 * @param Object arg this variable has the type InternMassage
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
