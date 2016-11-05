package ServidorNovasoft;

import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import utils.Seguridad;
import utils.Transformacion;

/**
 * Esta clase implementa el protocolo que se realiza al recibir una conexión de un cliente.
 * Infraestructura Computacional Universidad de los Andes. 
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * 
 * @author Cristian Fabián Brochero 		-  201620.
 */
public class Worker implements Runnable {

	// ----------------------------------------------------
	// CONSTANTES DE CONTROL DE IMPRESION EN CONSOLA
	// ----------------------------------------------------
	public static final boolean SHOW_ERROR = true;
	public static final boolean SHOW_S_TRACE = true;
	public static final boolean SHOW_IN = true;
	public static final boolean SHOW_OUT = true;
	// ----------------------------------------------------
	// CONSTANTES PARA LA DEFINICION DEL PROTOCOLO
	// ----------------------------------------------------
	public static final String OK = "OK";
	public static final String ALGORITMOS = "ALGORITMOS";
	public static final String RC4 = "RC4";
	public static final String BLOWFISH = "Blowfish";
	public static final String AES = "AES";
	public static final String DES = "DES";
	public static final String RSA = "RSA";
	public static final String HMACMD5 = "HMACMD5";
	public static final String HMACSHA1 = "HMACSHA1";
	public static final String HMACSHA256 = "HMACSHA256";
	public static final String CERTSRV = "CERTSRV";
	public static final String CERTPA = "CERTPA";
	public static final String SEPARADOR = ":";
	public static final String HOLA = "HOLA";
	public static final String INIT = "INIT";
	public static final String RTA = "RTA";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String ERROR_FORMATO = "Error en el formato. Cerrando conexion";
	
	
	private int id;
	private Socket ss;
	private KeyPair keyPair;
	
	public Worker(int pId, Socket pSocket) {
		id = pId;
		ss = pSocket;
		// Adiciona la libreria como un proveedor de seguridad.
		// Necesario para crear llaves.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());	
	}
	/**
	 * Metodo que se encarga de imprimir en consola todos los errores que se 
	 * producen durante la ejecuación del protocolo. 
	 * Ayuda a controlar de forma rapida el cambio entre imprimir y no imprimir este tipo de mensaje
	 */
	private void printError(Exception e) {
		if(SHOW_ERROR)		System.out.println(e.getMessage());
		if(SHOW_S_TRACE) 	e.printStackTrace();	
	}

	/**
	 * Metodo que se encarga de leer los datos que envia el punto de atencion.
	 *  Ayuda a controlar de forma rapida el cambio entre imprimir y no imprimir este tipo de mensaje
	 */
	private String read(BufferedReader reader) throws IOException {
		String linea = reader.readLine();
		if(SHOW_IN)			System.out.println("Thread " + id + "<<CLNT: (recibe) " + linea);
		return linea;
		
	}

	/**
	 * Metodo que se encarga de escribir los datos que el servidor envia el punto de atencion.
	 *  Ayuda a controlar de forma rapida el cambio entre imprimir y no imprimir este tipo de mensaje
	 */
	private void write(PrintWriter writer, String msg) {
		writer.println(msg);
		if(SHOW_OUT)		System.out.println("Srv " + id + ">>SERV (envia): " + msg);
	}
	/**
	 * Metodo que establece el protocolo de comunicacion con el punto de atencion.
	  */

