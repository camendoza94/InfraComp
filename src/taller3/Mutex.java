package taller3;

public class Mutex extends Thread{
	private int id;
	private int suma = 100;
	private static Semaforo s;
	public Mutex(int id, Semaforo s){
		this.id = id;
		Mutex.s = s;
	}
	public void run() {
		if(id==0){
			ma();
		} else {
			mb();
		}
	}
	
	private void ma() {
		s.P();
		suma++;
		s.V();
	}
	
	private void mb(){
		s.P();
		suma--;
		s.V();
	}
	public static void main(String[] args) {
		Semaforo s2 = new Semaforo(1);
		for(int i = 0; i < 2; i++){
			new Mutex(i, s2).start();
		}
	}
}
