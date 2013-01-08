import java.util.*;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
	T self;
	Node<T> parent;
	int level;
	PriorityQueue<Node<T>> children;
	
	public Node(T object) {
		parent = null;
		self = object;
		level = 0;
		children = new PriorityQueue<Node<T>>();
	}
	
	public Node(T object, Node<T> parent) {
		this.parent = parent;
		self = object;
		level = parent.level + 1;
		children = new PriorityQueue<Node<T>>();
	}

	public T getElement() {
		return self;
	}
	
	public String toString() {
		String s = self.toString();
		return s;
	}
	
	public int compareTo(Node<T> other) {
		T otherElement = other.getElement();
		T myElement = self;
		
		return myElement.compareTo(otherElement);
	}
	
	public int getDepth() {
		return level;
	}
	
	public void add(T obj) {
		children.add(new Node<T>(obj, this));
	}
}