	public void run(){
		try{
			
			PrintWriter writer = new PrintWriter(ss.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(ss.getInputStream()));
			
			//X509Certificate certSer;//Se genera el certificado del servidor
			
			// ////////////////////////////////////////////////////////////////////////
			// Recibe HOLA.
			// En caso de error de formato, cierra la conexion.
			// ////////////////////////////////////////////////////////////////////////
			
			String linea = read(reader);
			
			if (!linea.equals(HOLA)) {
				write(writer, ERROR_FORMATO);
				throw new FontFormatException(linea);
			}
			
			// ////////////////////////////////////////////////////////////////////////
			// Envia el status del servidor y recibe los algoritmos de codificacion
			// ////////////////////////////////////////////////////////////////////////
			write(writer, OK);
			linea = read(reader);
			if (!(linea.contains(SEPARADOR) && linea.split(SEPARADOR)[0].equals(ALGORITMOS))) {
				write(writer, ERROR_FORMATO);
				throw new FontFormatException(linea);
			}
			// Verificar los algoritmos enviados
			String[] algoritmos = linea.split(SEPARADOR);
			// Comprueba y genera la llave simetrica para comunicarse con el
			// servidor.
			
			// Comprueba que el algoritmo asimetrico sea simetrico y reconocido.
			if (!(algoritmos[1].equals(BLOWFISH)
					||algoritmos[1].equals(AES)
					||algoritmos[1].equals(DES)
					||algoritmos[1].equals(RC4))) {
				write(writer, "ERROR: Algoritmo no soportado o no reconocido: " + algoritmos[1] + ". Cerrando conexion");
				throw new NoSuchAlgorithmException();
			}

			// Comprueba que el algoritmo asimetrico sea RSA.
			if (!algoritmos[2].equals(RSA)) {
				write(writer, "ERROR: Algoritmo no soportado o no reconocido: " + algoritmos[1] + ". Cerrando conexion");
				throw new NoSuchAlgorithmException();
			}
			// Comprueba que el algoritmo HMAC sea valido.
			if (!(algoritmos[3].equals(HMACMD5) || algoritmos[3].equals(HMACSHA1) || algoritmos[3]
					.equals(HMACSHA256))) {
				write(writer, "ERROR: Algoritmo no soportado o no reconocido: " + algoritmos[3] + " . Cerrando conexion");
				throw new NoSuchAlgorithmException();
			}

			// Confirmando al cliente que los algoritmos son soportados.
			write(writer, OK);

			// ////////////////////////////////////////////////////////////////////////
			// Recibiendo el certificado del cliente el certificado
			// ////////////////////////////////////////////////////////////////////////
			
			X509Certificate certificadoCliente;
			//Se trata de reconstruir el certificado a partir de la informacion recibida
			try {
				linea = read(reader);
				String strToDecode= "";
				strToDecode+=linea;
					while (!linea.equals("-----END CERTIFICATE-----")) {
						strToDecode+=linea+"\n";
						linea = read(reader);
					}
				strToDecode+=linea;
				StringReader rea = new StringReader(strToDecode);
				PemReader pr = new PemReader(rea);
				PemObject pemcertificadoPuntoAtencion =  pr.readPemObject();
				X509CertificateHolder certHolder = new X509CertificateHolder(pemcertificadoPuntoAtencion.getContent());
				certificadoCliente =  new JcaX509CertificateConverter().getCertificate(certHolder);
				pr.close();
				
			} catch (Exception e) {
				write(writer, RTA + SEPARADOR + ERROR);
				write(writer, e.getMessage());
				e.printStackTrace();
				throw new FontFormatException("Error en el certificado recibido, no se puede decodificar");
				
			}
			

			// ////////////////////////////////////////////////////////////////////////
			// Enviando el certificado del servidor
			// ////////////////////////////////////////////////////////////////////////
			try {
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA, "BC");
				keyPair=keyGen.generateKeyPair();
				//X509Certificate certSer = Seguridad.generateV3Certificate(keyPair);
				//byte[] b;
				Security.addProvider(new BouncyCastleProvider());
				keyGen.initialize(1024);
				X509Certificate cert =Seguridad.generateV3Certificate(keyPair);
				//b = Seguridad.generateV3Certificate(keyPair).getEncoded();
				StringWriter wr = new StringWriter();
			    JcaPEMWriter pemWriter = new JcaPEMWriter(wr);
			    pemWriter.writeObject(cert);
			    pemWriter.flush();
			    pemWriter.close();
			    String certStr= wr.toString();
			    write(writer,certStr);
			} catch (Exception e) {
				//Nunca va a pasar por acá, el certificado del servidor está bien
				e.printStackTrace();
			}	
			
			linea= read(reader);
			linea= read(reader);
			if(!linea.equals(OK))
			{
				System.out.println("Error de confirmación, cerrando conexion: "+linea);
				return;
			}
						

			// ////////////////////////////////////////////////////////////////////////
			// Enviando llave simetrica cifrada con la llave publica del cliente
			// ////////////////////////////////////////////////////////////////////////
			SecretKey llaveSimetrica = Seguridad.keyGenGenerator(algoritmos[1]);
			byte[] cyph = Seguridad.asymmetricEncryption( llaveSimetrica.getEncoded(), certificadoCliente.getPublicKey(),algoritmos[2]);
			//System.out.println("Tam llave original: "+llaveSimetrica.getEncoded().length);
			String llav=Transformacion.codificar(cyph);
			write(writer, llav);		
			
			//////////////////////////////////////////////////////////////////////////
			// Recibe la misma llave simetrica enviada cifrada con la llave publica del servidor.
			// ////////////////////////////////////////////////////////////////////////
			linea = read(reader);
			byte[] simkeyCiph= Transformacion.decodificar(linea);
			byte[] simkeyUnCiph= Seguridad.asymmetricDecryption(simkeyCiph, keyPair.getPrivate(), algoritmos[2]);
			//Se autentica el punto de atencion
			SecretKeySpec llaveSimetricaRecibida = new SecretKeySpec(simkeyUnCiph, algoritmos[1]);
			//System.out.println("Tam llave recibida: "+llaveSimetricaRecibida.getEncoded().length);
			
			if(!llaveSimetrica.getEncoded().equals(llaveSimetricaRecibida.getEncoded()))
			{
				write(writer, OK);
			}
			else
			{
				write(writer,  ERROR);
				write(writer,  "La llave no corresponde con la enviada, cerrando conexión");
				ss.close();
			}
			
			// ////////////////////////////////////////////////////////////////////////
			// Recibe la consulta del cliente y su digest cifrados con la llave simetrica
			// ////////////////////////////////////////////////////////////////////////

			linea = read(reader);
			byte[] cons = Seguridad.symmetricDecryption(Transformacion.decodificar(linea.split(SEPARADOR)[0]), llaveSimetrica, algoritmos[1]);
			String consulta = new String(cons);
			byte[] hash = Seguridad.symmetricDecryption(Transformacion.decodificar(linea.split(SEPARADOR)[1]), llaveSimetrica, algoritmos[1]);
			// Evalua que el HMAC corresponda.
			boolean verificacion = Seguridad.verificarIntegridad(consulta.getBytes(),  llaveSimetrica, algoritmos[3], hash);
			// ////////////////////////////////////////////////////////////////////////
			// Verifica el hash y termina la conexion.
			// ////////////////////////////////////////////////////////////////////////
			if(verificacion)
			{
				String rta= Transformacion.codificar(Seguridad.symmetricEncryption("OK: la consulta ha sido exitosa".getBytes(), llaveSimetrica, algoritmos[1]));
				write(writer, rta );
				
			}
			else 
			{
				String rta= Transformacion.codificar(Seguridad.symmetricEncryption("ERROR: No coinciden la consulta con su digest".getBytes(), llaveSimetrica, algoritmos[1]));
				write(writer, rta );
			}
			System.out.println("Thread " + id + "Terminando\n");
			
			
			
		} catch (NullPointerException e) {
			// Probablemente la conexion fue interrumpida.
			printError(e);
		} catch (IOException e) {
			// Error en la conexion con el cliente.
			printError(e);
		} catch (FontFormatException e) {
			// Si hubo errores en el protocolo por parte del cliente.
			printError(e);
		} catch (NoSuchAlgorithmException e) {
			// Si los algoritmos enviados no son soportados por el servidor.
			printError(e);
		} catch (InvalidKeyException e) {
			// El certificado no se pudo generar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} catch (IllegalStateException e) {
			// El certificado no se pudo generar.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			printError(e);
		} // catch (CertificateNotYetValidException e) {
			// El certificado del cliente no se pudo recuperar.
			// El cliente deberia revisar la creacion y envio de su
			// certificado.
		//	printError(e);
	//	} 
	catch (NoSuchPaddingException e) {
			// Error en el proceso de encripcion de datos del servidor.
		e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// No se pudo generar un sobre digital sobre la llave simetrica.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// No se pudo generar un sobre digital sobre la llave simetrica.
			// No deberia alcanzarce en condiciones normales de ejecuci��n.
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			try {
				ss.close();
			} catch (Exception e) {
				// DO NOTHING
			}
		}
	}


}
