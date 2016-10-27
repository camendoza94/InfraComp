package caso1;

public class Server extends Thread{
	private Buffer buffer;
	public Server(Buffer buffer) {
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
			Message m;
			try {
				m = buffer.getMessage();
				synchronized (m) {
					m.setContent();
					m.setHasAnswer(true);
					m.notifyAll();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
