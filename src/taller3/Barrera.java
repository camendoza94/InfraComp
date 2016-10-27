package taller3;

public class Barrera extends Thread{
	private int nThreads = 0;
	private static int N;
	private static Semaforo s;
	private static Semaforo s2;
	public Barrera(Semaforo s, Semaforo s2){
		Barrera.s = s;
		Barrera.s2 = s2;
	}
	public void run() {
		m();
	}
	
	private void m() {
		s.P();
		nThreads++;
		if(nThreads == N)
			s2.V();
		s.V();
		s2.P();
		//Do something
		s2.V();
	}
	public static void main(String[] args) {
		N = 100;
		Semaforo s2 = new Semaforo(1);
		Semaforo s3 = new Semaforo(0);
		for(int i = 0; i < 20; i++){
			new Barrera (s2,s3).start();
		}
	}
}
