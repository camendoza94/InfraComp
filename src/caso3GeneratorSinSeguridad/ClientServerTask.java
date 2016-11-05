/*
 * Decompiled with CFR 0_118.
 */
package caso3GeneratorSinSeguridad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import caso3ClientServerSinSeguridad.Client;
import caso3ClientServerSinSeguridad.Servidor;
import caso3Core.Task;

public class ClientServerTask extends Task {
	private PrintWriter writer;
	@Override
	public void execute() {
		Client client = new Client();
		client.sendMessageToServer("HOLA");
		String fromServer;
		String alg = "DES:RSA:HMACMD5";
		long authInitTime = 0;
		long authEndTime = 0;
		long authTime = 0;
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("OK")) {
				client.sendMessageToServer("ALGORITMOS:" + alg);
			}

		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("OK")) {
				authInitTime = System.nanoTime();
				client.sendMessageToServer("CERTIFICADOCLIENTE");
			}
		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("CERTIFICADOSERVIDOR")) {
				client.sendMessageToServer("OK");
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("CIFRADOKC+")) {
				client.sendMessageToServer("CIFRADOKS+");
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("OK")) {
				authEndTime = System.nanoTime();
				authTime = authEndTime - authInitTime;
				client.sendMessageToServer("CIFRADOLS1");
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("CIFRADOLS2")) {
				client.sendMessageToServer("CIFRADOLS1");
				long queryEndTime = System.nanoTime();
				long queryTime = queryEndTime - authEndTime;
				try {
					crearWriter();
				} catch (Exception e) {
					e.printStackTrace();
				}
				synchronized (writer) {
					writer.println(authTime + "," + queryTime);
					writer.close();
				}
			}
		}
		client.sendMessageToServer("EOT");
	}
	
	public void crearWriter() throws Exception {
			boolean noExiste = !new File(Servidor.N_THREADS + "Threads" + Generator.numberOfTasks +"SS.csv").exists();
			BufferedWriter buffer = new BufferedWriter(new FileWriter(
					Servidor.N_THREADS + "threads" + Generator.numberOfTasks +"SS.csv", true));
			writer = new PrintWriter(buffer);

			if (noExiste)
				writer.println("Tiempo autenticacion, Tiempo Consulta");

	}

	public void fail() {
		System.out.println("FAIL_TEST");
	}

	public void success() {
		System.out.println("OK_TEST");
	}
}
