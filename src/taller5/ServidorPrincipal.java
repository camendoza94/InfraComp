package taller5;

import java.io.IOException;
import java.net.ServerSocket;

public class ServidorPrincipal {
	public static final int PUERTO = 8540;

	public static void main(String[] args) throws IOException {
		ServerSocket ss = null;
		boolean continuar = true;
		int nThread = 0;
		try {
			ss = new ServerSocket(PUERTO);
		} catch (IOException e) {
			System.err.println("No pudo crear socket en el puerto:" + PUERTO);
			System.exit(-1);
		}
		while (continuar) {
			new ThreadServidor(ss.accept(), nThread).start();
			nThread++;
		}
		ss.close();
	}
}
