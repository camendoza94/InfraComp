package caso1;

public class Client extends Thread{
	
	private Buffer buffer;
	
	private int nMessages;

	public Client(Buffer buffer, int nMessages){
		this.buffer = buffer;
		this.nMessages = nMessages;
	}
	
	public void run() {
		for (int i = 0; i < nMessages; i++) {
			Message m = new Message(i);
			boolean putMessage = false;
			while (!putMessage) {
				putMessage = buffer.putMessage(m);
				if(!putMessage)
					yield();
			}
			synchronized (m) {
				while (!m.isHasAnswer()) {
					try {
						m.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			System.out.println("Primero: " + i + " Segundo: " + m.getContent());
		}
	}
}
