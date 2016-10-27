package taller3;

public class Semaforo {
	private int contador;
	
	public Semaforo(int contador){
		this.contador = contador;
	}
	
	public synchronized void P(){
		contador--;
		if(contador < 0)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public synchronized void V (){
		contador++;
		notify();
	}
}
