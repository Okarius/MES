import java.util.ArrayList;
import java.util.Observable;

public class ServerState extends Observable {
	private ArrayList<String> inquiredDevices;

	public ServerState() {
		inquiredDevices = new ArrayList<String>();
	}

	/*
	 * This funktion performs inquire It is called after "inquire" button is
	 * pressed
	 */
	public void inquire() {
		inquiredDevices.add("lol\n");
		changeData(this.inquiredDevices);
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

}
