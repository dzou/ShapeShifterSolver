/*
 * Shapeshifter Solver
 * 
 * To run this program, run the main method of the Driver. Then copy and paste
 * the page source of the Shapeshifter game into console. After solving is complete,
 * a solution list of coordinates will be generated. Use this list of
 * coordinates to solve the puzzle.
 */

public class Driver {
	public static void main(String[] args) {
		ParseInfo info = Parser.beginParsing();
		Solver s = new Solver(info.intArray, info.pieceList, info.goal);
		s.solve();
	}
}
