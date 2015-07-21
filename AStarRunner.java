import world.Robot;
import world.World;

public class AStarRunner extends Robot {

	@Override
	public void travelToDestination() {

	}

	public static void main(String args[]) {
		try {
			World myWorld = new World("input1.txt", false);
			AStarRunner myRobot = new AStarRunner();
			myRobot.addToWorld(myWorld);
			myRobot.travelToDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
