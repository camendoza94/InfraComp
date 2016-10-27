package taller2;

public class T extends Thread{
	
	private static int [][] M;
	private static Id id;
	private static Maximo m;
	public static int nFilas;
	private static int nFilas2;
	private static int nThreads;
	
	public T(Id id, Maximo m){
		T.id = id;
		T.m = m;
	}
	
	private static void crearMatriz(int n) {
		M = new int[n][n];
		nFilas = n;
		nFilas2 = n;
		int m = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				M[i][j] = m;
				m++;
			}
		}
	}
	
	public void run() {
		int id2 = id.darId();
		while(id2 != -1){
			int maximo = 0;
			for (int i = 0; i < M[id2].length; i++) {
				if (M[id2][i] > maximo)
					maximo = M[id2][i];
			}
			m.anotar(maximo);
			nFilas2--;
			id2=id.darId();
		}
		if (nFilas2== 0){
			System.out.println(m.darMax());
		}
	}

	private static void inicializar(){
		m = new Maximo();
		crearMatriz(10);
		
	}
	
	public static void main(String[] args) {
		inicializar();
		Id id2 = new Id();
		nThreads = 10000;
		int j = nThreads;		
		for(int i = 0; i< M[0].length && j > 0; i++){
			new T(id2,m).start();
			j--;
		}
		
	}
}
