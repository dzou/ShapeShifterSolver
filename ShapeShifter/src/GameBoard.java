import java.util.Arrays;
import java.util.HashSet;

/**
 * 
 * Each Gameboard is stored as a gameboard object which
 * holds information about the tiles as well as
 * other parameters that help in finding a solution.
 */

public class GameBoard implements Comparable<GameBoard> {
	
	int[][] gameBoard;	
	int goal, flips, flipsNeeded; 
	int moveRow, moveCol;
	int rows, cols;
	int resets;
	
	public GameBoard(int[][] array, int goalValue, 
			int moveRow, int moveCol, int resets) {
		this.moveRow = moveRow;
		this.moveCol = moveCol;
		
		gameBoard = array;
		goal = goalValue;
		
		rows = gameBoard.length;
		cols = gameBoard[0].length;
		
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				flips += array[i][j];
				flipsNeeded += goal - array[i][j];
			}
		}
		
		this.resets = resets;
	}
	
	public GameBoard(int[][] array, int goalValue) {
		moveRow = -1;
		moveCol = -1;
		
		gameBoard = array;
		goal = goalValue;
		
		rows = gameBoard.length;
		cols = gameBoard[0].length;
		
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				flips += array[i][j];
				flipsNeeded += goal - array[i][j];
			}
		}
		
		resets = 0;
	}
	
	public GameBoard(int[][] array, int goalValue, 
			int moveRow, int moveCol, int resets, int flipsNeeded) {
		this.moveRow = moveRow;
		this.moveCol = moveCol;
		
		gameBoard = array;
		goal = goalValue;
		
		rows = gameBoard.length;
		cols = gameBoard[0].length;
		
		this.resets = resets;
		this.flipsNeeded = flipsNeeded;
		this.flips = rows * cols - flipsNeeded;
	}
	
	/**
	 * Places a piece on the board and generates the resulting gameboard
	 * 
	 * @param p - The piece that is placed on the board
	 * @param x - x-coordinate of placement
	 * @param y - y-coordinate of placement
	 * @return - The resulting gameboard after the placement
	 */
	
	public GameBoard place(Piece p, int x, int y) {
		
		int[][] copy = new int[gameBoard.length][];
				
		for (int i = 0; i < gameBoard.length; i++) {
			copy[i] = gameBoard[i].clone();
		}
		
		int resetCount = resets;
		int additionalFlipsNeeded = flipsNeeded;
		
		HashSet<Point> offsets = p.getOffsets();
		
		for (Point point : offsets) {
			
			int pieceRow = point.x + x;
			int pieceCol = point.y + y;
			int currentVal = copy[pieceRow][pieceCol] + 1;
			
			if (currentVal > goal) {
				currentVal = 0;
				resetCount++;
				additionalFlipsNeeded += goal;
			} else {
				additionalFlipsNeeded--;
			}

			
			copy[pieceRow][pieceCol] = currentVal;	
		}

		return new GameBoard(copy, goal, x, y, resetCount, additionalFlipsNeeded);
	}
	
	/**
	 * Checks to see if the placement of Piece p in (x, y) fits
	 * completely on the board.
	 */
	
	public boolean fits(Piece p, int x, int y) {
		int maxRow = gameBoard.length - x;
		int maxCol = gameBoard[0].length - y;
		
		if (p.rows > maxRow || p.cols > maxCol) 
			return false;
		else
			return true;
	}
	
	/**
	 * 
	 * @return true if the board is solved, false if not.
	 */
	
	public boolean isSolved() {
		return flipsNeeded == 0;
	}
	
	/**
	 * String representation for testing purposes.
	 */
	
	public String toString() {
		String result = "";
		
		for (int i = 0; i < gameBoard.length; i++) {
			result += Arrays.toString(gameBoard[i]) + "\n";
		}
			
		result += "Flips: " + flips + "\nFlipsNeeded: " + flipsNeeded;
		return result;
	}

	/**
	 * Used when placing Gameboards in priority queue.
	 * This helps the tree pruning phase occur higher up in the tree.
	 */
	
	public int compareTo(GameBoard other) {
		if (this.resets > other.resets)
			return 1;
		else if (this.resets == other.resets)
			return 0;
		else
			return -1;
	}
	
	/*
	// Change to absVal of difference heuristic
	
	
	public int compareTo(GameBoard other) {
		int otherScore = other.getWorkableArea() + other.resets;
		int myScore = this.getWorkableArea() + this.resets;
		
		if (myScore > otherScore) {
			return -1;
		} else if (myScore == otherScore) {
			return 0;
		} else {
			return 1;
		}
	}
	
	private int getWorkableArea() {
		int freeLines = 0;
		
		for (int i = 0; i < gameBoard.length; i++) {
			int[] row = gameBoard[i];
			boolean complete = true;
			for (int j = 0; j < row.length; j++) {
				if (row[j] != goal) {
					complete = false;
					break;
				}
			}
			if (complete)
				freeLines++;
		}
		
		for (int col = 0; col < gameBoard[0].length; col++) {
			boolean complete = true;
			for (int row = 0; row < gameBoard.length; row++) {
				if (gameBoard[row][col] != goal) {
					complete = false;
					break;
				}
			}
			
			if (complete)
				freeLines++;
		}
			
		return freeLines;
	}
	
	
	public int compareTo(GameBoard other) {
		int h = this.resets - this.flipsNeeded;
		int otherh = other.resets - other.flipsNeeded;
		
		if (h > otherh)
			return -1;
		else
			return 1;
	}
	
	
	public int compareTo(GameBoard other) {
		if (this.resets > other.resets)
			return -1;
		else if (this.resets == other.resets) {
			if (this.flipsNeeded > other.flipsNeeded)
				return -1;
			else
				return 1;
		}
		else
			return 1;
	}
	*/
}
