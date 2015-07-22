import java.util.Comparator;

public class TileComparator implements Comparator<Tile> {

	@Override
	public int compare(Tile a, Tile b) {
		double aScore = (a.getDistFromStart() + a.getDistToEnd())*a.getCertainty();
		double bScore = (b.getDistFromStart() + b.getDistToEnd())*b.getCertainty();
		return aScore < bScore ? -1 : aScore == bScore ? 0 : 1;
	}

}
