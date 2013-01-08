
import java.util.*;

public class Piece implements Comparable<Piece> {
	public int rows, cols, dimension;
	int [][] pieceMap;
	int numberOfFlips = 0;
	private HashSet<Point> offsets = new HashSet<Point>();
	
	public Piece(String s) {
		rows = 1;
		int endRow = s.indexOf('\n');
		while (endRow != -1) {
			rows++;
			endRow = s.indexOf('\n', endRow + 1);
		}
		
		cols = 1;
		int endCol = s.indexOf('\n');
		if (endCol != -1)
			cols = endCol;
		else
			cols = s.length();
		
		pieceMap = new int[rows][cols];
		
		int stringPos = 0;
		for (int i = 0; i < pieceMap.length; i++) {
			for (int j = 0; j < pieceMap[i].length; j++) {
				int entry = Integer.parseInt(s.substring(stringPos, stringPos + 1));
				pieceMap[i][j] = entry;
				stringPos++;
				
				if (entry == 1) {
					numberOfFlips++;
					offsets.add(new Point(i, j));
				}
			}
			stringPos++;
		}
		
		dimension = rows * cols;
	}
	
	public int getFlips() {
		return numberOfFlips;
	}
	
	public HashSet<Point> getOffsets() {
		return offsets;
	}
	public String toString() {
		String result = "";
		
		for (int i = 0; i < pieceMap.length; i++) {
			result += Arrays.toString(pieceMap[i]) + "\n";
		}
		
		result += "FlipCount = " + numberOfFlips + "\n";
		
		return result;
	}
	
	public int get(int r, int c) {
		return pieceMap[r][c];
	}
	
	/*
	public int compareTo(Piece other) {
		if (this.dimension > other.dimension)
			return -1;
		else if (this.dimension == other.dimension)
			return 0;
		else
			return 1;
	}
	*/
	
	public int compareTo(Piece other) {
		if (this.numberOfFlips > other.numberOfFlips)
			return -1;
		else if (this.numberOfFlips == other.numberOfFlips)
			return 0;
		else
			return 1;
	}
}
