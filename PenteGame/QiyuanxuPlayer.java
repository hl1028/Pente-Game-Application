import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QiyuanxuPlayer implements Player {

    private static final int[][] DIRECTIONS = new int[][] {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
    };
    private Stone stone;
    private Stone opponent;
    private int moveCount;

    public QiyuanxuPlayer(Stone stone) {
        this.stone = stone;
        this.opponent = stone == Stone.RED ? Stone.YELLOW : Stone.RED;
        this.moveCount = 0;
    }

    /**
     * This method returns a coordinate that the player wishes to place a piece yet.
     * <p>
     * A human player should be prompted to enter and row and column number.
     * <p>
     * A computer player should somehow determine where to place a stone.
     * <p>
     * THIS METHOD MUST NOT CALL b.placeStone AT ANY POINT. This method should not
     * modify board in any way! If it does, you will lose points!
     *
     * @param b - board object which keep track of the state of the game
     * @return the coordination that the player want to move
     */
    @Override
    public Coordinate getMove(Board b) {
        // 1 check whether there are already a move to make a 5 row
        // 2 check whether opponent has a 4 row or 3 row
        // 3 check whether there is a move to remove pair
        // 4 check whether there is a move to create unlocked 4 row
        // 5 check whether there is a move to create unlocked 3 row
        if (stone == Stone.RED && moveCount == 0) {
            moveCount++;
            return new MyCoordinate(9, 9);
        }
        if (stone == Stone.RED && moveCount == 1) {
            moveCount++;
            if (b.pieceAt(new MyCoordinate(12, 12)) == Stone.EMPTY) {
                return new MyCoordinate(12, 12);
            } else {
                return new MyCoordinate(6, 6);
            }
        }
        int[][] stratMap = CreateMap(b);
        printMap(stratMap);
        int max = -1;
        List<Coordinate> pool = new ArrayList<>();
        for (int i = 0; i < stratMap.length; i++) {
            for (int j = 0; j < stratMap[0].length; j++) {
                if (stratMap[i][j] > max) {
                    max = stratMap[i][j];
                    pool.clear();
                    pool.add(new MyCoordinate(i, j));
                } else if (stratMap[i][j] == max) {
                    pool.add(new MyCoordinate(i, j));
                }
            }
        }
        if (max == -1) {
            System.out.print("The board is FULL!");
        }
        Random random = new Random();
        int ran = random.nextInt(pool.size());
        return pool.get(ran);
    }

    private int[][] CreateMap(Board b) {

        int[][] stratMap = new int[19][19];

        Stone[][] board = new Stone[19][19];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = b.pieceAt(new MyCoordinate(i, j));
                if (board[i][j] != Stone.EMPTY) {
                    stratMap[i][j] = -1;
                } else if (i >= 7 && i <= 11 && j >= 7 && j <= 11) {
                    stratMap[i][j] = 5;
                }
            }
        }

        for (int i = 0; i < stratMap.length; i++) {
            for (int j = 0; j < stratMap.length; j++) {
                if (stratMap[i][j] < 0) continue;
                for (int k = 0; k < 4; k++) {
                    int[] d = DIRECTIONS[k];
                    int row = i + d[0];
                    int col = j + d[1];
                    int count = 0;
                    // end with opponent stone 1, boundary 0, Empty 2
                    Stone tmp1 = stone;
                    Stone tmp2 = stone;
                    int endWith1 = 0;
                    int endWith2 = 0;

                    while (row >= 0 && col >= 0 && row <= 18 && col <= 18 && board[row][col] == stone) {
                        count++;
                        row += d[0];
                        col += d[1];
                        if (row >= 0 && col >= 0 && row <= 18 && col <= 18 ) {
                            tmp1 = board[row][col];
                        } else {
                            tmp1 = null;
                        }
                    }
                    if (tmp1 == stone) {
                        endWith1 = 0;
                    } else if (tmp1 == Stone.EMPTY) {
                        endWith1 = 2;
                    } else {
                        endWith1 = 1;
                    }

                    row = i - d[0];
                    col = j - d[1];
                    while (row >= 0 && col >= 0 && row <= 18 && col <= 18 && board[row][col] == stone) {
                        count++;
                        row -= d[0];
                        col -= d[1];
                        if (row >= 0 && col >= 0 && row <= 18 && col <= 18 ) {
                            tmp2 = board[row][col];
                        } else {
                            tmp2 = null;
                        }
                    }
                    if (tmp2 == stone) {
                        endWith2 = 0;
                    } else if (tmp2 == Stone.EMPTY) {
                        endWith2 = 2;
                    } else {
                        endWith2 = 1;
                    }

                    if (count >= 4) {
                        // Win right now
                        stratMap[i][j] += 100000;
                    } else if (count == 3 && endWith1 + endWith2 == 4) {
                        // Win next turn
                        System.out.println("FOund");
                        stratMap[i][j] += 5000;
                    } else if (count == 3 && endWith1 + endWith2 == 3) {
                        // Good option
                        stratMap[i][j] += 2000;
                    } else if (count == 2 && endWith1 + endWith2 == 4) {
                        stratMap[i][j] += 800;
                    } else if (count == 2) {
                        stratMap[i][j] += 500;
                    } else if (count == 3) {
                        stratMap[i][j] += 100;
                    } else if (count == 1) {
                        stratMap[i][j] += 2;
                    } else if (count > 0) {
                        stratMap[i][j] += 10;
                    } else {
                        stratMap[i][j] += 1;
                    }
                }

                for (int[] d: DIRECTIONS) {
                    int row = i + d[0];
                    int col = j + d[1];
                    int count = 0;
                    Stone tmp = stone;

                    int endWith = 0;
                    while (row >= 0 && col >= 0 && row <= 18 && col <= 18 && board[row][col] == opponent) {
                        count++;
                        row += d[0];
                        col += d[1];
                        if (row >= 0 && col >= 0 && row <= 18 && col <= 18 ) {
                            tmp = board[row][col];
                        } else {
                            tmp = null;
                        }
                    }
                    if (tmp == stone) {
                        endWith = 2;
                    } else if (tmp == Stone.EMPTY) {
                        endWith = 1;
                    } else {
                        endWith = 0;
                    }

                    if (count == 2 && endWith == 2) {
                        // Kill a pair
                        stratMap[i][j] += 3000;
                    } else if (count == 4) {
                        // opponent to win
                        stratMap[i][j] += 900000;
                    } else if (count == 3 && endWith == 1) {
                        stratMap[i][j] += 100000;
                    } else {
                        stratMap[i][j] += 0;
                    }
                }

                for (int k = 0; k < 4; k++) {
                    int[] d = DIRECTIONS[k];
                    int row = i + d[0];
                    int col = j + d[1];
                    int count = 0;
                    // end with opponent stone 1, boundary 0, Empty 2
                    Stone tmp1 = stone;
                    Stone tmp2 = stone;
                    int endWith1 = 0;
                    int endWith2 = 0;

                    while (row >= 0 && col >= 0 && row <= 18 && col <= 18 && board[row][col] == opponent) {
                        count++;
                        row += d[0];
                        col += d[1];
                        if (row >= 0 && col >= 0 && row <= 18 && col <= 18 ) {
                            tmp1 = board[row][col];
                        } else {
                            tmp1 = null;
                        }
                    }
                    if (tmp1 == stone) {
                        endWith1 = 0;
                    } else if (tmp1 == Stone.EMPTY) {
                        endWith1 = 2;
                    } else {
                        endWith1 = 1;
                    }

                    row = i - d[0];
                    col = j - d[1];
                    while (row >= 0 && col >= 0 && row <= 18 && col <= 18 && board[row][col] == opponent) {
                        count++;
                        row -= d[0];
                        col -= d[1];
                        if (row >= 0 && col >= 0 && row <= 18 && col <= 18 ) {
                            tmp2 = board[row][col];
                        } else {
                            tmp2 = null;
                        }
                    }
                    if (tmp2 == stone) {
                        endWith2 = 0;
                    } else if (tmp2 == Stone.EMPTY) {
                        endWith2 = 2;
                    } else {
                        endWith2 = 1;
                    }

                    if (count == 3 && endWith2 + endWith1 == 4) {
                        stratMap[i][j] += 100000;
                    } else if (count == 4 && endWith1 + endWith2 == 4) {
                        stratMap[i][j] += 100000;
                    }
                }
            }

        }
        return stratMap;
    }

    private void printMap(int[][] stratMap) {
        for (int i = 0; i < stratMap.length; i++) {
            for (int j = 0; j < stratMap[0].length; j++) {
                System.out.print("[" + stratMap[i][j] +"]");
            }
            System.out.print("\n");
        }
    }
    /**
     * When a player is created, it should be given either Stone.RED or Stone.YELLOW
     *
     * @return Stone type
     */
    @Override
    public Stone getStone() {
        return stone;
    }
}
