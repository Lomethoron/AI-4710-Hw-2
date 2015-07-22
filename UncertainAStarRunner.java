import java.awt.Point;
import java.util.List;
import java.util.Stack;

import world.Robot;
import world.World;

public class UncertainAStarRunner extends Robot {
	
	List<List<Tile>> localMap;
	int yEndPos, xEndPos, yStartPos, xStartPos, numCol, numRow;
	Stack<Point> moves = new Stack<Point>();
	
	@Override
	public void travelToDestination(){
		
	}
	
	@Override
	public void addToWorld(World world){
		Point endCoord = world.getEndPos();
		yEndPos = (int) endCoord.getY();
		xEndPos = (int) endCoord.getX();
		
		Point startCoord = world.getStartPos();
		yStartPos = (int) startCoord.getY();
		xStartPos = (int) startCoord.getX();
		
		//System.out.println(endCoord.toString());
		numCol = world.numCols();
		numRow = world.numRows();
		//System.out.println("numCol: "+numCol+" numRow: "+numRow);
		super.addToWorld(world);
	}

	public static void main(String[] args) {
		try {
			World myWorld = new World("stupidMaze.txt", true);
			UncertainAStarRunner myRobot = new UncertainAStarRunner();
			myRobot.addToWorld(myWorld);
			myRobot.travelToDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
