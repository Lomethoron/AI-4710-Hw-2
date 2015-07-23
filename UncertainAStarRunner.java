import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import world.Robot;
import world.World;

public class UncertainAStarRunner extends Robot {

	List<List<Tile>> localMap;
	int yEndPos, xEndPos, yStartPos, xStartPos, numCol, numRow;
	Stack<Point> moves = new Stack<Point>();

	@Override
	public void travelToDestination() {
		buildLocalMap();
		findHeuristic();
		while ((super.getX() != xEndPos) || (super.getY() != yEndPos)) {
			System.out.println("Start loop");
			resetTiles();
			pingEntireMap();
			//Tile startTile = localMap.get(super.getY()+5).get(super.getX()+5);
			//System.out.println("Tile reported ("+startTile.getxPos()+","+startTile.getyPos()+")");
			
			Tile finalTile = findPath();
			executePath(finalTile);
			// System.out.println("("+super.getX()+","+ super.getY()+")");
		}
		System.out.println((super.getX() != xEndPos) && (super.getY() != yEndPos));
		 System.out.println("This is a problem ("+(super.getX()+1)+","+ (char)(super.getY()+65)+")");
	}

	private void resetTiles() {
		moves = new Stack<Point>();
		for(int y = 0; y<numRow; y++){
			for(int x = 0; x<numCol; x++){
				Tile tile = localMap.get(y).get(x);
				if(tile.isClosed()){
					tile.setClosed(false);
				}
				if(tile.isOpen()) {
					tile.setOpen(false);
				}
				if(tile.getxPrev()!=-1) {
					tile.setxPrev(-1);
				}
				if(tile.getyPrev()!=-1){
					tile.setyPrev(-1);
				}
				tile.setDistFromStart(-1);
			}
		}
		
	}

	@Override
	public void addToWorld(World world) {
		Point endCoord = world.getEndPos();
		yEndPos = (int) endCoord.getY();
		xEndPos = (int) endCoord.getX();

		Point startCoord = world.getStartPos();
		yStartPos = (int) startCoord.getY();
		xStartPos = (int) startCoord.getX();

		// System.out.println(endCoord.toString());
		numCol = world.numCols();
		numRow = world.numRows();
		// System.out.println("numCol: "+numCol+" numRow: "+numRow);
		super.addToWorld(world);
		System.out.println("Robot at ("+super.getX()+","+super.getY()+")");
	}

	private void findHeuristic() {
		for (List<Tile> row : localMap) {
			for (Tile tile : row) {
				// pythag
				tile.setDistToEnd(Math.sqrt(Math.abs(Math.pow((tile.getyPos() - yEndPos), 2) + Math.pow((tile.getxPos() - xEndPos), 2))));
			}
		}

	}

	private void executePath(Tile finalTile) {
		createPath(finalTile);
		while (!moves.empty()) {
			int oldxPos = super.getX();
			int oldyPos = super.getY();
			//System.out.println("Were currently at ("+(oldxPos+1)+","+(char)(oldyPos+65)+")");
			Point nextMove = moves.pop();
			//System.out.println(nextMove.toString());
			super.move(nextMove);
			int newxPos = super.getX();
			int newyPos = super.getY();
			System.out.println("Were moving to ("+((int)nextMove.getX()+1)+","+(char)(nextMove.getY()+65)+") from("+(oldxPos+1)+","+(char)(oldyPos+65)+")");
			if((oldxPos==newxPos)&&(oldyPos==newyPos)){
				//if its a wall, never go here again
				Tile tile = localMap.get((int)nextMove.getY()).get((int)nextMove.getX());
				tile.setPassable(false);
				System.out.println("Ow, ("+(tile.getxPos()+1)+","+(char)(tile.getyPos()+65)+") is a wall.");
				return;
			}
		}
		

	}

