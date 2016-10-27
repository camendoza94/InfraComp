package taller3;

public class Señalamiento extends Thread{
	private int id;
	private int suma = 100;
	private static Semaforo s;
	public Señalamiento(int id, Semaforo s){
		this.id = id;
		Señalamiento.s = s;
	}
	public void run() {
		if(id==0){
			ma();
		} else {
			mb();
		}
	}
	
	private void ma() {
		suma++;
		s.V();
	}
	
	private void mb(){
		s.P();
		suma--;
	}
	public static void main(String[] args) {
		Semaforo s2 = new Semaforo(0);
		for(int i = 0; i < 2; i++){
			new Señalamiento(i, s2).start();
		}
	}
}
