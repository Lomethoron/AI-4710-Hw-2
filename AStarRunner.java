import java.awt.Point;
import world.Robot;
import world.World;
import java.util.*;

public class AStarRunner extends Robot {

	List<List<Tile>> localMap;
	int yEndPos, xEndPos;
	Stack<Point> moves = new Stack<Point>();

	@Override
	public void travelToDestination() {
		//super.move( new Point(1,1));
		buildLocalMap();
		findHeuristic();
		Tile finalTile = findPath();
		executePath(finalTile);
		System.out.println("("+super.getX()+","+ super.getY()+")");
	}

	private void findHeuristic() {
		for (List<Tile> row : localMap) {
			for (Tile tile : row) {
				// pythag
				tile.setDistToEnd(Math.sqrt(
						Math.abs(Math.pow((tile.getyPos() - yEndPos), 2) + Math.pow((tile.getxPos() - xEndPos), 2))));
			}
		}

	}

	private void executePath(Tile finalTile) {
		createPath(finalTile);
		while (!moves.empty()) {
			Point nextMove = moves.pop();
			//System.out.println(nextMove.toString());
			super.move(nextMove);
		}

	}

	private void createPath(Tile tile) {
		moves.push(new Point(tile.getxPos(), tile.getyPos()));
		if (tile.isStart()) {
			return;
		}
		 //System.out.println("We're pushing ("+tile.getxPos()+","+tile.getyPos()+")");
		 //System.out.println("We're pushing (" + (tile.getxPos()+1) + "," + (char)(tile.getyPos()+65)+")");
		createPath(localMap.get(tile.getyPrev()).get(tile.getxPrev()));

	}

	private Tile findPath() {
		PriorityQueue<Tile> openList = new PriorityQueue<Tile>(1, new TileComparator());
		int xPos = super.getX();
		int yPos = super.getY();
		int xMin = 0;
		int yMin = 0;
		int xMax = localMap.get(0).size();
		int yMax = localMap.size();

		Tile startTile = localMap.get(yPos).get(xPos);
		startTile.setDistFromStart(0);

		openList.add(startTile);

		while (openList.size() > 0) {
			Tile curTile = openList.poll();
			int xTilePos = curTile.getxPos();
			int yTilePos = curTile.getyPos();
			//System.out.println("(" + (xTilePos) + "," + (yTilePos)+")");
			//System.out.println("(" + (xTilePos+1) + "," + (char)(yTilePos+65)+")");
			for (int yPossibleMove = yTilePos - 1; yPossibleMove <= yTilePos + 1; yPossibleMove++) {
				// stay in bounds
				if (yPossibleMove < yMin || yPossibleMove >= yMax) {
					continue;
				}
				for (int xPossibleMove = xTilePos - 1; xPossibleMove <= xTilePos + 1; xPossibleMove++) {
					// stay in bounds
					if (xPossibleMove < xMin || xPossibleMove >= xMax
							|| (xPossibleMove == xTilePos && yPossibleMove == yTilePos)) {
						continue;
					}
					//System.out.println("We're trying (" + (xPossibleMove+1) + "," + (char)(yPossibleMove+65)+")");
					Tile nextPossibleTile = localMap.get(yPossibleMove).get(xPossibleMove);
					if (nextPossibleTile.isEnd()) {
						nextPossibleTile.setxPrev(xTilePos);
						nextPossibleTile.setyPrev(yTilePos);
						//System.out.println("Curr ("+xPossibleMove+","+yPossibleMove+") Prev ("+xTilePos+","+yTilePos+")");
						return nextPossibleTile;
					}
					if (!nextPossibleTile.isClosed() &&!nextPossibleTile.isOpen()&& nextPossibleTile.isPassable()) {
						nextPossibleTile.setDistFromStart(curTile.getDistFromStart() + 1);
						//System.out.println("Curr ("+xPossibleMove+","+yPossibleMove+") Prev ("+xTilePos+","+yTilePos+")");
						nextPossibleTile.setxPrev(xTilePos);
						nextPossibleTile.setyPrev(yTilePos);
						nextPossibleTile.setOpen(true);
						openList.add(nextPossibleTile);
						//System.out.println("We're trying (" + (xPossibleMove+1) + "," + (char)(yPossibleMove+65)+")");
					}

				}
			}
			curTile.setClosed(true);
		}
		throw new RuntimeException("Oh my, no path to the finish tile was found!");

	}

	private void buildLocalMap() {
		localMap = new ArrayList<List<Tile>>(0);
		int xPos = 0;
		int yPos = 0;
		while (true) { // y
			xPos = 0;
			String yPingResult = super.pingMap(new Point(xPos, yPos));
			if (yPingResult == null) {
				break;
			}
			localMap.add(new ArrayList<Tile>());
			while (true) {// x
				String xPingResult = super.pingMap(new Point(xPos, yPos));
				if (xPingResult == null) {
					break;
				}

				boolean isPassible = true;
				if (!xPingResult.equals("X")) {
					Tile tile = new Tile(isPassible, xPos, yPos);
					if (xPingResult.equals("S")) {
						tile.setStart(true);
					}
					if (xPingResult.equals("F")) {
						tile.setEnd(true);
						xEndPos = xPos;
						yEndPos = yPos;
					}
					localMap.get(yPos).add(tile);
				} else {
					localMap.get(yPos).add(new Tile(!isPassible, xPos, yPos));
				}
				xPos++;

			}
			yPos++;
		}
		// Unessecary pinging
		/*
		 * for(yPos = 0; super.pingMap(new Point(xPos,yPos)) != null; yPos++){
		 * for(xPos = 0; super.pingMap(new Point(xPos, yPos)) != null; xPos++){
		 * 
		 * } }
		 */

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
