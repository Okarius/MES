import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import Services.QuizService;

/**
 * 
 * @author Nico
 *
 */

public class QuizTest {

	QuizService qS = new QuizService(0, "quiz");

	@Test
	public void newGameTest() {

		byte[] msg = qS.getAnswer("102;3");
		assertArrayEquals("1".getBytes(), msg);
	}

	@Test
	public void QRCodeTest(){
		byte[] msg = qS.getAnswer("102;5");

	}
	@Test
	public void nextQuestionTest() {

		byte[] msg = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 1+1?;1;2;3;4".getBytes(), msg);

		// send answer
		byte[] msg00 = qS.getAnswer("102;0;B");
		assertArrayEquals("B".getBytes(), msg00);

		byte[] msg2 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 2+2?;1;2;3;4".getBytes(), msg2);

		// send answer
		byte[] msg01 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(), msg01);

		byte[] msg3 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 3+3?;1;2;3;6".getBytes(), msg3);

		// send answer
		byte[] msg02 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(), msg02);

		byte[] msg4 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 4+4?;1;2;3;8".getBytes(), msg4);

		// send answer
		byte[] msg03 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(), msg03);

		byte[] msg5 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 5+5?;1;2;3;10".getBytes(), msg5);

		// send answer
		byte[] msg04 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(), msg04);

		// 5 correct answers, highscore = 5
		assertEquals(5, qS.highscore);

	}

	@Test
	public void answerTest() {

		// reset question id
		byte[] msg0 = qS.getAnswer("102;3");
		assertArrayEquals(msg0, "1".getBytes());

		// Ask question
		byte[] msg = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 1+1?;1;2;3;4".getBytes(), msg);

		// send answer
		byte[] msg2 = qS.getAnswer("102;0;B");
		assertArrayEquals("B".getBytes(), msg2);

		byte[] msg3 = qS.getAnswer("102;0");
		assertArrayEquals("No answer".getBytes(), msg3);

	}

	@Test
	public void endgameTest() {
		String name = "harald";
		byte[] msg = qS.getAnswer("102;1;" + name);
		String answer = "Niki" + ":" + "6" + "&" + "Nico" + ":" + "5" + "&" + "Mihael" + ":" + "5" + "&" + "Lukas"
				+ ":" + "5" + "&" + "Petra" + ":" + "5" + "&" + "Christoph" + ":" + "4" + "&" + "Fayez" + ":" + "4"
				+ "&" + "Ridah" + ":" + "4" + "&" + "Yves" + ":" + "4" + "&" + "Wolfgang" + ":" + "3" + "&"
				+ "harald" + ":" + "0" + "&";

		assertArrayEquals(answer.getBytes(), msg);

		String name2 = "";
		byte[] msg2 = qS.getAnswer("102;1;" + name2);
		assertArrayEquals("No username".getBytes(), msg2);

	}



}
