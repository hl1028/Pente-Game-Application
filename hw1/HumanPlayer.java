import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HumanPlayer implements Player {

	Stone playerColor;
	/**
	 * constructor of humanPlayer
	 * @param s the Stone color that HumanPlayer is taking
	 */
	public HumanPlayer(Stone s) {
		playerColor = s;
	}
	/**
	 * ask user for an input and return a valid Coordinate
	 */
	@Override
	public Coordinate getMove(Board b) {
		/**
		 * first move must be center
		 */
		if (b.getMoveNumber() == 0) return new MyCoordinate(9,9);
		int row;
		int column;
		System.out.println("Insert \"row, column\" seperated with comma: ");
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		// input can be wrong format, check with regex
		String myPattern = "^(\\d+)[\\s]*,[\\s]*(\\d+)[\\s]*";
		Pattern r = Pattern.compile(myPattern);
		Matcher m = r.matcher(input);
		while (!m.matches()) 
		{	
			System.out.println("Illegal format! Insert \"row, column\" seperated with comma: ");	//first time regex
			input = s.nextLine();
			m = r.matcher(input);
		}
		
		row = Integer.parseInt(m.group(1));
		column = Integer.parseInt(m.group(2));
		Coordinate current = new MyCoordinate(row, column);	//good till here
		/**
		 * this coordinate could be illegal, throw this exception in myboard and catch in main Game
		 */
		return current;
	}
	/**
	 *  get the stone color
	 */
	@Override
	public Stone getStone() {
		// TODO Auto-generated method stub
		return playerColor;
	}

}
