package Container;

import java.util.ArrayList;

import Container.InternMessage.WhatMsg;

/**
 * The Storage is the servers Message Managing object. The Server Class holds an
 * Reference of the Storage. The Server passes every incomig InternMessage to
 * the Storage. Thus the storage menages the InterMessages. The storage is then
 * Used by the view to update the GUI.
 */
public class Storage {
	private ConnectedDevices connectedDevices;
	private ArrayList<InternMessage> debuggMsgs;

	public Storage() {
		connectedDevices = new ConnectedDevices();
		debuggMsgs = new ArrayList<InternMessage>();
	}

	public int getNumberOfConections() {
		return connectedDevices.size();
	}

	/**
	 * When ever the server get a new InternMessage it passes it to the storage.
	 * Thus the storages job is it to save the message correctly. debuggMsgs get
	 * saved in the order they arrive. And ConnectionMsgs have to get checked if
	 * they are new or old Connections.
	 */
	public void newConnectionMessage(InternMessage msg) {
		if (msg.whatMsg == WhatMsg.CONNECTIONMSG) {
			if (msg.firstMsg)
				connectedDevices.newConnection(msg.id);
			else {
				connectedDevices.newMsgById(msg);
			}
		}
		debuggMsgs.add(msg);
	}

	public ArrayList<String> getAllMsgsString() {
		ArrayList<String> list = new ArrayList<String>();
		for (InternMessage i : debuggMsgs) {
			list.add(i.msg);
		}
		return list;
	}

	// ************ Funktion View reads to update******************//

	public ArrayList<Integer> getAllIdsFromRunningConnections() {
		return connectedDevices.getIDList();
	}

	public ArrayList<String> getAllMsgsFromConnectionByID(int id) {
		return connectedDevices.getMsgsById(id);
	}

	public ArrayList<InternMessage> getAllMsgs() {
		return debuggMsgs;
	}

	public InternMessage getLastDebugMsg() {
		return debuggMsgs.get(debuggMsgs.size() - 1);
	}

	public boolean connectionsChanged() {
		InternMessage msg = getLastDebugMsg();
		if (msg.firstMsg)
			return true;
		if (msg.lastMsg)
			return true;
		return false;
	}

}