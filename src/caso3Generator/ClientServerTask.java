/*
 * Decompiled with CFR 0_118.
 */
package caso3Generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Key;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import caso3ClientServer.Certificate;
import caso3ClientServer.Client;
import caso3ClientServer.Servidor;
import caso3Core.Task;

public class ClientServerTask extends Task {

	private PrintWriter writer;
	@Override
	public void execute() {
		Client client = new Client();
		client.sendMessageToServer("HOLA");
		String fromServer;
		String alg = "DES:RSA:HMACMD5";
		String[] algoritmos = alg.split(":");
		long authInitTime = 0;
		long authEndTime = 0;
		long authTime = 0;

		X509Certificate certificadoServidor = null;
		Certificate certificadoCliente = null;
		SecretKeySpec llaveSimetrica = null;
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("OK")) {
				client.sendMessageToServer("ALGORITMOS:" + alg);
			}

		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			if (fromServer.equals("OK")) {
				authInitTime = System.currentTimeMillis();
				StringWriter escritorString = new StringWriter();
				JcaPEMWriter escritorPEM = new JcaPEMWriter(
						(Writer) escritorString);
				try {
					escritorPEM
							.writeObject((Object) (certificadoCliente = new Certificate())
									.getCert());
					escritorPEM.flush();
					escritorPEM.close();
				} catch (IOException e) {
				}
				client.sendMessageToServer(escritorString.toString());
			}
		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			try {

				String decodificar = "";
				decodificar = String.valueOf(decodificar) + fromServer;
				while (!fromServer.equals("-----END CERTIFICATE-----")) {
					decodificar = String.valueOf(decodificar) + fromServer
							+ "\n";
					fromServer = client.waitForMessageFromServer();
				}
				decodificar = String.valueOf(decodificar) + fromServer;
				StringReader lectorString = new StringReader(decodificar);
				PemReader lectorPEM = new PemReader((Reader) lectorString);
				PemObject PEMCertificadoServidor = lectorPEM.readPemObject();
				X509CertificateHolder holderCertificado = new X509CertificateHolder(
						PEMCertificadoServidor.getContent());
				certificadoServidor = new JcaX509CertificateConverter()
						.getCertificate(holderCertificado);
				lectorPEM.close();
				client.sendMessageToServer("OK");
			} catch (Exception e) {
			}
		}

		if ((fromServer = client.waitForMessageFromServer()) != null
				&& !fromServer.equals("ERROR")) {
			try {
				fromServer = client.waitForMessageFromServer();
				byte[] bytes = DatatypeConverter.parseHexBinary(fromServer);
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, certificadoCliente
						.getKeyPair().getPrivate());
				byte[] decodificado = cipher.doFinal(bytes);
				llaveSimetrica = new SecretKeySpec(decodificado, algoritmos[0]);
				cipher.init(Cipher.ENCRYPT_MODE,
						certificadoServidor.getPublicKey());
				byte[] codificado = cipher.doFinal(llaveSimetrica.getEncoded());
				String llave = DatatypeConverter.printHexBinary(codificado);
				client.sendMessageToServer(llave);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ((fromServer = client.waitForMessageFromServer()) != null
				&& fromServer.equals("OK")) {
			try {
				authEndTime = System.currentTimeMillis();
				authTime = authEndTime - authInitTime;
				String algoritmo = (algoritmos[0]);
				if (algoritmo.equals("DES") || algoritmo.equals("AES"))
					algoritmo += "/ECB/PKCS5Padding";
				Cipher cipher = Cipher.getInstance(algoritmo);
				cipher.init(Cipher.ENCRYPT_MODE, (Key) llaveSimetrica);
				byte[] consulta = (Math.random() + "").getBytes();
				byte[] codificado = cipher.doFinal(consulta);
				String mensaje = DatatypeConverter.printHexBinary(codificado);
				Mac mac = Mac.getInstance(algoritmos[2]);
				mac.init((Key) llaveSimetrica);
				byte[] macMensaje = mac.doFinal(consulta);
				cipher.init(Cipher.ENCRYPT_MODE, (Key) llaveSimetrica);
				codificado = cipher.doFinal(macMensaje);
				String hash = DatatypeConverter.printHexBinary(codificado);
				client.sendMessageToServer(mensaje + ":" + hash);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ((fromServer = client.waitForMessageFromServer()) != null) {
			byte[] descifrado = DatatypeConverter.parseHexBinary(fromServer);
			String algoritmo = (algoritmos[0]);
			if (algoritmo.equals("DES") || algoritmo.equals("AES"))
				algoritmo += "/ECB/PKCS5Padding";
			try {
				Cipher cipher = Cipher.getInstance(algoritmo);
				cipher.init(Cipher.DECRYPT_MODE, llaveSimetrica);
				String respuesta = new String(cipher.doFinal(descifrado));
				String confirmacion = respuesta.startsWith("OK:") ? "OK"
						: "ERROR";
				client.sendMessageToServer(confirmacion);
				long queryEndTime = System.currentTimeMillis();
				long queryTime = queryEndTime - authEndTime;
				crearWriter();
				writer.println(authTime + "," + queryTime);
				writer.close();
				writer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}

			client.sendMessageToServer("OK");
		}
		client.sendMessageToServer("EOT");
	}

	public void crearWriter() throws Exception {
		if (writer == null) {
			boolean noExiste = !new File(Servidor.N_THREADS + "Threads" + Generator.numberOfTasks +"SS.csv").exists();
			BufferedWriter buffer = new BufferedWriter(new FileWriter(
					Servidor.N_THREADS + "threads" + Generator.numberOfTasks +"SS.csv", true));
			writer = new PrintWriter(buffer);

			if (noExiste)
				writer.println("Tiempo autenticacion, Tiempo Consulta");
		}

	}

	public void fail() {
		System.out.println("FAIL_TEST");
	}

	public void success() {
		System.out.println("OK_TEST");
	}
}
