
public class Point implements Comparable<Point> {
	int x, y;
	int moveOrder;
	
	public Point(int x, int y, int moveOrder) {
		this.x = x;
		this.y = y;
		this.moveOrder = moveOrder;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		this.moveOrder = -1;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public int compareTo(Point other) {
		if (this.moveOrder < other.moveOrder)
			return -1;
		else if (this.moveOrder == other.moveOrder)
			return 0;
		else
			return 1;
	}
	
}
