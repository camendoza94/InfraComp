package taller3;

public class Multiplex extends Thread{
	private static int suma = 100;
	private static Semaforo s;
	public Multiplex(Semaforo s){
		Multiplex.s = s;
	}
	public void run() {
		m();
	}
	
	private void m() {
		s.P();
		suma++;
		s.V();
	}
	public static void main(String[] args) {
		Semaforo s2 = new Semaforo(1);
		for(int i = 0; i < 20; i++){
			new Multiplex (s2).start();
		}
	}
}
