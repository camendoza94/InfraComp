/*
 * Decompiled with CFR 0_118.
 */
package caso3ClientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import caso3ClientServer.ClientThread;

//TODO Mirar la clase Servidor de SerivodorNovasoft y hacer los cambios respectivos
public class Server {
    private ServerSocket server;

    public Server() {
        try {
            this.server = new ServerSocket(4443);
        }
        catch (IOException e) {
            System.out.println("Fail Opening the Server t Socket: " + e.getMessage());
        }
        this.listenClients();
    }

    public void listenClients() {
        System.out.println("Server Running ...");
        try {
            do {
                Socket clientScoket = this.server.accept();
                System.out.println("Conection Accepted!");
                ClientThread ct = new ClientThread(clientScoket);
                ct.start();
            } while (true);
        }
        catch (IOException e) {
            System.out.println("Fail Connecting to the Client: " + e.getMessage());
            return;
        }
    }

    public static /* varargs */ void main(String ... args) {
        new Server();
    }
}

