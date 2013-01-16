import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;


public class Solver {
	
	GameBoard gameBoard;
	ArrayList<Piece> pieceList;
	int nodeCount = 0;
	
	public Solver(int[][] intArray, ArrayList<String> stringList, int goalValue) {
		gameBoard = new GameBoard(intArray, goalValue);
		pieceList = new ArrayList<Piece>();
		
		
		// At the present moment, each piece is represented as a string.
		// So this wraps the pieces in a Piece class so it's easier
		// to manipulate.
		
		for (String s : stringList) {
			Piece p = new Piece(s);
			pieceList.add(p);
		}
		
	}
	
	/**
	 * The primary method for solving ShapeShifter puzzles
	 */
	
	public void solve() {
		ArrayList<Piece> pieceListBySize = new ArrayList<Piece>(pieceList);
		Collections.sort(pieceListBySize);	// This is done since the order
											// of piece placements does not matter.
											// Placing larger pieces down first lets
											// pruning occur sooner.
		
		/**
		 * Calculates number of resets needed for a game board.
		 * A "reset" refers to a tile that goes from the solved state to
		 * an unsolved state and back to the solved state.
		 * 
		 * This is the calculation used for pruning.
		 */
		
		ArrayList<Integer> areaLeft = new ArrayList<Integer>();
		areaLeft.add(0);
		int totalArea = 0;
		for (int i = pieceListBySize.size() - 1; i >= 0; i--) {
			totalArea += pieceListBySize.get(i).numberOfFlips;
			areaLeft.add(0, totalArea);
		}
		int totalResets = (totalArea - gameBoard.flipsNeeded) / (gameBoard.goal + 1);
		System.out.println("Total Resets: " + totalResets);
		
		/* 
		int highRow = 0;
		int highCol = 0;
		int[][] maxDim = new int[2][pieceListBySize.size()];
		for (int i = pieceListBySize.size() - 1; i >= 0; i--) {
			if (highRow < pieceListBySize.get(i).rows)
				highRow = pieceListBySize.get(i).rows;
			
			if (highCol < pieceListBySize.get(i).cols)
				highCol = pieceListBySize.get(i).cols;
			
			maxDim[0][i] = highRow;
			maxDim[1][i] = highCol;
		}
		*/
		
		long startTime = System.currentTimeMillis();
		
		Node<GameBoard> currentNode = new Node<GameBoard>(gameBoard);
		Stack<Node<GameBoard>> stack = new Stack<Node<GameBoard>>();
		stack.push(currentNode);
		
		while (!stack.isEmpty()) {
			nodeCount++;
			
			Node<GameBoard> node = stack.pop();
			GameBoard current = node.getElement();
			int depth = node.getDepth();
			
			// Checks to see if we reach a solved state.
			// If so, re-order the pieces then print out the solution.
			if (depth == pieceListBySize.size() && current.isSolved()) {
				ArrayList<Point> moves = new ArrayList<Point>();
				
				while (node.parent != null) {
					int index = node.level - 1;
					int sequence = pieceList.indexOf(pieceListBySize.get(index));
					Point p = new Point(current.moveRow, current.moveCol, sequence);
					moves.add(p);
					
					node = node.parent;
					current = node.getElement();
				}
				
				Collections.sort(moves);
				for (Point p : moves) {
					System.out.println(p);
				}
				System.out.println("Nodes opened: " + nodeCount);
				long endTime = System.currentTimeMillis();
				System.out.println("Elapsed Time: " + ((endTime - startTime) / 1000) + " secs.");
				break;
			}
			
			Piece currentPiece = pieceListBySize.get(depth);
			int pieceRows = currentPiece.rows;
			int pieceCols = currentPiece.cols;
			
			PriorityQueue<Node<GameBoard>> pQueue = new PriorityQueue<Node<GameBoard>>();
			
			// Place piece in every possible position in the board
			for (int i = 0; i <= current.rows - pieceRows; i++) {
				for (int j = 0; j <= current.cols - pieceCols; j++) {
					GameBoard g = current.place(currentPiece, i, j);
					
					if (totalResets - g.resets < 0)
						continue;
					
					// Put in the temporary priority queue
					pQueue.add(new Node<GameBoard>(g, node));
				}
			}
			
			// Remove from priority queue 1 at a time and put into stack.
			// The reason this is done is so that boards with the highest reset
			// count can be chosen over ones with fewer resets.
			while (!pQueue.isEmpty()) {
				Node<GameBoard> n = pQueue.remove();
				stack.push(n);
			}
			
			
		}
	}
	
