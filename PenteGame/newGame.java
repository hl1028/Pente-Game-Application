

public class newGame {

	private static MyBoard b;
	private static Player p1;
	private static Player p2;
	private static Stone result;
	private static int harry28;
	private static int qiyuan;
	
	public static void main(String[] args) throws InterruptedException {
		String goFirst = "y";	//y is harry28 RED, n is qiyuan RED
		for (int i=0; i<1000; i++) {
			result = play(goFirst);
			if (result == Stone.RED && goFirst == "y") {
				harry28++;
			}
			if (result == Stone.YELLOW && goFirst == "n") {
				harry28++;
			}
			if (result == Stone.YELLOW && goFirst == "y") {
				qiyuan++;
			}
			if (result == Stone.RED && goFirst == "n") {
				qiyuan++;
			}
			if (goFirst=="y")
				goFirst = "n";
			else 
				goFirst = "y";
		}
		System.out.println("Final result:");
		System.out.println("harry28: " + harry28);
		System.out.println("qiyuan: " + qiyuan);
		//result = play(goFirst);
	}
	
	private static Stone play(String input) throws InterruptedException {
		b = new MyBoard();
		/*System.out.println("Do Zhao Li want to go first as RED? (y/n)");
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();*/
		
		if (input.equalsIgnoreCase("y")){
			p1 = new harry28Player(Stone.RED);
			p2 = new QiyuanxuPlayer(Stone.YELLOW);
			//p2 = new HumanPlayer(Stone.YELLOW);	//test 2 human
		} else {
			p1 = new QiyuanxuPlayer(Stone.RED);
			p2 = new harry28Player(Stone.YELLOW);
		}
		//harry28Player computer = new harry28Player(Stone.YELLOW);
		Player playerNow = p1;
		
		while (true) {
			
			while(true) {
				try {
					b.placeStone(playerNow.getStone(), playerNow.getMove(b));
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					//Thread.sleep(10000) ;
				}
			}
			
			System.out.println(b);
			System.out.println("red captures: " + b.getRedCaptures());
			System.out.println("yellow captures: " + b.getYellowCaptures());
			if (b.gameOver()) {
				System.out.println("Game Over!");
				if (b.getWinner() == Stone.RED)
					System.out.println("Red Won!");
				else 
					System.out.println("Yellow Won!");
				return b.getWinner();
				//break;
			}
			if (playerNow == p1)
				playerNow = p2;
			else
				playerNow = p1;
			/*	b.placeStone(p2.getStone(), p2.getMove(b));
				System.out.println(b);
				System.out.println("red captures: " + b.getRedCaptures());
				System.out.println("yellow captures: " + b.getYellowCaptures());
				if (b.gameOver()) {
					System.out.println("Game Over!");
					break;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}*/
			//Thread.sleep(1000) ;
		}
	}

}
