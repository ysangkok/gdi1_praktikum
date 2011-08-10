package testpackage.shared.ship;

public class AITester {

public AITester() {
	Class<? extends AI>[] competitors = new Class<? extends AI>[] {BadAI.class, GoodAI.class};

	Engine engine = new Engine();
	AI ai1 = competitors[0].getInstance();
	AI ai2 = competitors[1].getInstance();

	

}

public static void main(String[] args) {
	new AITester().run();
}

}
