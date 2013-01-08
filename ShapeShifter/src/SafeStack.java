import java.util.Stack;

public class SafeStack<T> extends Stack<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Stack<T> stack;
	
	public SafeStack(Stack<T> s) {
		this.stack = s;
	}
	
	public synchronized T push(T obj) {
		stack.push(obj);
		if (stack.size() == 1) {
			this.notifyAll();
		}
		return obj;
	}
	
	public synchronized T pop() {
		while (stack.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return stack.pop();
	}
}
