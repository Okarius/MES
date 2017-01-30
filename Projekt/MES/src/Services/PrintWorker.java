/**
 * This class is used from The services.
 * It can be used to Create and Print QRCodes. 
 * To create QRCodes it uses its inner class QRCode
 * Source: http://www.java2s.com/Code/Java/2D-Graphics-GUI/PrintanImagetoprintdirectly.htm
 * @author Niki
 */
package Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class PrintWorker {
	public static String filePath = "PrintMe";

	private void print() {
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));
		PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
		if (pss.length == 0)
			throw new RuntimeException("No printer services available.");
		PrintService ps = pss[0];
		DocPrintJob job = ps.createPrintJob();
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(filePath + ".png");
			Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
			job.print(doc, pras);
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PrintException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function can get called from Services. It creates the QR-Code and
	 * initializes the printing
	 * 
	 * @param payload
	 *            this string will be the printed QRCode
	 */
	public void printPayload(String payload) {
		QRCode codeMaker = new QRCode();
		String qrCodeData = payload;
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		try {
			codeMaker.createQRCode(qrCodeData, filePath + ".png", charset, hintMap, 200, 200);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		print();
	}
}

/**
 * Inner Class to Create and read QRCodes
 * Source:
 * http://www.compiletimeerror.com/2014/11/create-quick-response-codeqr-code-in.html#.WI8nNfErJhE
 * 
 * @author niki
 *
 */
class QRCode {

	public void createQRCode(String qrCodeData, String filePath, String charset, Map hintMap, int qrCodeheight,
			int qrCodewidth) throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(
				new String(qrCodeData.replace("&", "\n").getBytes(charset), charset), BarcodeFormat.QR_CODE,
				qrCodewidth, qrCodeheight, hintMap);
		MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
	}

	public String readQRCode(String filePath, String charset, Map hintMap)
			throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
		return qrCodeResult.getText();
	}
}
