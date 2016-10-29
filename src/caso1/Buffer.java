package caso1;

import java.util.ArrayList;

public class Buffer {
	private int capacity;
	private ArrayList<Message> messages;
	private int clients;
	
	public Buffer(int capacity, int clients){
		this.capacity = capacity;
		this.clients = clients;
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
	
	public synchronized void clientCompleted(){
		clients--;
	}
	
	public boolean hasClients(){
		return clients > 0;
	}
}
