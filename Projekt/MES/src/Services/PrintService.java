package Services;

/**
 * PrintService can receive a String and print it as a QR Code to a file.
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
		
		printHighScore(payloadArray[0]);
		
		String msg = "Printed";
		
		return msg.getBytes();
		
	}
	
	/**
	 * prints String to file as QR code 
	 * @param input: String to be printed
	 *
	 */
	
	private void printHighScore(String input) {
		QRCode codeMaker = new QRCode();
		
		String filePath = "QRCode2.png";
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		try {
			codeMaker.createQRCode(input, filePath, charset, hintMap, 200, 200);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
