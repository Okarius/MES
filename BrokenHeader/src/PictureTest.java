import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import Services.PictureService;
import Services.QuizHandler;
import Services.QuizService;

/**
 * 
 * @author Nico
 *
 */

public class PictureTest {
	
	PictureService pS = new PictureService(0, "picture");

	
	@Test
	public void PictureServiceTest() {
	
		byte[] image = readImage();
		byte[] imageFromService = pS.getAnswer("103;0");
		
		assertNotNull(image);
		assertNotNull(imageFromService);
		assertArrayEquals(image , imageFromService);

	}
	
	public byte[] readImage() {
	
		byte[] imageInByte = null;
		try{
		
			BufferedImage originalImage = 
					ImageIO.read(new File("sand.jpg"));
				
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( originalImage, "jpg", baos );
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		
			}catch(IOException e){
				System.out.println(e.getMessage());
			}		
	
		return imageInByte;
}

}