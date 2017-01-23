/**
 * 
 * Source: http://www.java2s.com/Code/Java/2D-Graphics-GUI/PrintanImagetoprintdirectly.htm
 * @author : 
 */
package Services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

public class PrintWorker {
	public PrintWorker(String filePath) {
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));
		PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
		if (pss.length == 0)
			throw new RuntimeException("No printer services available.");
		PrintService ps = pss[0];
		DocPrintJob job = ps.createPrintJob();
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(filePath+".png");
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
}