	public void solveWithThreads() {
		Node<GameBoard> root = new Node<GameBoard>(gameBoard);
		Stack<Node<GameBoard>> stack = new Stack<Node<GameBoard>>();
		stack.add(root);
		
		ArrayList<Piece> pieceListBySize = new ArrayList<Piece>(pieceList);
		Collections.sort(pieceListBySize);
		ArrayList<Integer> areaLeft = new ArrayList<Integer>();
		
		areaLeft.add(0);
		
		int totalArea = 0;
		for (int i = pieceListBySize.size() - 1; i >= 0; i--) {
			totalArea += pieceListBySize.get(i).numberOfFlips;
			areaLeft.add(0, totalArea);
		}
		
		int totalResets = (totalArea - gameBoard.flipsNeeded) / (gameBoard.goal + 1);
		
		System.out.println("Total Resets: " + totalResets);
		
		int threads = Runtime.getRuntime().availableProcessors();
		
		for (int i = 0; i < threads; i++) {
			TreeThread t = new TreeThread(stack, pieceList, totalResets);
			t.start();
		}
		
	}
	
	public void solveWithPQueue() {
		ArrayList<Piece> pieceListBySize = new ArrayList<Piece>(pieceList);
		Collections.sort(pieceListBySize);
		ArrayList<Integer> areaLeft = new ArrayList<Integer>();
		
		areaLeft.add(0);
		
		int totalArea = 0;
		for (int i = pieceListBySize.size() - 1; i >= 0; i--) {
			totalArea += pieceListBySize.get(i).numberOfFlips;
			areaLeft.add(0, totalArea);
		}
		
		int totalResets = (totalArea - gameBoard.flipsNeeded) / (gameBoard.goal + 1);
		
		System.out.println("Total Resets: " + totalResets);
		
		long startTime = System.currentTimeMillis();
		
		Node<GameBoard> currentNode = new Node<GameBoard>(gameBoard);
		PriorityQueue<Node<GameBoard>> pQueue = new PriorityQueue<Node<GameBoard>>();
		pQueue.add(currentNode);
		
		while (!pQueue.isEmpty()) {
			nodeCount++;
			
			currentNode = pQueue.remove();
			GameBoard currentBoard = currentNode.getElement();
			int depth = currentNode.getDepth();
			
			if (depth == pieceListBySize.size() && currentBoard.isSolved()) {
				ArrayList<Point> moves = new ArrayList<Point>();
				
				while (currentNode.parent != null) {
					int index = currentNode.level - 1;
					int sequence = pieceList.indexOf(pieceListBySize.get(index));
					Point p = new Point(currentBoard.moveRow, currentBoard.moveCol, sequence);
					moves.add(p);
					
					currentNode = currentNode.parent;
					currentBoard = currentNode.getElement();
				}
				
				Collections.sort(moves);
				for (Point p : moves) {
					System.out.println(p);
				}
				System.out.println("Nodes opened: " + nodeCount);
				long endTime = System.currentTimeMillis();
				System.out.println("Elapsed Time: " + ((endTime - startTime) / 1000) + " secs.");
				break;
			}
			
			Piece currentPiece = pieceListBySize.get(depth);
			int pieceRows = currentPiece.rows;
			int pieceCols = currentPiece.cols;
			
			for (int i = 0; i <= currentBoard.rows - pieceRows; i++) {
				for (int j = 0; j <= currentBoard.cols - pieceCols; j++) {
					GameBoard g = currentBoard.place(currentPiece, i, j);
					
					if (totalResets - g.resets < 0)
						continue;
					
					pQueue.add(new Node<GameBoard>(g, currentNode));
				}
			}
		}
	}
	
	
	
	private Point getDim(GameBoard g) {
		int startingRow = 0;
		int startingCol = 0;
		int[][] gameBoard = g.gameBoard;
		
		for (int i = 0; i < gameBoard.length; i++) {
			boolean complete = true;
			int[] row = gameBoard[i];
			for (int j = 0; j < row.length; j++) {
				if (row[j] != g.goal) {
					complete = false;
					break;
				}
			}
			
			if (complete)
				startingRow++;
			else
				break;
		}
		
		for (int j = 0; j < gameBoard[0].length; j++) {
			boolean complete = true;
			
			for (int i = 0; i < gameBoard.length; i++) {
				if (gameBoard[i][j] != g.goal) {
					complete = false;
					break;
				}
			}
			
			if (complete)
				startingCol++;
			else
				break;
		}
		
		return new Point(startingRow, startingCol);
	}
}
