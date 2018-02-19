import java.util.Scanner;

public class Game {

	private static MyBoard b;
	private static Player p1;
	private static Player p2;
	/**
	 * constructor, new MyBoard
	 */
	public Game() {
		b = new MyBoard();
	}
	
	public static void main(String[] args) {
		Game game1 = new Game();
		System.out.println("Do you want to go first as RED? (y/n)");
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		/**
		 * ask user if he/she wants to go first
		 */
		if (input.equalsIgnoreCase("y")){
			p1 = new HumanPlayer(Stone.RED);
			p2 = new harry28Player(Stone.YELLOW);
			//p2 = new HumanPlayer(Stone.YELLOW);	//test 2 human
		} else {
			p1 = new harry28Player(Stone.RED);
			p2 = new HumanPlayer(Stone.YELLOW);
		}
		
		Player playerNow = p1;
		
		while (true) {
			
			while(true) {
				try {
					b.placeStone(playerNow.getStone(), playerNow.getMove(b));
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();	//if caught, still in this inner loop
				}
			}
			System.out.println(b);
			System.out.println("red captures: " + b.getRedCaptures());
			System.out.println("yellow captures: " + b.getYellowCaptures());
			/**
			 * check if gameover or not
			 */
			if (b.gameOver()) {
				System.out.println("Game Over!");
				if (b.getWinner() == Stone.RED)
					System.out.println("Red Won!");
				else 
					System.out.println("Yellow Won!");
				break;	//break the outer loop
			}
			/**
			 * switch the player
			 */
			if (playerNow == p1)
				playerNow = p2;
			else
				playerNow = p1;	
		}
	}
	

}
