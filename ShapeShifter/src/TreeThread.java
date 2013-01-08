import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;


public class TreeThread extends Thread {
	
	private static boolean solutionFound = false;
	private static long nodeCount = 0;
	
	private SafeStack<Node<GameBoard>> stack;
	private ArrayList<Piece> pieceList;
	private ArrayList<Piece> pieceListBySize;
	private int totalResets;
	
	public TreeThread(Stack<Node<GameBoard>> stack
			, ArrayList<Piece> pieceList, int totalResets) {
		this.stack = new SafeStack<Node<GameBoard>>(stack); 
		this.pieceList = pieceList;
		pieceListBySize = new ArrayList<Piece>(pieceList);
		Collections.sort(pieceListBySize);
		this.totalResets = totalResets;
	}
	
	public void run() {
		long startTime = System.currentTimeMillis();
		while (!solutionFound) {
			
			synchronized(this) {
				nodeCount++;
			}
			
			Node<GameBoard> node = stack.pop();
			GameBoard current = node.getElement();
			int depth = node.getDepth();
			
			// Checks if DONE!!
			if (depth == pieceListBySize.size() && current.isSolved()) {
				synchronized (this) {
					if (solutionFound)
						break;
					
					solutionFound = true;
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
			}
			
			Piece currentPiece = pieceListBySize.get(depth);
			int pieceRows = currentPiece.rows;
			int pieceCols = currentPiece.cols;
			
			PriorityQueue<Node<GameBoard>> pQueue = new PriorityQueue<Node<GameBoard>>();
			
			for (int i = 0; i <= current.rows - pieceRows; i++) {
				for (int j = 0; j <= current.cols - pieceCols; j++) {
					GameBoard g = current.place(currentPiece, i, j);
					
					if (totalResets - g.resets < 0)
						continue;
					
					pQueue.add(new Node<GameBoard>(g, node));
				}
			}
			
			synchronized(stack) {
				while (!pQueue.isEmpty()) {
					Node<GameBoard> n = pQueue.remove();
					stack.push(n);
				}
			}
			
		}
	}
}