	private void createPath(Tile tile) {
		if (tile.isStart()||tile.getyPrev()==-1) {//extra condition for determining starts that are not the initial start location
			return;
		}
		moves.push(new Point(tile.getxPos(), tile.getyPos()));// again with the matrix notation :/ Print statements are in readable form
		// System.out.println("We're pushing ("+tile.getxPos()+","+tile.getyPos()+")");
		 System.out.println("We're pushing (" + (tile.getxPos()+1) + "," +(char)(tile.getyPos()+65)+")");
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
		System.out.println("Start ("+(xPos+1)+","+(char)(yPos+65)+")");
		//System.out.println("Dim ("+xMax+","+yMax+")");

		Tile startTile = localMap.get(yPos).get(xPos);
		//System.out.println("Tile reported ("+startTile.getxPos()+1+","+(char)(startTile.getyPos()+65)+")");
		startTile.setDistFromStart(0);
		startTile.setLocalStart(true);

		openList.add(startTile);

		while (openList.size() > 0) {
			Tile curTile = openList.poll();
			int xTilePos = curTile.getxPos();
			int yTilePos = curTile.getyPos();
			 //System.out.println("(" + (xTilePos) + "," + (yTilePos)+")");
			 //System.out.println("(" + (xTilePos+1) + "," +(char)(yTilePos+65)+")");
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
					//System.out.println("We're trying (" + (xPossibleMove+1) +"," + (char)(yPossibleMove+65)+")");
					Tile nextPossibleTile = localMap.get(yPossibleMove).get(xPossibleMove);
					if (nextPossibleTile.isEnd()) {
						nextPossibleTile.setxPrev(xTilePos);
						nextPossibleTile.setyPrev(yTilePos);
						// System.out.println("Curr("+xPossibleMove+","+yPossibleMove+") Prev("+xTilePos+","+yTilePos+")");
						return nextPossibleTile;
					}
					if (!nextPossibleTile.isClosed() && !nextPossibleTile.isOpen() && nextPossibleTile.isPassable()) {
						nextPossibleTile.setDistFromStart(curTile.getDistFromStart() + 1);
						 //System.out.println("Curr("+xPossibleMove+","+yPossibleMove+") Prev("+xTilePos+","+yTilePos+")");
						nextPossibleTile.setxPrev(xTilePos);
						nextPossibleTile.setyPrev(yTilePos);
						nextPossibleTile.setOpen(true);
						openList.add(nextPossibleTile);
						// System.out.println("We're trying (" +(xPossibleMove+1) + "," +(char)(yPossibleMove+65)+")");
					}

				}
			}
			curTile.setClosed(true);
		}
		throw new RuntimeException("Oh my, no path to the finish tile was found!");

	}

	/**
	 * Instantiates everything
	 */
	private void buildLocalMap() {
		localMap = new ArrayList<List<Tile>>(0);
		for (int yPos = 0; yPos < numRow; yPos++) { // y
			localMap.add(new ArrayList<Tile>());
			for (int xPos = 0; xPos < numCol; xPos++) {// x
				Tile tile = new Tile();
				tile.setxPos(xPos);
				tile.setyPos(yPos);
				//System.out.println("Creating Tile at ("+xPos+","+yPos+")");
				if(tile.getxPos()==xStartPos && tile.getyPos()==yStartPos){
					tile.setStart(true);
				}
				if(tile.getxPos()==xEndPos && tile.getyPos()==yEndPos){
					tile.setEnd(true);
				}
				localMap.get(yPos).add(tile);
			}
		}
	}

	/**
	 * Special case to perform a map wide sweep from current position
	 */

	private void pingEntireMap() {
		for (int yPos = 0; yPos < numRow; yPos++) {
			for (int xPos = 0; xPos < numCol; xPos++) {
				String tileReport = super.pingMap(new Point(yPos, xPos));
				Tile curTile = localMap.get(yPos).get(xPos);
				// if it says its a wall, don't go there easily
				if (tileReport.equals("X")) {
					curTile.setCertainty(.9);
					continue;
				}
				// otherwise go there
				curTile.setCertainty(.1);
				if(curTile.getxPos()==xStartPos && curTile.getyPos()==yStartPos){
					curTile.setCertainty(0);
				}
				if(curTile.getxPos()==xEndPos && curTile.getyPos()==yEndPos){
					curTile.setCertainty(0);
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			World myWorld = new World("input1.txt", true);
			UncertainAStarRunner myRobot = new UncertainAStarRunner();
			myRobot.addToWorld(myWorld);
			myRobot.travelToDestination();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
