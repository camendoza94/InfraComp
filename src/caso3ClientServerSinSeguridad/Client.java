/*
 * Decompiled with CFR 0_118.
 */
package caso3ClientServerSinSeguridad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InputStream inS;
    private OutputStream outS;
    private BufferedReader in;
    private PrintWriter out;

    public Client() {
        try {
            this.socket = new Socket("localhost", 4444);
            this.inS = this.socket.getInputStream();
            this.outS = this.socket.getOutputStream();
            this.in = new BufferedReader(new InputStreamReader(this.inS));
            this.out = new PrintWriter(this.outS, true);
        }
        catch (Exception e) {
            System.out.println("Fail Opening de Client Socket: " + e.getMessage());
        }
    }

    public synchronized void sendMessageToServer(String message) {
        this.out.println(message);
    }

    public synchronized String waitForMessageFromServer() {
        try {
            String answer = this.in.readLine();
            System.out.println("Client - Message: " + answer);
            return answer;
        }
        catch (IOException e) {
            System.out.println("Fail to Listen ACK from Server: " + e.getMessage());
            return null;
        }
    }
}

