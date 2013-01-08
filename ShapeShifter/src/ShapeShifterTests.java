import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;


public class ShapeShifterTests {

	@Test
	public void testPieceFit() {
		Piece p = new Piece("111\n111\n001");
		Piece p2 = new Piece("11\n10");
		GameBoard g = new GameBoard(new int[3][3], 1);
		
		assertTrue(g.fits(p, 0, 0));
		assertTrue(g.fits(p2, 0, 0));
		assertFalse(g.fits(p, 0, 1));
		assertFalse(g.fits(p, 1, 0));
		assertTrue(g.fits(p2, 1, 1));
		assertFalse(g.fits(p2, 1, 2));
		assertFalse(g.fits(p2, 2, 2));
		
		GameBoard h;
		
		
		h = g.place(p, 0, 0);
		System.out.println(h);
		
		h = h.place(p2, 0, 0);
		System.out.println(h);
		
		h = h.place(p, 0, 0);
		System.out.println(h);
		
		h = h.place(p, 7, 0);
		System.out.println(h);
		
		h = g;
		h = h.place(p, 1, 0);
		System.out.println(h);
		
		ArrayList<Piece> list = new ArrayList<Piece>();
		list.add(p2);
		list.add(p);
		
		
		System.out.println(list);
		
		Collections.sort(list);
		System.out.println(list);
	}
	
	@Test
	public void testNodes() {
		
		Piece p = new Piece("111\n111\n001");
		Piece p2 = new Piece("11\n10");
		GameBoard g = new GameBoard(new int[3][3], 1);
		
		GameBoard h = g.place(p2, 0, 0);
		GameBoard i = h.place(p, 0, 0);
		
		Node<GameBoard> root = new Node<GameBoard>(g);
		Node<GameBoard> n1 = new Node<GameBoard>(h);
		Node<GameBoard> n2 = new Node<GameBoard>(i);
		
		ArrayList<Node<GameBoard>> list = new ArrayList<Node<GameBoard>>();
		
		list.add(root);
		list.add(n1);
		list.add(n2);
		
		Collections.sort(list);
		System.out.println(list);
				
	}

}
