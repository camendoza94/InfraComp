package caso1;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer {
	private int capacity;
	private ArrayList<Message> messages;
	private AtomicInteger clients;
	
	public Buffer(int capacity, AtomicInteger clients){
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
	
	public void clientCompleted(){
		clients.decrementAndGet();
	}
	
	public boolean hasClients(){
		System.out.println("Clients count" + clients);
		return clients.get() > 0;
	}
}
