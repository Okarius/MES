package Services;

/**
 * 
 * @author Nico
 *
 */


public class QuizService extends Service {

	private QuizHandler quizHandler;
	private UserHandler userHandler;

	public QuizService(int _id, String _name) {
		super(_id, _name);
		quizHandler = new QuizHandler();

	}

	@Override
	public byte[] getAnswer(String payload) {

		String[] payloadArray = this.getArgumentsArray(payload);
		String msg = "";

		/*
		 * case 0: register new player case 1: request question by id case 2:
		 * incoming id and answer from client, send back if answer is correct or
		 * not
		 */

		switch (payloadArray[0]) {

		case "0":
			String answer = quizHandler.getAnswerById(payloadArray[0]);
			if (answer.equals(payloadArray[1])) {
				msg = "correct";
				userHandler.increaseHighscore(payloadArray[0]);
			} else {
				msg = "wrong";
			}
			break;

		case "1":
			msg = quizHandler.getQuestionById(payloadArray[0]);
			break;

		case "2":
			msg = userHandler.addPlayer(payloadArray[0]);
			break;
		case "3":
			msg = quizHandler.newGame();
			break;

		default:
			msg = "Illegal arguments for Service Quiz";
			break;

		}
		debugMsg = msg;
		return msg.getBytes();

	}

}
