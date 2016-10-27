package caso2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.security.Key;
import java.security.cert.X509Certificate;
import java.sql.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import javax.xml.bind.DatatypeConverter;

public class Cliente {
	public static final String HOST = "localhost";

	public static void main(String[] args) throws IOException {
		Socket socket = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		try {
			socket = new Socket(HOST, 4443);
			escritor = new PrintWriter(socket.getOutputStream(), true);
			lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		String fromUser;
		String[] algoritmos = null;

		X509Certificate certificadoServidor = null;
		CertificadoDigital certificadoCliente = null;
		SecretKeySpec llaveSimetrica = null;

		System.out.print("Escriba el mensaje para enviar:");
		fromUser = stdIn.readLine();
		if (fromUser != null) {
			System.out.println("Cliente: " + fromUser);
			escritor.println(fromUser);
		}
		if ((fromServer = lector.readLine()) != null && !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				System.out.print("Escriba los algoritmos para enviar:");
				fromUser = stdIn.readLine();
				if (fromUser != null) {
					algoritmos = fromUser.split(":");
					System.out.println("ALGORITMOS:" + fromUser);
					escritor.println("ALGORITMOS:" + fromUser.trim());
				}
			}
		}
		if ((fromServer = lector.readLine()) != null && !fromServer.equals("ERROR")) {
			System.out.println("Servidor: " + fromServer);
			if (fromServer.equals("OK")) {
				StringWriter escritorString = new StringWriter();
				JcaPEMWriter escritorPEM = new JcaPEMWriter((Writer) escritorString);
				escritorPEM.writeObject((Object) (certificadoCliente = new CertificadoDigital()).getCert());
				escritorPEM.flush();
				escritorPEM.close();
				escritor.println(escritorString.toString());
			}
		}
		if ((fromServer = lector.readLine()) != null && !fromServer.equals("ERROR")) {
			try {

				String decodificar = "";
				decodificar = String.valueOf(decodificar) + fromServer;
				while (!fromServer.equals("-----END CERTIFICATE-----")) {
					decodificar = String.valueOf(decodificar) + fromServer + "\n";
					fromServer = lector.readLine();
				}
				decodificar = String.valueOf(decodificar) + fromServer;
				StringReader lectorString = new StringReader(decodificar);
				PemReader lectorPEM = new PemReader((Reader) lectorString);
				PemObject PEMCertificadoServidor = lectorPEM.readPemObject();
				X509CertificateHolder holderCertificado = new X509CertificateHolder(
						PEMCertificadoServidor.getContent());
				certificadoServidor = new JcaX509CertificateConverter().getCertificate(holderCertificado);
				lectorPEM.close();
				escritor.println("OK");
			} catch (Exception e) {
				System.out.println("No se pudo decodificar el certificado");
			}
		}

		if ((fromServer = lector.readLine()) != null && !fromServer.equals("ERROR")) {
			try {
				fromServer = lector.readLine();
				byte[] bytes = DatatypeConverter.parseHexBinary(fromServer);
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, certificadoCliente.getKeyPair().getPrivate());
				byte[] decodificado = cipher.doFinal(bytes);
				llaveSimetrica = new SecretKeySpec(decodificado, algoritmos[0]);
				cipher.init(Cipher.ENCRYPT_MODE, certificadoServidor.getPublicKey());
				byte[] codificado = cipher.doFinal(llaveSimetrica.getEncoded());
				String llave = DatatypeConverter.printHexBinary(codificado);
				escritor.println(llave);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("No se pudo decodificar la llave");
			}
		}
		if ((fromServer = lector.readLine()) != null && fromServer.equals("OK")) {
			try {
				System.out.println("Servidor: " + fromServer);
				String algoritmo = (algoritmos[0]);
				if (algoritmo.equals("DES") || algoritmo.equals("AES"))
					algoritmo += "/ECB/PKCS5Padding";
				Cipher cipher = Cipher.getInstance(algoritmo);
				cipher.init(Cipher.ENCRYPT_MODE, (Key) llaveSimetrica);
				byte[] consulta = (Math.random() +"").getBytes();
				byte[] codificado = cipher.doFinal(consulta);
				String mensaje = DatatypeConverter.printHexBinary(codificado);
				Mac mac = Mac.getInstance(algoritmos[2]);
				mac.init((Key)llaveSimetrica);
				byte[] macMensaje = mac.doFinal(consulta);
				cipher.init(Cipher.ENCRYPT_MODE, (Key)llaveSimetrica);
				codificado = cipher.doFinal(macMensaje);
				String hash = DatatypeConverter.printHexBinary(codificado);
				escritor.println(mensaje + ":" + hash);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("No se pudo codificar el mensaje");
			}
		}
		if ((fromServer = lector.readLine()) != null) {
			byte[] descifrado = DatatypeConverter.parseHexBinary(fromServer);
			String algoritmo = (algoritmos[0]);
			if (algoritmo.equals("DES") || algoritmo.equals("AES"))
				algoritmo += "/ECB/PKCS5Padding";
			try {
				Cipher cipher = Cipher.getInstance(algoritmo);
				cipher.init(Cipher.DECRYPT_MODE, llaveSimetrica);
				String respuesta = new String(cipher.doFinal(descifrado));
				String confirmacion = respuesta.startsWith("OK:") ? "OK" : "ERROR";
				System.out.println("Servidor: " + respuesta);
				escritor.println(confirmacion);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("No se pudo decodificar la respuesta");
			}

			escritor.println("OK");
		}
		escritor.close();
		lector.close();
		socket.close();
		stdIn.close();
	}
}
