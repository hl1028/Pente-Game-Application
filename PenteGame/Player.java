
public interface Player {
	
	/**
	 * This method returns a coordinate that the player wishes to place a piece yet.
	 * 
	 * A human player should be prompted to enter and row and column number.
	 * 
	 * A computer player should somehow determine where to place a stone.
	 * 
	 * THIS METHOD MUST NOT CALL b.placeStone AT ANY POINT. This method should not
	 * modify board in any way! If it does, you will lose points!
	 * 
	 * @param b
	 * @return
	 */
	public Coordinate getMove(Board b);
	
	/**
	 * When a player is created, it should be given either Stone.RED or Stone.YELLOW
	 * @return
	 */
	public Stone getStone();
}
