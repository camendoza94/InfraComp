package caso2SinSeguridad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
	public static final String HOST = "localhost";

	public static void main(String[] args) throws IOException {
		Socket socket = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		try {
			socket = new Socket(HOST, 4444);
			escritor = new PrintWriter(socket.getOutputStream(), true);
			lector = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		String fromServer;
		String fromUser;

		System.out.print("Escriba el mensaje para enviar:");
		fromUser = stdIn.readLine();
		if (fromUser != null) {
			System.out.println("Cliente: " + fromUser);
			escritor.println(fromUser);
		}
		if ((fromServer = lector.readLine()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				System.out.print("Escriba los algoritmos para enviar:");
				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("ALGORITMOS:" + fromUser);
					escritor.println("ALGORITMOS:" + fromUser.trim());
				}
			}
		}
		if ((fromServer = lector.readLine()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				escritor.println("CERTIFICADOCLIENTE");
			}
		}
		if ((fromServer = lector.readLine()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("CERTIFICADOSERVIDOR")) {
				escritor.println("OK");
			}
		}

		if ((fromServer = lector.readLine()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("CIFRADOKC+")) {
				escritor.println("CIFRADOKS+");
			}
		}

		if ((fromServer = lector.readLine()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				escritor.println("CIFRADOLS1");
			}
		}
		if ((fromServer = lector.readLine()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("CIFRADOLS2")) {
				escritor.println("OK");
			}
		}
		escritor.close();
		lector.close();
		socket.close();
		stdIn.close();
	}
}
