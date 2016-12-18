package Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;

public class PictureService extends Service {

	public PictureService(int _id, String _name) {
		super(_id, _name);
	}

	@Override
	public byte[] getAnswer(String payload) {
		String[] args = this.getArgumentsArray(payload);
		byte[] line;
		byte[] picture;
		try {
			BufferedReader br = new BufferedReader(new FileReader("lol.png"));
			while ((line = br.readLine().getBytes()) != null) {
				System.out.println("LoL");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	

}
