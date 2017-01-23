import static org.junit.Assert.*;

import org.junit.Test;

import Services.PhoneNumberService;
import Services.PhoneNumberHandler;

/**
 * 
 * @author Nico
 *
 */


public class PhoneNumberTest {

	PhoneNumberService pS = new PhoneNumberService(0, "phone");
	
	@Test
	public void addNumberTest() {
		
		byte[] msg = pS.getAnswer("101;Lukas;123456789");
		assertArrayEquals("NewNumber;3;Lukas;123456789".getBytes(), msg);
		
	}
}
