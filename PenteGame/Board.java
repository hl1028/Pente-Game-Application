
public interface Board {
	/**
	 * This function places a Stone S at coordinate C on the board.
	 * This function should not allow a piece to "overwrite" another
	 * piece. That is, if a coordinate on the board is not EMPTY, this
	 * function will not overwrite that piece and instead throw a 
	 * RuntimeException of some kind. Additionally throws a RuntimeException
	 * if c is out of bounds. Finally, it throws a RuntimeException if the move
	 * made is illegal (see the instructions on Tournament Rules).
	 * 
	 * @param s - the stone to be placed, either Stone.RED or Stone.YELLOW
	 * @param c - a coordinate of where on the board to place.
	 */
	public void placeStone(Stone s, Coordinate c);
	
	/**
	 * returns the stone at C on the board
	 * @param c - the coordinate you are getting the stone from
	 * @return the stone at the coordinate C
	 */
	public Stone pieceAt(Coordinate c);
	
	/**
	 * Returns true of the coordinate C is out of bounds (such as [-3,137])
	 * 
	 * 
	 * @param c - the coordinate
	 * @return true/false whether coordinate is out of bounds.
	 */
	public boolean isOutOfBounds(Coordinate c);
	
	/**
	 * Returns true if the space at coordinate is Stone.EMPTY. False otherwise
	 * @param c - the coordinate
	 * @return true/false if space is empty
	 */
	public boolean isEmpty(Coordinate c);
	
	/**
	 * Return the number of COMPLETED moves. I.e., if called before the first piece is placed,
	 * this should return 0.
	 * @return
	 */
	public int getMoveNumber();
	
	/**
	 * Get the number of times red has captures a pair of yellow pieces.
	 * @return
	 */
	public int getRedCaptures();
	
	/**
	 * Get the number of times yellow has captures a pair of red pieces.
	 * @return
	 */
	public int getYellowCaptures();
	
	/**
	 * Return true if the game has ended, by either playing getting 5 in a row
	 * or capturing 5 pairs.
	 * @return
	 */
	public boolean gameOver();
	
	/**
	 * Return the winner of the game (Stone.RED or Stone.YELLOW) if there is one.
	 * If there is no winner, throw a RuntimeException.
	 * @return
	 */
	public Stone getWinner();
	
	public String toString();
}
