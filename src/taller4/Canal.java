package taller4;

public class Canal {
	private boolean ocupado;	
	private int contenido;
	public Canal() {
		ocupado = false;
	}
	
	public void recibir(Consumidor c) throws InterruptedException {
		synchronized (this) {
			if (ocupado) {
				ocupado = false;
				notify();
			} else {
				ocupado = true;
				wait();
			}
			c.setContenido(contenido);
		}
	}
	
	public void enviar(int contenido) throws InterruptedException {
		synchronized (this) {
			this.contenido = contenido;
			if (ocupado) {
				ocupado = false;
				notify();
			} else {
				ocupado = true;
				wait();
			}
		}
	}
}
