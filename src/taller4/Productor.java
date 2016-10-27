package taller4;

public class Productor extends Thread {
	private Canal canal;
	
	public Productor (Canal canal){
		this.canal = canal;
	}
	public void run() {
			try {int i = 0;
			while(i < 100) {
				canal.enviar(i);
				i++;
			}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
