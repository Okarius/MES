
public class main {

	public static void main(String[] args) {
		ServerState state = new ServerState();
		Inquiry inquiry = new Inquiry();
		Controller controller = new Controller(state,inquiry);
	}

}
