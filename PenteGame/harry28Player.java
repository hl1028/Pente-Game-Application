import java.util.Arrays;
import java.util.Random;

/**
 * AI player by harry28@seas.upenn.edu, Zhao Li
 * @author lizhao
 *
 */
public class harry28Player implements Player {
	Stone myColor;
	Stone opponentColor;
	Coordinate[][] allCoordinate = new MyCoordinate[19][19];
	Coordinate testCoordinate;
	
	int[][] vectors = new int[][] {
		{0,1}, {1,0}, {0,-1}, {-1,0}, {1,1}, {-1,-1}, {1,-1}, {-1,1}
	};
	
	public harry28Player(Stone s) {
		myColor = s;
		if (s == Stone.RED) opponentColor = Stone.YELLOW;
		else opponentColor = Stone.RED;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				allCoordinate[i][j] = new MyCoordinate(i,j);
			}
		}
		testCoordinate = new MyCoordinate(9,9);
	}

	@Override
	public Coordinate getMove(Board b) {
		if (b.getMoveNumber() == 0) return new MyCoordinate(9,9);

		int row;
		int column;
		Random rand = new Random();
		if (b.getMoveNumber()==2 && myColor == Stone.RED) {
			// do things outside of 3x3
			while (true) {
			int index = rand.nextInt(8); //0-7
			row = 9 + 3*vectors[index][0];
			column = 9 + 3*vectors[index][1];
			if (b.isEmpty(allCoordinate[row][column]))
				return new MyCoordinate(row, column);
			else
				continue;
			}
		} else if (b.getMoveNumber()==1 && myColor == Stone.YELLOW){	//random stick to center
			int index = rand.nextInt(8); //0-7
			row = 9 + vectors[index][0];
			column = 9 + vectors[index][1];
			return new MyCoordinate(row, column);
		} else {
			Coordinate strategy = new MyCoordinate(9,9);	//initializing as center
			//just win with 5th step
			strategy = checkMyFour(b);
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			//final capture
			if (myColor == Stone.RED && b.getRedCaptures()==4 || myColor == Stone.YELLOW && b.getYellowCaptures() == 4) {
			strategy = checkCaptures(b);
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			}
			//block opponent's final step
			strategy = checkOpponentFour(b);	//if not block, 1 remaining
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			
			
			//attack with 2 remaining
			strategy = checkMyOpenThree(b);	//get 4 to win
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			strategy = checkCaptures(b);
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			
			strategy = checkOpponentThree(b);// *** or * **    if not block, 2 remaining
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			//triangle
			strategy = checkMyTriangle(b);	//get tri to win
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			strategy = checkMySuperTri(b);	//get tri to win
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			//opponent tri
			strategy = checkOpponentSuperTri(b);	//if not block, 3 remaining
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			strategy = checkTriangle(b);	//if not block, 3 remaining
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			//strategy = checkCaptures(b);
			//if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			
			strategy = dontGetCaptured(b);	
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;
			strategy = checkMyOpenTwo(b);	//2 into 3
			if (strategy.getRow()!=9 || strategy.getColumn()!=9) return strategy;

			strategy = randomNearCenter(b);
			return strategy;
		}

	}

	@Override
	public Stone getStone() {
		// TODO Auto-generated method stub
		return myColor;
	}
	
	private Coordinate checkOpponentFour(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == opponentColor) {	//if current spot is opponent
					for (int[] d: vectors) {							//check 4/8 vectors
						//System.out.println(d[0]+","+d[1]);
						if (0<=i+4*d[0] && i+4*d[0]<=18 && 0<=j+4*d[1] && j+4*d[1]<=18) {
							// OOOOX
							if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i+4*d[0], j+4*d[1]);
							}
							// OOOXO
							if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == opponentColor) {
								return new MyCoordinate(i+3*d[0], j+3*d[1]);
							}
							// OOxOO
							if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == opponentColor) {
								return new MyCoordinate(i+2*d[0], j+2*d[1]);
							}
							//if found return blockng spot
						}
					//not found, next direction
					}
				//not found, next spot
				}
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkMyFour(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == myColor) {	//if current spot is myColor
					for (int[] d: vectors) {							//check 4/8 vectors
						//System.out.println(d[0]+","+d[1]);
						if (0<=i+4*d[0] && i+4*d[0]<=18 && 0<=j+4*d[1] && j+4*d[1]<=18) {
							if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == myColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i+4*d[0], j+4*d[1]);
							}
							if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == myColor) {
								return new MyCoordinate(i+3*d[0], j+3*d[1]);
							}
							// OO*OO
							if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == myColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == myColor) {
								return new MyCoordinate(i+2*d[0], j+2*d[1]);
							}
							//if found return blockng spot
						}
					//not found, next direction
					}
				//not found, next spot
				}
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkOpponentThree(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == opponentColor) {	//if current spot is opponent
					for (int[] d: vectors) {							//check 4/8 vectors
						//System.out.println(d[0]+","+d[1]);
						//	*OOO*
						if (0<=i+3*d[0] && i+3*d[0]<=18 && 0<=j+3*d[1] && j+3*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == Stone.EMPTY) {
								if (canBeCaptured(allCoordinate[i-d[0]][j-d[1]], b))
									return allCoordinate[i+3*d[0]][j+3*d[1]];
								else 
									return allCoordinate[i-d[0]][j-d[1]];
							}
							//if found return blockng spot
						}
						// *O*OO*
						if (0<=i+4*d[0] && i+4*d[0]<=18 && 0<=j+4*d[1] && j+4*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i+d[0], j+d[1]);
							}
							//if found return blockng spot
						}
					//not found, next direction
					}
				//not found, next spot
				}
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkMyOpenThree(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == myColor) {	//if current spot is myColor
					for (int[] d: vectors) {							//check 4/8 vectors
						//System.out.println(d[0]+","+d[1]);
						if (0<=i+3*d[0] && i+3*d[0]<=18 && 0<=j+3*d[1] && j+3*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i-d[0], j-d[1]);
							}
							//if found return blockng spot
						}
						if (0<=i+4*d[0] && i+4*d[0]<=18 && 0<=j+4*d[1] && j+4*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == myColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i+d[0], j+d[1]);
							}
							//if found return blockng spot
						}
					//not found, next direction
					}
				//not found, next spot
				}
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkMyOpenTwo(Board b) {	//need to check toBeCaptured or not
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == myColor) {	//if current spot is myColor!
					for (int[] d: vectors) {							//check 4/8 vectors
						//System.out.println(d[0]+","+d[1]);
						/**
						 * O*O
						 */
									// to write!!
						/**
						 *  OO
						 */
						if (0<=i+2*d[0] && i+2*d[0]<=18 && 0<=j+2*d[1] && j+2*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i-d[0], j-d[1]);
							}
							//if found, attack!
						}
						/**	   V	
						 *     OXXO
						 */
						if (0<=i+4*d[0] && i+4*d[0]<=18 && 0<=j+4*d[1] && j+4*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == myColor && b.pieceAt(allCoordinate[i+4*d[0]][j+4*d[1]]) == Stone.EMPTY) {
								return new MyCoordinate(i+d[0], j+d[1]);
							}
							//if found return blockng spot
						}
					//not found, next direction
					}
				//not found, next spot
				}
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate dontGetCaptured(Board b) {	//	*OOX
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == myColor) {	//if current spot is myColor
					for (int[] d: vectors) {							//check 4/8 vectors
						//System.out.println(d[0]+","+d[1]);
						/**
						 *    *OOX
						 */
						if (0<=i+3*d[0] && i+3*d[0]<=18 && 0<=j+3*d[1] && j+3*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
							if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor) {
								return new MyCoordinate(i-d[0], j-d[1]);
							}
							//if found return blockng spot
						}
					//not found, next direction
					}
				//not found, next spot
				}
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkTriangle(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == Stone.EMPTY && 1<=i && i<=17 && 1<=j && j<=17) {	//if current spot is empty but surrounded
					/**O O
					 *  X
					 * O O
					 */
					if (b.pieceAt(allCoordinate[i-1][j-1]) == opponentColor && b.pieceAt(allCoordinate[i+1][j+1]) == opponentColor && b.pieceAt(allCoordinate[i-1][j+1]) == opponentColor && b.pieceAt(allCoordinate[i+1][j-1]) == opponentColor) {
						return new MyCoordinate(i,j);
					}
					/** O
					 * OXO
					 *  O
					 */
					if (b.pieceAt(allCoordinate[i+1][j]) == opponentColor && b.pieceAt(allCoordinate[i][j+1]) == opponentColor && b.pieceAt(allCoordinate[i-1][j]) == opponentColor && b.pieceAt(allCoordinate[i][j-1]) == opponentColor) {
						return new MyCoordinate(i,j);
					}
					
					/** OOX
					 *   O
					 *  O
					 */
					for (int[] d1: vectors) {
						for (int[] d2: vectors) {
							if (!Arrays.equals(d1,d2)) {
								if (inBound(i+3*d1[0]) && inBound(i+3*d2[0]) && inBound(j+3*d1[1]) && inBound(j+3*d2[1])) {
									if (b.pieceAt(allCoordinate[i+d1[0]][j+d1[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d1[0]][j+2*d1[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d1[0]][j+3*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d2[0]][j+d2[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d2[0]][j+2*d2[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d2[0]][j+3*d2[1]]) == Stone.EMPTY) {
										return new MyCoordinate(i,j);
									}
								}
							}
						}
					}
				}//if current spot is empty but threatened
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkOpponentSuperTri(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == Stone.EMPTY && 1<=i && i<=17 && 1<=j && j<=17) {	//if current spot is empty but surrounded			
					for (int[] d1: vectors) {
						for (int[] d2: vectors) {
							if (!Arrays.equals(d1,d2)) {
								/**   O
								 *    O
								 *  *OXO*	d1
								 *    *
								 *    
								 */
								if (inBound(i+2*d1[0]) && inBound(j+2*d1[1]) && inBound(i-2*d1[0]) && inBound(j-2*d1[1]) && inBound(i+3*d2[0]) && inBound(j+3*d2[1]) && inBound(i-d2[0]) && inBound(j-d2[1])) {
									if (b.pieceAt(allCoordinate[i+d1[0]][j+d1[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d1[0]][j+2*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d1[0]][j-d1[1]]) == opponentColor && b.pieceAt(allCoordinate[i-2*d1[0]][j-2*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d2[0]][j+d2[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d2[0]][j+2*d2[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d2[0]][j+3*d2[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d2[0]][j-d2[1]]) == Stone.EMPTY) {
										return new MyCoordinate(i,j);
									}
								}
								/**   O
								 * 	  O 
								 *  *OX*O*
								 *    *
								 */
								if (inBound(i+3*d1[0]) && inBound(j+3*d1[1]) && inBound(i-2*d1[0]) && inBound(j-2*d1[1]) && inBound(i+3*d2[0]) && inBound(j+3*d2[1]) && inBound(i-d2[0]) && inBound(j-d2[1])) {
									if (b.pieceAt(allCoordinate[i+d1[0]][j+d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+2*d1[0]][j+2*d1[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d1[0]][j+3*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d1[0]][j-d1[1]]) == opponentColor && b.pieceAt(allCoordinate[i-2*d1[0]][j-2*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d2[0]][j+d2[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d2[0]][j+2*d2[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d2[0]][j+3*d2[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d2[0]][j-d2[1]]) == Stone.EMPTY) {
										return new MyCoordinate(i,j);
									}
								}
							}
						}
					}
					
					
				}//if current spot is empty but threatened
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkMySuperTri(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == Stone.EMPTY && 1<=i && i<=17 && 1<=j && j<=17) {	//if current spot is empty but surrounded			
					for (int[] d1: vectors) {
						for (int[] d2: vectors) {
							if (!Arrays.equals(d1,d2)) {
								/**   O
								 *    O
								 *  *OXO*	d1
								 *    *
								 *    
								 */
								if (inBound(i+2*d1[0]) && inBound(j+2*d1[1]) && inBound(i-2*d1[0]) && inBound(j-2*d1[1]) && inBound(i+3*d2[0]) && inBound(j+3*d2[1]) && inBound(i-d2[0]) && inBound(j-d2[1])) {
									if (b.pieceAt(allCoordinate[i+d1[0]][j+d1[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d1[0]][j+2*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d1[0]][j-d1[1]]) == myColor && b.pieceAt(allCoordinate[i-2*d1[0]][j-2*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d2[0]][j+d2[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d2[0]][j+2*d2[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d2[0]][j+3*d2[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d2[0]][j-d2[1]]) == Stone.EMPTY) {
										return new MyCoordinate(i,j);
									}
								}
								/**   O
								 * 	  O 
								 *  *OX*O*
								 *    *
								 */
								if (inBound(i+3*d1[0]) && inBound(j+3*d1[1]) && inBound(i-2*d1[0]) && inBound(j-2*d1[1]) && inBound(i+3*d2[0]) && inBound(j+3*d2[1]) && inBound(i-d2[0]) && inBound(j-d2[1])) {
									if (b.pieceAt(allCoordinate[i+d1[0]][j+d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+2*d1[0]][j+2*d1[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d1[0]][j+3*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d1[0]][j-d1[1]]) == myColor && b.pieceAt(allCoordinate[i-2*d1[0]][j-2*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d2[0]][j+d2[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d2[0]][j+2*d2[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d2[0]][j+3*d2[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i-d2[0]][j-d2[1]]) == Stone.EMPTY) {
										return new MyCoordinate(i,j);
									}
								}
							}
						}
					}
					
					
				}//if current spot is empty but threatened
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkMyTriangle(Board b) {
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (b.pieceAt(allCoordinate[i][j]) == Stone.EMPTY && 1<=i && i<=17 && 1<=j && j<=17) {	//if current spot is empty but surrounded
					/**O O
					 *  X
					 * O O
					 */
					if (b.pieceAt(allCoordinate[i-1][j-1]) == myColor && b.pieceAt(allCoordinate[i+1][j+1]) == myColor && b.pieceAt(allCoordinate[i-1][j+1]) == myColor && b.pieceAt(allCoordinate[i+1][j-1]) == myColor) {
						return new MyCoordinate(i,j);
					}
					/** O
					 * OXO
					 *  O
					 */
					if (b.pieceAt(allCoordinate[i+1][j]) == myColor && b.pieceAt(allCoordinate[i][j+1]) == myColor && b.pieceAt(allCoordinate[i-1][j]) == myColor && b.pieceAt(allCoordinate[i][j-1]) == myColor) {
						return new MyCoordinate(i,j);
					}
					
					/** OOX
					 *   O
					 *  O
					 */
					for (int[] d1: vectors) {
						for (int[] d2: vectors) {
							if (!Arrays.equals(d1,d2)) {
								if (inBound(i+3*d1[0]) && inBound(i+3*d2[0]) && inBound(j+3*d1[1]) && inBound(j+3*d2[1])) {
									if (b.pieceAt(allCoordinate[i+d1[0]][j+d1[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d1[0]][j+2*d1[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d1[0]][j+3*d1[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d2[0]][j+d2[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d2[0]][j+2*d2[1]]) == myColor && b.pieceAt(allCoordinate[i+3*d2[0]][j+3*d2[1]]) == Stone.EMPTY) {
										return new MyCoordinate(i,j);
									}
								}
							}
						}
					}
				}//if current spot is empty but threatened
			}
		}//nothing found, return (9,9)
		return testCoordinate;
	}
	
	private Coordinate checkCaptures(Board b) {	//  *XXO
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				for (int[] d: vectors) {
					if (b.pieceAt(allCoordinate[i][j]) == myColor && inBound(i+3*d[0]) && inBound(j+3*d[1])) {
						if (b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor && b.pieceAt(allCoordinate[i+3*d[0]][j+3*d[1]]) == Stone.EMPTY) {
							return new MyCoordinate(i+3*d[0],j+3*d[1]);
						}
					}
				}
			}
		}
		return testCoordinate;
	}
	
	private Boolean inBound (int i) {
		if (0<=i && i<=18)
			return true;
		else
			return false;
	}
	
	private Coordinate randomNearCenter(Board b){
		Coordinate current = null;
		for (int i=1; i<=9; i++) {	//from 1 to 9
			for (int[] d : vectors) {	//8 directions
				int row = 9 + i*d[0];
				int column = 9 + i*d[1];
				current = allCoordinate[row][column];
				if (canBeCaptured(current, b)) {
					continue;
				}
				if (b.isEmpty(current))
					return current;	//if not full, success in some spot
				/**
				 * also, dont put in being captured
				 * **OX
				 */
			}
		}
		return current;
	}
	
	private Boolean canBeCaptured(Coordinate c, Board b) {	//**OX
		// **OX
		//  ^
		int i = c.getRow();
		int j = c.getColumn();
		if (b.pieceAt(allCoordinate[i][j]) == Stone.EMPTY) {	//if current spot is myColor
			for (int[] d: vectors) {							//check 4/8 vectors
				//System.out.println(d[0]+","+d[1]);
				if (0<=i+2*d[0] && i+2*d[0]<=18 && 0<=j+2*d[1] && j+2*d[1]<=18 && 0<=i-d[0] && i-d[0]<=18 && 0<=j-d[1] && j-d[1]<=18) {
					if (b.pieceAt(allCoordinate[i-d[0]][j-d[1]]) == Stone.EMPTY && b.pieceAt(allCoordinate[i+d[0]][j+d[1]]) == myColor && b.pieceAt(allCoordinate[i+2*d[0]][j+2*d[1]]) == opponentColor) {
						return true;
					}
					//if found return blockng spot
				}
			//not found, next direction
			}
		//not found, next spot
		}

		return false;
	}

}
