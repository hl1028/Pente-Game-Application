
public class MyCoordinate implements Coordinate {
	
	private int row;
	private int column;
	
	public MyCoordinate(int r, int c) {
		if ( r < 0 || r > 18 || c < 0 || c > 18)
			throw new IllegalArgumentException("Invalid parameter");
		row = r;
		column = c;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		// TODO Auto-generated method stub
		return column;
	}
	
	public String toString() {
		return Integer.toString(row) + "," + Integer.toString(column);
	}

}
