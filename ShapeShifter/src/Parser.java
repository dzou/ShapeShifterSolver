import java.util.*;

/**
 * 
 * This class is used to parse data from the source of the 
 * game's page.
 * 
 */

public class Parser {

	public static ParseInfo beginParsing() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		String current = "";
		
		int[][] intArray = null;
		int goalValue = -1;
		ArrayList<String> pieceList = new ArrayList<String>();
		
		String[][] array = null;
		ArrayList<String> sequence = new ArrayList<String>();
		
		while (scanner.hasNextLine()) {
			current = scanner.nextLine();
			
			if (current.equals("</html>"))
				break;
			
			// Part 1 - get array
			// In the HTML source code, each board game is represented
			// as an array of 3 letter strings. Each board can be of
			// variable height and length, and the three letter strings
			// are also variable.
			//
			// The three letter strings that we are parsing correspond to a type
			// of tile on the board.
			
			if (current.indexOf("imgLocStr = new Array(") != -1) {
				int left = current.indexOf('(');
				int right = current.indexOf(')');
				String sRows = current.substring(left + 1, right);
				int rows = Integer.parseInt(sRows);
				array = new String[rows][];
				
				for (int i = 0; i < rows; i++) {
					current = scanner.nextLine();
					left = current.indexOf('(');
					right = current.indexOf(')');
					String sEntries = current.substring(left + 1, right);
					int entries = Integer.parseInt(sEntries);
					
					array[i] = new String[entries];
				}
				
				for (int i = 0; i < array.length; i++) {
					for (int j = 0; j < array[i].length; j++) {
						current = scanner.nextLine();
						int index = current.indexOf('"');
						
							if (index == -1) {
								System.err.println("LINE 39 - MISPARSE");
								throw new IllegalArgumentException();
							}
						
						String entry = current.substring(index + 1, current.length() - 1);
						array[i][j] = entry;
					}
				}
			}
			
			// Part 2 - get sequence
			// Precondition: Sequence can only have one tile after goal.
			//
			// This section parses the HTML code that defines the goal tile
			// of the game. Also, this determines the sequence of tiles in the game.
			// Each level of Shapeshifter follows a tile sequence that is defined
			// at the bottom of the board. This tile sequence shows what the tile
			// will "shapeshift" into after it is flipped.
			
			if (current.indexOf("bordercolor='gray'") != -1) {
				int pos = current.indexOf(".gif");
				while (pos != -1) {
					int right = pos;
					int left = current.lastIndexOf('/', right);
					
					String entry = current.substring(left + 1, right - 2);
					
					if (!entry.equals("arr") && (sequence.isEmpty()
							|| !entry.equals(sequence.get(0))))
						sequence.add(entry);
					
					current = scanner.nextLine();
					pos = current.indexOf(".gif");
				}
			}
			
			
			if (array == null) 
				continue;
			
			
			// Part 3 - BookKeeping
			// Rather than working with an array of strings, I convert them into
			// an array of integers so they are easier to work with. This allows for
			// arithmetic operations too that makes other calculations easier later on.
			intArray = new int[array.length][];
			for (int i = 0; i < array.length; i++) {
				intArray[i] = new int[array[i].length];
				for (int j = 0; j < array[i].length; j++) {
					intArray[i][j] = sequence.indexOf(array[i][j]);
				}
			}
			goalValue = sequence.size() - 1;
			
			// Part 4 - Getting the pieces
			//
			// Now we parse the pieces in the game. It happens that each piece
			// is created with an HTML table. In the source code, there
			// are several tags to look out for that I commented out below. The tags
			// and their attributes defined the dimension of each piece as well as what
			// shape it has.
			//
			// This code is a little convoluted;
			// I'm thinking recursion would have been better here...
			
			if (current.indexOf("<big>ACTIVE SHAPE</big>") != -1) {
				
				/*  String startRow = "<tr>";
					String endRow = "</tr>";
					String startCell = "<td";
					String endCell = "</td>";
					String startPiece = "<table border=0 cellpadding=0";
					String endPiece = "</table>";
				*/
				
				int startPiece = current.indexOf("<table border=0 cellpadding=0");
				while (startPiece != -1) {
					
					String entry = "";
					int endPiece = current.indexOf("</table>", startPiece);
					int startRow = current.indexOf("<tr>", startPiece);
					
					while (startRow != -1 && startRow < endPiece) {
						int endRow = current.indexOf("</tr>", startRow);
						int startCell = current.indexOf("<td", startRow);
						
						while (startCell != -1 && startCell < endRow) {
							int endCell = current.indexOf("</td>", startCell);
							if (current.charAt(startCell + 3) == '>')
								entry += "1";
							else
								entry += "0";
							startCell = current.indexOf("<td", endCell);
						}
						
						startRow = current.indexOf("<tr>", endRow);
						if (startRow != -1 && startRow < endPiece)
							entry += "\n";
					}
					
					startPiece = current.indexOf("<table border=0 cellpadding=0", endPiece);
					pieceList.add(entry);
				}
				
			}
			
		}
		
		/*
			for (int i = 0; i < array.length; i++) {
				System.out.println(Arrays.toString(array[i]));
			}
			System.out.println(sequence);
			for (int i = 0; i < intArray.length; i++) {
				System.out.println(Arrays.toString(intArray[i]));
			}
			System.out.println(goalValue);
			System.out.println(pieceList);
		*/
		scanner.close();
		System.out.println("done parsing");
		return new ParseInfo(intArray, pieceList, goalValue);
	}
	
}
