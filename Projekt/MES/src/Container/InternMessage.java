package Container;

public class InternMessage {
	public enum WhatMsg {
		DEBUGMSG, CONNECTIONMSG
	}

	public WhatMsg whatMsg;
	public String msg;
	public boolean firstMsg, from;
	public int id;
	/**
	 * Constructor for new Connections
	 * @param _msg
	 * @param _from
	 * @param _id
	 */
	public InternMessage( String _msg, boolean _firstMsg, boolean _from, int _id) {
		whatMsg = WhatMsg.CONNECTIONMSG;
		msg = _msg;
		firstMsg = _firstMsg;
		from = _from;
		id = _id;
	}
	/**
	 * Constructor for old Connections
	 * @param _msg
	 * @param _from
	 * @param _id
	 */
	public InternMessage(String _msg, boolean _from, int _id) {
		whatMsg =WhatMsg.CONNECTIONMSG;
		msg = _msg;
		firstMsg = false;
		from = _from;
		id = _id;
	}
	
	
	/**
	 * Constructor for DebugMsgs
	 * @param _msg
	 */
	public InternMessage(String _msg) {
		whatMsg = WhatMsg.DEBUGMSG;
		msg = _msg;
		id = -1;
	}

}
