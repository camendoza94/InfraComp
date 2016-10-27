package taller2;

public class Maximo {
	private int nThreads;
	private int maximo;
	public synchronized boolean anotar( int n ) {
		nThreads--;
		if(n>maximo) maximo = n;
		return (nThreads == 0);
	}
	public int darMax(){
		return maximo;
	}
}
