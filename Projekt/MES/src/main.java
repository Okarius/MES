import Model.Server;

public class main {
	public static void main(String[] args) {
		Server server = new Server();
		View view = new View();
		Controller controller = new Controller(server, view);
	}
}
