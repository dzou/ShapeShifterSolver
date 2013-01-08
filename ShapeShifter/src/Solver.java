import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;


public class Solver {
	
	GameBoard gameBoard;
	ArrayList<Piece> pieceList;
	int nodeCount = 0;
	
	public Solver(int[][] intArray, ArrayList<String> stringList, int goalValue) {
		gameBoard = new GameBoard(intArray, goalValue);
		pieceList = new ArrayList<Piece>();
		
		for (String s : stringList) {
			Piece p = new Piece(s);
			pieceList.add(p);
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
	
	public void solve() {
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
		
		long startTime = System.currentTimeMillis();
		
		Node<GameBoard> currentNode = new Node<GameBoard>(gameBoard);
		Stack<Node<GameBoard>> stack = new Stack<Node<GameBoard>>();
		stack.push(currentNode);
		
		while (!stack.isEmpty()) {
			nodeCount++;
			
			Node<GameBoard> node = stack.pop();
			GameBoard current = node.getElement();
			int depth = node.getDepth();
			
			// Checks if DONE!!
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
			
			Point dimensions = getDim(current);
			highRow = maxDim[0][depth];
			highCol = maxDim[1][depth];
			
			int startingRow = 0;
			int startingCol = 0;
			
			if (current.rows - dimensions.x >= highRow)
				startingRow = dimensions.x;
			
			if (current.cols - dimensions.y >= highCol)
				startingCol = dimensions.y;
			
			for (int i = startingRow; i <= current.rows - pieceRows; i++) {
				for (int j = startingCol; j <= current.cols - pieceCols; j++) {
					GameBoard g = current.place(currentPiece, i, j);
					
					if (totalResets - g.resets < 0)
						continue;
					
					pQueue.add(new Node<GameBoard>(g, node));
				}
			}
			
			while (!pQueue.isEmpty()) {
				Node<GameBoard> n = pQueue.remove();
				stack.push(n);
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
