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
		String fileName = "QRCode2";		
		printHighScore(payloadArray[0], fileName);
		PrintWorker printWorker = new PrintWorker(fileName);
		String msg = "Printed";
		debugMsg = "Printed";
		return msg.getBytes();
		
	}
	
	/**
	 * prints String to file as QR code 
	 * @param fileName 
	 * @param input: String to be printed
	 *
	 */
	
	private void printHighScore(String input, String fileName) {
		QRCode codeMaker = new QRCode();
		
		String filePath = fileName+".png";
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
