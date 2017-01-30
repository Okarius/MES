package Services;

/**
 * PrintService can receive a String and print it as a QR Code to a sheet of paper
 * 
 * @author Nico
 *
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class PrintService extends Service {

	public PrintService(int _id, String _name) {
		super(_id, _name);

	}

	public byte[] getAnswer(String payload) {

		String[] payloadArray = this.getArgumentsArray(payload);
		PrintWorker printWorker = new PrintWorker();
		printWorker.printPayload(payloadArray[0]);
		String msg = "Printed";
		debugMsg = "Printed";
		return msg.getBytes();

	}

}
