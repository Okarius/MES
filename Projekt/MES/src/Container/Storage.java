package Container;

import java.util.ArrayList;

/**
 * The Storage is the servers Message managing class. The Server has a storage
 * Attribute. The Server passes every incoming InternMessage to the Storage.
 * Thus the storage manages the InterMessages. The storage is then Used by the
 * view to update the GUI.
 * 
 * @author niki
 */
public class Storage {
	private InternMessage lastMsg;
	private ConnectedDevices connectedDevices;

	public Storage() {
		lastMsg = new InternMessage("lol", -1);
		connectedDevices = new ConnectedDevices();
	}

	public int getNumberOfConections() {
		return connectedDevices.size();
	}

	/**
	 * Saves incoming InternMessage correctly ConnectionMsgs have to get checked
	 * if they are new or old Connections.
	 */
	public void newConnectionMessage(InternMessage msg) {
		lastMsg = msg;
		if (msg.firstMsg)
			connectedDevices.newConnection(msg.id);
		else {
			if (msg.lastMsg)
				connectedDevices.removeConnection(msg.id);
			else
				connectedDevices.newMsgById(msg);
		}

	}

	public void writeLogFiles(int id) {

	}

	// ************ Funktion View reads to update******************//

	public ArrayList<Integer> getAllIdsFromRunningConnections() {
		return connectedDevices.getIDList();
	}

	public ArrayList<String> getAllMsgsFromConnectionByID(int id) {
		return connectedDevices.getMsgsById(id);
	}

	public InternMessage getLastMsg() {
		return lastMsg;
	}

	public boolean connectionsChanged() {
		if (lastMsg.firstMsg)
			return true;
		if (lastMsg.lastMsg)
			return true;
		return false;
	}

}
