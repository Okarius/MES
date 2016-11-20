package Container;

/**
 * The InternMessage class is used to pass debugMesages and messages sent or
 * received by clients comfortable to the server. It Conatins a lot of important
 * informations. If its the firstMsg thus a new Connection got created The
 * connection Id to pringt the Msgs seperate for every connection If its the
 * lastMsg thus this connection got stopped. Also its contains if its a DebugMsg
 * or a ConnectionMsg. Connection Msgs are msgs the Server Received or Sent to a
 * Client While DebugMsgs are msgs We use to Debug the System. Those are also
 * printed in the view.
 * 
 *
 */
public class InternMessage {
	public enum WhatMsg {
		DEBUGMSG, CONNECTIONMSG
	}

	public WhatMsg whatMsg;
	public String msg;
	public boolean firstMsg, from;
	public int id;
	public boolean lastMsg;

	/**
	 * Constructor for Connections
	 * 
	 * @param _msg
	 * @param _from
	 * @param _id
	 */
	public InternMessage(String _msg, boolean _from, int _id) {
		whatMsg = WhatMsg.CONNECTIONMSG;
		msg = _msg;
		id = _id;
		from = _from;
		firstMsg = false;
		lastMsg = false;

		if (from)
			msg = "From: " + msg;
		else
			msg = "To: " + msg;
	}

	/**
	 * Constructor for new Connections
	 * 
	 * @param _msg
	 * @param _from
	 * @param _id
	 */
	public InternMessage(String _msg, int _id) {
		whatMsg = WhatMsg.CONNECTIONMSG;
		msg = _msg;
		id = _id;
		firstMsg = true;
		lastMsg = false;
	}

	public void setAsLastMsg() {
		lastMsg = true;
	}

	/**
	 * Constructor for DebugMsgs
	 * 
	 * @param _msg
	 */
	public InternMessage(String _msg) {
		whatMsg = WhatMsg.DEBUGMSG;
		msg = _msg;
		id = -1;
		lastMsg = false;

	}

}
