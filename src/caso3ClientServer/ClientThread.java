/*
 * Decompiled with CFR 0_118.
 */
package caso3ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

// TODO Server
public class ClientThread
extends Thread
implements Runnable {
    private Socket client;
    private InputStream inS;
    private OutputStream outS;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(Socket clientP) {
        this.client = clientP;
        try {
            this.inS = this.client.getInputStream();
            this.outS = this.client.getOutputStream();
            this.in = new BufferedReader(new InputStreamReader(this.inS));
            this.out = new PrintWriter(this.outS, true);
        }
        catch (Exception e) {
            System.out.println("Fail Opening the Client Socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            boolean connected = true;
            while (connected) {
                String clientMessage = this.in.readLine();
                System.out.println("Message From Client Recived: " + clientMessage);
                this.out.println("ACK");
                if (!clientMessage.equalsIgnoreCase("EOT")) continue;
                connected = false;
            }
        }
        catch (IOException e) {
            System.out.println("Fail to Read the Message from the Client: " + e.getMessage());
        }
    }
}

