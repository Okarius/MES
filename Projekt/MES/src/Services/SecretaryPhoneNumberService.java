package Services;

/**
 * The SecretaryPhoneNumberService is the easiest Service The Client hast to
 * send the Services Id If nothing else is send the Payload is correct and the
 * Client gets the Secretaries PhoneNumber 
 * @author Nikolas+Nico
 *
 */
public class SecretaryPhoneNumberService extends Service {
	private String phoneNumber = "1234567890";

	public SecretaryPhoneNumberService(int _id, String _name) {
		super(_id, _name);
	}

	/**
	 * Returns the Phonnumber if no arguments are given
	 */
	@Override
	public byte[] getAnswer(String payload) {
		String[] payloadArray = this.getArgumentsArray(payload);
		String msg = "";
		// no arguments needed!
		if (payloadArray.length == 0) {
			msg = phoneNumber;
		} else {
			// If i get Arguments, Sth is wrong
			msg = "Error;Wrong Arguments";
		}
		// Dont forget the msg for the view :)
		debugMsg = msg;
		return msg.getBytes();
	}

}
