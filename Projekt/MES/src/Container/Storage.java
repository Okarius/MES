package Container;

import java.util.ArrayList;

import Container.InternMessage.WhatMsg;

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
		if(msg.firstMsg)
			return true;
		if(msg.lastMsg)
			return true;
		return false;
	}

}
