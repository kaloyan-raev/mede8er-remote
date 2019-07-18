package name.raev.kaloyan.android.mede8er.remote;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {
	
	private Queue<Command> queue = new LinkedList<Command>();
	
	public synchronized void add(Command command) {
		queue.add(command);
	}
	
	public synchronized Command poll() {
		return queue.poll();
	}
	
	public synchronized void clear() {
		queue.clear();		
	}

}
