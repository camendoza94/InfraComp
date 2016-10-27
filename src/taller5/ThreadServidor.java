package taller5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServidor extends Thread {
	// atributo socket
	private Socket sktCliente = null;
	// defina un atributo identificador local de tipo int
	private int id;

	public ThreadServidor(Socket pSocket, int pId) {
		this.sktCliente = pSocket;
		this.id = pId;
	}

	public void run() {

		System.out.println("Inicio de nuevo thread." + id);
		try {
			PrintWriter escritor = new PrintWriter(sktCliente.getOutputStream(), true);
			BufferedReader lector = new BufferedReader(new InputStreamReader(sktCliente.getInputStream()));
			procesar(lector, escritor);
			escritor.close();
			lector.close();
			sktCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void procesar(BufferedReader pIn, PrintWriter pOut) throws IOException {
		String inputLine, outputLine;
		int estado = 0;
		PrintWriter escritor = new PrintWriter(sktCliente.getOutputStream(), true);

		while (estado < 3 && (inputLine = pIn.readLine()) != null) {
			switch (estado) {
			case 0:
				if (inputLine.equalsIgnoreCase("HOLA")) {
					outputLine = "LISTO";
					estado++;
				} else {
					outputLine = "ERROR-EsperabaHola";
					estado = 0;
				}
				escritor.println(outputLine);
				break;
			case 1:
				try {
					int val = Integer.parseInt(inputLine);
					val++;
					outputLine = "" + val;
					estado++;
				} catch (Exception e) {
					outputLine = "ERROR-EnArgumentoEsperado";
					estado = 0;
				}
				escritor.println(outputLine);
				break;
			case 2:
				if (inputLine.equalsIgnoreCase("OK")) {
					outputLine = "ADIOS";
					estado++;
				} else {
					outputLine = "ERROR-EsperabaOK";
					estado = 0;
				}
				escritor.println(outputLine);
				break;
			default:
				outputLine = "ERROR";
				estado = 0;
				escritor.println(outputLine);
				break;
			}
		}
	}
}