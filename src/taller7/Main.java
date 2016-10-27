package taller7;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.crypto.SecretKey;

public class Main {
	public static void main(String[] args) {
		Encriptador e = new Encriptador();
		byte[] cifrada = e.cifrar();
		try {
			FileOutputStream farch = new FileOutputStream("datoCifrado");
			ObjectOutputStream oos = new ObjectOutputStream(farch);
			oos.writeObject(cifrada);
			oos.close();
			FileOutputStream farch2 = new FileOutputStream("llave");
			ObjectOutputStream oos2 = new ObjectOutputStream(farch2);
			oos2.writeObject(e.getKey());
			oos2.close();
			// Recuperando texto cifrado
			FileInputStream input = new FileInputStream("datoCifrado");
			ObjectInputStream ois = new ObjectInputStream(input);
			byte cipheredText[] = (byte[]) ois.readObject();
			ois.close();
			FileInputStream input2 = new FileInputStream("llave");
			ObjectInputStream ois2 = new ObjectInputStream(input2);
			SecretKey llave = (SecretKey) ois2.readObject();
			ois2.close();
			e.setKey(llave);
			e.descifrar(cipheredText);
		} catch (Exception ex) {
		}
	}
}
