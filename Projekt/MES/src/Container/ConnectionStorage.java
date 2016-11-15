package Container;

import java.util.ArrayList;

public class ConnectionStorage {

	private int id;

	private ArrayList<String> msgs;

	public ConnectionStorage() {
		msgs = new ArrayList<>();
		msgs.add(id + " is connected");
	}

	public ConnectionStorage(int _id) {
		id = _id;
		msgs = new ArrayList<>();
		msgs.add(id + " is connected");
	}

	public void saveMSGToDevice(String msg) {
		msgs.add("To: " + msg);
	}

	public void saveMSGFromDevice(String msg) {
		msgs.add("From: " + msg);

	}

	public ArrayList<String> getMsgs() {
		return msgs;
	}

	public int getId() {
		return id;
	}
}
