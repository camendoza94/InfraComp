package caso1;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Main {
	public static void main(String[] args) {
		try {
			FileInputStream a = new FileInputStream(new File("data/propiedades.properties"));
			Properties p = new Properties();
			p.load(a);
			a.close();
			int numClientes = Integer.parseInt(p.getProperty("numClientes"));
			int numConsultas = Integer.parseInt(p.getProperty("numConsultas"));
			int capacidadBuffer = Integer.parseInt(p.getProperty("capacidadBuffer"));
			int numServidores = Integer.parseInt(p.getProperty("numServidores"));

			Buffer buffer = new Buffer(capacidadBuffer);
			for (int i = 0; i < numServidores; i++) {
				new Server(buffer).start();
			}
			for (int i = 0; i < numClientes; i++) {
				new Client(buffer, numConsultas).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
