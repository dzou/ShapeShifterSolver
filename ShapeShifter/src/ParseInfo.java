import java.util.ArrayList;

/*
 * This class just holds data that the parser collected.
 */


public class ParseInfo {
	public int[][] intArray;
	public ArrayList<String> pieceList;
	public int goal;
	
	public ParseInfo(int[][] intArray, ArrayList<String> pieceList, int goal) {
		this.intArray = intArray;
		this.pieceList = pieceList;
		this.goal = goal;
	}
}
