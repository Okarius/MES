package Services;

/**
 * The PhoneNumberService allows usage of functions from PhoneNumberHandler.
 * 
 * @author nico
 *
 */


public class PhoneNumberService extends Service {

	private PhoneNumberHandler phoneNumberHandler;

	public PhoneNumberService(int _id, String _name) {
		super(_id, _name);
		phoneNumberHandler = new PhoneNumberHandler();

	}

	@Override
	public byte[] getAnswer(String payload) {
		String[] payloadArray = this.getArgumentsArray(payload);
		String msg = "";
		int numberOfArguments = payloadArray.length;
		switch (numberOfArguments) {
		case 0:
			msg = phoneNumberHandler.getAllNumbers();
			break;
		case 1:
			msg = phoneNumberHandler.deletePhoneNumber(payloadArray[0]);
			break;
		case 2:
			msg = phoneNumberHandler.addNewNumber(payloadArray[0], payloadArray[1]);
			break;
		default:
			msg = "Illegal arguments for Service PhoneNumber";
			break;
		}
		debugMsg = msg;
		return msg.getBytes();
	}

}
