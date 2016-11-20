package Container;

import java.util.ArrayList;

/**
 * ConnectedDevices Manage the msgs recevied or sent on an connections.
 * It is an ArrayList containing ConenctionStorage. 
 * Thus it contains every message per connection.
 * And is able to add Msgs as String to the correct connection.
 * It gets the Msgs as an InternMessage.
 * This class is important for the view to print the msgs of a connection.
 */
public class ConnectedDevices extends ArrayList<ConnectionStorage> {

	/**
	 * Add a msg to a specific connection
	 * @param INternMessage msg this msg contains the connections id
	 */
	public void newMsgById(InternMessage msg) {
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).getId() == msg.id)
				this.get(i).saveMSG(msg.msg);
	}

	public ArrayList<String> getMsgsById(int id) {
		for (ConnectionStorage d : this)
			if (d.getId() == id)
				return d.getMsgs();

		return null;
	}

	public ArrayList<Integer> getIDList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < this.size(); i++)
			list.add(this.get(i).getId());
		return list;
	}

	public void newConnection(int id) {
		this.add(new ConnectionStorage(id));
	}

}

/**
 * Nested class it is used to save all msgs sent or received by a single
 * Connection
 *
 */
class ConnectionStorage {

	private int id;
	private ArrayList<String> msgs;

	public ConnectionStorage(int _id) {
		id = _id;
		msgs = new ArrayList<>();
	}

	public void saveMSG(String msg) {
		msgs.add(msg);
	}

	public ArrayList<String> getMsgs() {
		return msgs;
	}

	public int getId() {
		return id;
	}
}