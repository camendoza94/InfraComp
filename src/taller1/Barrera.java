package src;

public class Barrera {
	private int n;
	
	public Barrera(){
		
	}
	
	public synchronized void barrera(){
		n--;
		if(n>0)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else {
			notifyAll();
		}
	}
	
	public synchronized void reservar(int k){
		while(k>n)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		n-=k;
	}
	
	public synchronized void liberar(int k){
		n +=k;
		notifyAll();
	}
	
	
	public void reservar2(int k){
		P(mutex);
		while(k>n){
			enEsperar++;
			V(m);
			P(s);
			P(m);
		}
		V(mutex);
	}
	
	public void liberar2(int k){
		P(mutex);
		n+=k;
		while(enEspera > 0){
			enEspera--;
			V(s);			
		}
		V(mutex)
	}
	
	
}
