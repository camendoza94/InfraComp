package taller4;

public class Consumidor extends Thread{
	private int contenido;

	private Canal canal;
	
	public Consumidor (Canal canal){
		this.canal = canal;
	}
	public void run() {
			try {
				int i = 0;
				while(i < 100) {
				canal.recibir(this);
				System.out.println(contenido);
				i++;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public int getContenido() {
		return contenido;
	}
	public void setContenido(int contenido) {
		this.contenido = contenido;
	}
}
