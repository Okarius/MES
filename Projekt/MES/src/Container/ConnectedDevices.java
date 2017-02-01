package Container;

import java.util.ArrayList;

/**
 * Manages the received messages. It is an ArrayList containing
 * ConenctionStorage. Thus it contains every message per connection. And is able
 * to add messages as String to the correct connection. It gets the messages as
 * an InternMessage. This class is important for the view to print the messages
 * of a connection.
 * 
 * @author niki
 */
public class ConnectedDevices extends ArrayList<ConnectionStorage> {

	/**
	 * Adds a message to a specific connection
	 * 
	 * @param msg
	 *            this message contains the connections id and content
	 */
	public void newMsgById(InternMessage msg) {
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).getId() == msg.id)
				this.get(i).saveMSG(msg.msg.replace("&", "\n"));
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

	public void removeConnection(int id) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).getId() == id)
				this.remove(i);
		}

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