import static org.junit.Assert.*;

import org.junit.Test;

import Services.QuizHandler;
import Services.QuizService;

public class QuizTest {

	@Test
	public void test() {
		QuizService qS = new QuizService(0, "quiz");
		QuizHandler qH = new QuizHandler();
		String firstMessage = "0;3";
		String firstAnswer = qS.getAnswer(firstMessage).toString();
		assert(firstAnswer.equals("lol"));
		
		
	}

}
