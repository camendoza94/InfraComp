/**
 * 
 */
package caso3ClientServerSinSeguridad;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import caso3GeneratorSinSeguridad.Generator;

/**
 * Esta clase implementa el servidor que atiende a los clientes. El servidor 
 * esta implemntado como un pool de threads. Cada vez que un cliente crea
 * una conexion al servidor, un thread se encarga de atenderlo el tiempo que
 * dure la sesion. 
 * Infraestructura Computacional Universidad de los Andes. 
 * Las tildes han sido eliminadas por cuestiones de compatibilidad.
 * 
 * @author Cristian Fabián Brochero Rodríguez-  201620
 */
public class Servidor {

	/**
	 * Constante que especifica el tiempo máximo en milisegundos que se esperara 
	 * por la respuesta de un cliente en cada una de las partes de la comunicación
	 */
	private static final int TIME_OUT = 10000;

	/**
	 * Constante que especifica el numero de threads que se usan en el pool de conexiones.
	 */
	public static final int N_THREADS = 1;

	/**
	 * Puerto en el cual escucha el servidor. 
	 |*/
	public static final int PUERTO = 4444;

	/**
	 * El socket que permite recibir requerimientos por parte de clientes.
	 */
	private static ServerSocket elSocket;
	private static Servidor elServidor;

	/**
	 * Metodo main del servidor con seguridad que inicializa un 
	 * pool de threads determinado por la constante nThreads.
	 * @param args Los argumentos del metodo main (vacios para este ejemplo).
	 * @throws IOException Si el socket no pudo ser creado.
	 */
	private ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
	public static void main(String[] args) throws IOException {
		elServidor = new Servidor();
		elServidor.runServidor();
	}
	
	private void runServidor() {

		int num = 0;
		int fallos = 0;
		try {
			// Crea el socket que escucha en el puerto seleccionado.
			elSocket = new ServerSocket(PUERTO);
			System.out.println("Servidor Coordinador escuchando en puerto: " + PUERTO);
			while (true) {
				Socket sThread = null;
				// ////////////////////////////////////////////////////////////////////////
				// Recibe conexiones de los clientes
				// ////////////////////////////////////////////////////////////////////////
				sThread = elSocket.accept();
				sThread.setSoTimeout(TIME_OUT);
				System.out.println("Thread " + num + " recibe a un cliente.");
				executor.submit(new Worker(num,sThread));
				num++;
			}
		} catch (Exception e) {
			fallos++;
			BufferedWriter buffer;
			try {
				buffer = new BufferedWriter(new FileWriter(
						Servidor.N_THREADS + "threads" + Generator.numberOfTasks +"FallosSS.txt", true));

				PrintWriter writer = new PrintWriter(buffer);
				writer.println("Error N�" + fallos + " " + e.getMessage());
				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				// No deberia alcanzarse en condiciones normales de ejecucion.
				e.printStackTrace();
		}
	}



}
