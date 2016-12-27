import static org.junit.Assert.*;

import org.junit.Test;

import Services.QuizHandler;
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
		assertArrayEquals("1".getBytes(),msg);	
	}
	
	@Test
	public void nextQuestionTest() {
		
		byte[] msg = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 1+1?;1;2;3;4".getBytes(),msg);
		
		//send answer
		byte[] msg00 = qS.getAnswer("102;0;B");
		assertArrayEquals("B".getBytes(),msg00);
		
		byte[] msg2 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 2+2?;1;2;3;4".getBytes(),msg2);
		
		//send answer
		byte[] msg01 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(),msg01);
		
		byte[] msg3 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 3+3?;1;2;3;6".getBytes(),msg3);
		
		//send answer
		byte[] msg02 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(),msg02);
				
		
		byte[] msg4 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 4+4?;1;2;3;8".getBytes(),msg4);
		
		//send answer
		byte[] msg03 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(),msg03);
				
		
		byte[] msg5 = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 5+5?;1;2;3;10".getBytes(),msg5);
		
		//send answer
		byte[] msg04 = qS.getAnswer("102;0;D");
		assertArrayEquals("D".getBytes(),msg04);
		
		//5 correct answers, highscore = 5
		assertEquals(5, qS.highscore);
				
	}
	
	@Test
	public void answerTest() {
		
		//reset question id
		byte[] msg0 = qS.getAnswer("102;3");
		assertArrayEquals(msg0,"1".getBytes());	
		
		//Ask question
		byte[] msg = qS.getAnswer("102;4");
		assertArrayEquals("Was ist 1+1?;1;2;3;4".getBytes(),msg);
		
		//send answer
		byte[] msg2 = qS.getAnswer("102;0;B");
		assertArrayEquals("B".getBytes(),msg2);	
		
		byte[] msg3 = qS.getAnswer("102;0");
		assertArrayEquals("No answer".getBytes(),msg3);	
		
		
	}
		
	@Test
	public void endgameTest() {
		String name = "harald";
		byte[] msg = qS.getAnswer("102;1;"+name);
		
		String answer = "Niki" +":" + "6" +"\n" + 
				"Nico" + ":" + "5" + "\n"+
				"Mihael" + ":" + "5" + "\n"+
				"Lukas" + ":" + "5" + "\n" +
				"Petra" + ":" + "5" + "\n" +
				"Christoph" + ":" + "4" + "\n" +
				"Fayez" + ":" + "4" + "\n" +
				"Ridah" + ":" + "4" + "\n" +
				"Yves" + ":" + "4" + "\n" +
				"Wolfgang" + ":" + "3" + "\n" +
				"harald" + ":" + "0" + "\n";
		
		assertArrayEquals(answer.getBytes(),msg);
		
		String name2 = "";
		byte[] msg2 = qS.getAnswer("102;1;"+name2);
		assertArrayEquals("No username".getBytes(),msg2);
		
	}
	
	@Test
	public void getHighscoreTest() {
			
		byte[] msg = qS.getAnswer("102;2;");
		assertArrayEquals("6".getBytes(),msg);
		
	}
	
	
}
