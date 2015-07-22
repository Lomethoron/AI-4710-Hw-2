
public class Tile {
	private boolean isPassable, isClosed, isStart, isEnd, isOpen;
	
	private double certainty, distToEnd;
	
	private int distOfLastPing, xPrev, yPrev, distFromStart, xPos, yPos;
	
	
	public Tile() {
		isPassable = false;
		isClosed = false;
		isStart = false;
		isEnd = false;
		isOpen = false;
		certainty = -1;
		distToEnd = -1;
		distOfLastPing = -1;
		xPrev = -1;
		yPrev = -1;
		distFromStart = -1;
		xPos = -1;
		yPos = -1;
		
	}
	public Tile(boolean isPassible, int xPos, int yPos) {
		this.isPassable = isPassible;
		this.xPos = xPos;
		this.yPos = yPos;
		isClosed = false;
		isStart = false;
		isEnd = false;
		isOpen = false;
		certainty = -1;
		distToEnd = -1;
		distOfLastPing = -1;
		xPrev = -1;
		yPrev = -1;
		distFromStart = -1;
		
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public double getDistToEnd() {
		return distToEnd;
	}
	public void setDistToEnd(double distToEnd) {
		this.distToEnd = distToEnd;
	}
	public boolean isStart() {
		return isStart;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public int getDistFromStart() {
		return distFromStart;
	}
	public void setDistFromStart(int distFromStart) {
		this.distFromStart = distFromStart;
	}
	public int getxPrev() {
		return xPrev;
	}
	public void setxPrev(int xPrev) {
		this.xPrev = xPrev;
	}
	public int getyPrev() {
		return yPrev;
	}
	public void setyPrev(int yPrev) {
		this.yPrev = yPrev;
	}
	public boolean isClosed() {
		return isClosed;
	}
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	
	public boolean isPassable() {
		return isPassable;
	}
	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}
	public double getCertainty() {
		return certainty;
	}
	public void setCertainty(double certainty) {
		this.certainty = certainty;
	}
	public int getDistOfLastPing() {
		return distOfLastPing;
	}
	public void setDistOfLastPing(int distOfLastPing) {
		this.distOfLastPing = distOfLastPing;
	}
}
