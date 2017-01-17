package Services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * PictureService can read an image and send it back to the client.
 * 
 * @author Nico
 *
 */

public class PictureService extends Service {

	public PictureService(int _id, String _name) {
		super(_id, _name);
	}

	@Override
	public byte[] getAnswer(String payload) {
		byte[] msg = null;
		msg = readImage();
		debugMsg = "Image read";
		return msg;
	}

	public byte[] readImage() {

		byte[] imageInByte = null;
		try {

			BufferedImage originalImage = ImageIO.read(new File("sand.jpg"));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return imageInByte;
	}

}
