
public class MyBoard implements Board{
	
	private Stone[][] stoneStatus = new Stone[19][19];
	
	private int moveNumber;
	private int redCaptures;
	private int yellowCaptures;
	private Stone winner;
			
	public MyBoard() {	//initialize this board
		moveNumber = 0;
		redCaptures = 0;
		yellowCaptures = 0;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				stoneStatus[i][j] = Stone.EMPTY;
			}
		}
	}

	@Override
	public void placeStone(Stone s, Coordinate c) {
		if (moveNumber==2 && 7<=c.getRow() && c.getRow() <=11 && 7<=c.getColumn() && c.getColumn()<=11) {
			System.out.println("Second move should be 3-spot away from Center!");
			throw new IllegalArgumentException("Second move should be 3-spot away from Center!");
		}
		
		if (!isOutOfBounds(c) && isEmpty(c)) {	//not out of bounds AND empty, then put it
			stoneStatus[c.getRow()][c.getColumn()] = s;
			moveNumber++;
			//write Captures here!!!
			checkCapture(c);		//should check Capture according to current placing coordinate c
		} else {
			System.out.println(c.getRow() + "," + c.getColumn());
			System.out.println(moveNumber);
			throw new IllegalArgumentException("Illegal Coordinate Haha!");

		}
	}



	@Override
	public Stone pieceAt(Coordinate c) {
		return stoneStatus[c.getRow()][c.getColumn()];
	}

	@Override
	public boolean isOutOfBounds(Coordinate c) {	//should be 0-18
		if (c.getRow() > 18 || c.getRow() < 0 || c.getColumn() > 18 || c.getColumn() < 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean isEmpty(Coordinate c) {
		if (stoneStatus[c.getRow()][c.getColumn()] == Stone.EMPTY)
			return true;
		else
			return false;
	}

	@Override
	public int getMoveNumber() {
		return moveNumber;
	}

	@Override
	public int getRedCaptures() {
		return redCaptures;
	}

	@Override
	public int getYellowCaptures() {
		return yellowCaptures;
	}

	@Override
	public boolean gameOver() {		//scan and determine!!  If gameOver == true or not ?  (cannot pass in stone color??)
		if (redCaptures >= 5) {
			winner = Stone.RED;
			return true;
		} else if (yellowCaptures >=5){
			winner = Stone.YELLOW;
			return true;
		} else if (existFiveWin()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Stone getWinner() {
		// TODO Auto-generated method stub
		return winner;
	}
	
	private boolean existFiveWin() {
		//win in a row
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 15; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i][j+1] == Stone.RED && stoneStatus[i][j+2] == Stone.RED && stoneStatus[i][j+3] == Stone.RED && stoneStatus[i][j+4] == Stone.RED) {
					winner = Stone.RED;
					return true;
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i][j+1] == Stone.YELLOW && stoneStatus[i][j+2] == Stone.YELLOW && stoneStatus[i][j+3] == Stone.YELLOW && stoneStatus[i][j+4] == Stone.YELLOW) {
					winner = Stone.YELLOW;
					return true;
				} else {
					//do nothing
				}
			}
		}
		//win in a column
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 19; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i+1][j] == Stone.RED && stoneStatus[i+2][j] == Stone.RED && stoneStatus[i+3][j] == Stone.RED && stoneStatus[i+4][j] == Stone.RED) {
					winner = Stone.RED;
					return true;
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i+1][j] == Stone.YELLOW && stoneStatus[i+2][j] == Stone.YELLOW && stoneStatus[i+3][j] == Stone.YELLOW && stoneStatus[i+4][j] == Stone.YELLOW) {
					winner = Stone.YELLOW;
					return true;
				} else {
					//do nothing
				}
			}
		}
		//win in a diagonal angle
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i+1][j+1] == Stone.RED && stoneStatus[i+2][j+2] == Stone.RED && stoneStatus[i+3][j+3] == Stone.RED && stoneStatus[i+4][j+4] == Stone.RED) {
					winner = Stone.RED;
					return true;
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i+1][j+1] == Stone.YELLOW && stoneStatus[i+2][j+2] == Stone.YELLOW && stoneStatus[i+3][j+3] == Stone.YELLOW && stoneStatus[i+4][j+4] == Stone.YELLOW) {
					winner = Stone.YELLOW;
					return true;
				} else {
					//do nothing
				}
			}
		}
		//win in another diagonal angle
		for (int i = 4; i < 19; i++) {
			for (int j = 0; j < 15; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i-1][j+1] == Stone.RED && stoneStatus[i-2][j+2] == Stone.RED && stoneStatus[i-3][j+3] == Stone.RED && stoneStatus[i-4][j+4] == Stone.RED) {
					winner = Stone.RED;
					return true;
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i-1][j+1] == Stone.YELLOW && stoneStatus[i-2][j+2] == Stone.YELLOW && stoneStatus[i-3][j+3] == Stone.YELLOW && stoneStatus[i-4][j+4] == Stone.YELLOW) {
					winner = Stone.YELLOW;
					return true;
				} else {
					//do nothing
				}
			}
		}
		
		return false;
	}
	
	private void checkCapture(Coordinate c) {
		//capture in a row
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 16; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i][j+1] == Stone.YELLOW && stoneStatus[i][j+2] == Stone.YELLOW && stoneStatus[i][j+3] == Stone.RED) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i && c.getColumn()==j+3 ) {
						stoneStatus[i][j+1] = Stone.EMPTY;
						stoneStatus[i][j+2] = Stone.EMPTY;
						redCaptures++;
					}
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i][j+1] == Stone.RED && stoneStatus[i][j+2] == Stone.RED && stoneStatus[i][j+3] == Stone.YELLOW) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i && c.getColumn()==j+3 ) {
					stoneStatus[i][j+1] = Stone.EMPTY;
					stoneStatus[i][j+2] = Stone.EMPTY;
					yellowCaptures++;
					}
				} else {
					//do nothing
				}
			}
		}
		//capture in a column
		for (int i = 0; i < 16; i++) {		//15-18
			for (int j = 0; j < 19; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i+1][j] == Stone.YELLOW && stoneStatus[i+2][j] == Stone.YELLOW && stoneStatus[i+3][j] == Stone.RED) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i+3 && c.getColumn()==j ) {
					stoneStatus[i+1][j] = Stone.EMPTY;
					stoneStatus[i+2][j] = Stone.EMPTY;
					redCaptures++;
					}
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i+1][j] == Stone.RED && stoneStatus[i+2][j] == Stone.RED && stoneStatus[i+3][j] == Stone.YELLOW) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i+3 && c.getColumn()==j ) {
					stoneStatus[i+1][j] = Stone.EMPTY;
					stoneStatus[i+2][j] = Stone.EMPTY;
					yellowCaptures++;
					}
				} else {
					//do nothing
				}
			}
		}
		//capture in a diagonal angle
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i+1][j+1] == Stone.YELLOW && stoneStatus[i+2][j+2] == Stone.YELLOW && stoneStatus[i+3][j+3] == Stone.RED) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i+3 && c.getColumn()==j+3 ) {
					stoneStatus[i+1][j+1] = Stone.EMPTY;
					stoneStatus[i+2][j+2] = Stone.EMPTY;
					redCaptures++;
					}
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i+1][j+1] == Stone.RED && stoneStatus[i+2][j+2] == Stone.RED && stoneStatus[i+3][j+3] == Stone.YELLOW) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i+3 && c.getColumn()==j+3 ) {
					stoneStatus[i+1][j+1] = Stone.EMPTY;
					stoneStatus[i+2][j+2] = Stone.EMPTY;
					yellowCaptures++;
					}
				} else {
					//do nothing
				}
			}
		}
		//capture in another diagonal angle
		for (int i = 3; i < 19; i++) {
			for (int j = 0; j < 16; j++) {
				if (stoneStatus[i][j] == Stone.RED && stoneStatus[i-1][j+1] == Stone.YELLOW && stoneStatus[i-2][j+2] == Stone.YELLOW && stoneStatus[i-3][j+3] == Stone.RED) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i-3 && c.getColumn()==j+3 ) {
					stoneStatus[i-1][j+1] = Stone.EMPTY;
					stoneStatus[i-2][j+2] = Stone.EMPTY;
					redCaptures++;
					}
				} else if (stoneStatus[i][j] == Stone.YELLOW && stoneStatus[i-1][j+1] == Stone.RED && stoneStatus[i-2][j+2] == Stone.RED && stoneStatus[i-3][j+3] == Stone.YELLOW) {
					if (c.getRow()==i && c.getColumn()==j || c.getRow()==i-3 && c.getColumn()==j+3 ) {
					stoneStatus[i-1][j+1] = Stone.EMPTY;
					stoneStatus[i-2][j+2] = Stone.EMPTY;
					yellowCaptures++;
					}
				} else {
					//do nothing
				}
			}
		}

	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (stoneStatus[i][j] == Stone.EMPTY)
					sb.append("[ ]");
				else if (stoneStatus[i][j] == Stone.RED)
					sb.append("[O]");
				else if (stoneStatus[i][j] == Stone.YELLOW)
					sb.append("[X]");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
