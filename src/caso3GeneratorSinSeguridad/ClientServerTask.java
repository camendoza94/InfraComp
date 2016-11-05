/*
 * Decompiled with CFR 0_118.
 */
package caso3GeneratorSinSeguridad;

import caso3ClientServerSinSeguridad.Client;
import caso3Core.Task;

public class ClientServerTask extends Task {
	@Override
	public void execute() {
		Client client = new Client();
		client.sendMessageToServer("HOLA");
		String fromServer;
		String alg = "DES:RSA:HMACMD5";
		long authInitTime = 0;
		long authEndTime = 0;
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("OK")) {
				client.sendMessageToServer("ALGORITMOS:" + alg);
			}

		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				authInitTime = System.currentTimeMillis();
				client.sendMessageToServer("CERTIFICADOCLIENTE");
			}
		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("CERTIFICADOSERVIDOR")) {
				client.sendMessageToServer("OK");
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("CIFRADOKC+")) {
				client.sendMessageToServer("CIFRADOKS+");
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				authEndTime = System.currentTimeMillis();
				client.sendMessageToServer("CIFRADOLS1");
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("CIFRADOLS2")) {
				client.sendMessageToServer("CIFRADOLS1");
				long queryEndTime = System.currentTimeMillis();
				long queryTime = queryEndTime - authEndTime;
			}
		}
		client.sendMessageToServer("EOT");
	}

	public void fail() {
		System.out.println("FAIL_TEST");
	}

	public void success() {
		System.out.println("OK_TEST");
	}
}
