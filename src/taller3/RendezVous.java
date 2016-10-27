package taller3;

public class RendezVous extends Thread{
	private int id;
	private int suma = 100;
	private static Semaforo s;
	private static Semaforo s2;
	public RendezVous(int id, Semaforo s, Semaforo s2){
		this.id = id;
		RendezVous.s = s;
		RendezVous.s2 = s2;
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
		s2.V();
		s2.P();
		suma+=100;
		s2.V();
	}
	
	private void mb(){
		s.P();
		suma--;
		s.V();
		s2.V();
		s2.P();
		suma-=100;
		s2.V();
	}
	public static void main(String[] args) {
		Semaforo s2 = new Semaforo(1);
		Semaforo s3 = new Semaforo(-1);
		for(int i = 0; i < 2; i++){
			new RendezVous(i, s2, s3).start();
		}
	}
}
