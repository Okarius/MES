package Container;

import java.util.ArrayList;

/**
 * 
 * @author root
 *
 */
public class ConnectedDevices extends ArrayList<ConnectionStorage> {

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

	public ArrayList<Integer> getIDList(){
		ArrayList<Integer>  list = new ArrayList<Integer>();
		for (int i = 0; i < this.size(); i++)
			list.add(this.get(i).getId());
		return list;
	}

	public void newConnection(int id) {
		this.add(new ConnectionStorage(id));
	}

}

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