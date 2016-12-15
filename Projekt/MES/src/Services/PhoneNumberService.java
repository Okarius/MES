package Services;

import ServicesHandlers.PhoneNumberHandler;

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
		System.out.println("numberOFARG" + numberOfArguments);
		switch (numberOfArguments) {
		case 0:
			msg = phoneNumberHandler.getAllNumbers();
			break;
		case 1:
			System.out.println("FML");
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
