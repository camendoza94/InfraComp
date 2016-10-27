package caso1;

import java.util.ArrayList;

public class Buffer {
	private int capacity;
	private ArrayList<Message> messages;
	
	public Buffer(int capacity){
		this.capacity = capacity;
		messages = new ArrayList<Message>();
	}
	
	public synchronized Message getMessage() throws InterruptedException {
		while (messages.size() == 0) {
			wait();
		}
		return messages.remove(0);

	}
	
	public synchronized boolean putMessage(Message message){
		if(capacity > messages.size()){
			messages.add(message);
			notify();
			return true;
		}
		return false;
	}
}
