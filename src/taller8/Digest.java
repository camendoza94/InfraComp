package taller8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;

public class Digest {
	private byte[] getKeyedDigest(byte[] buffer) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(buffer);
			return md5.digest();
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] calcular() {
		try {
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String dato = stdIn.readLine();
			byte[] text = dato.getBytes();
			String s1 = new String(text);
			System.out.println("dato original: " + s1);
			byte[] digest = getKeyedDigest(text);
			String s2 = new String(digest);
			System.out.println("digest: " + s2);
			return digest;
		} catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
	}
	
	public boolean verificar(byte[] codigo){
		return Arrays.equals(codigo, calcular());
	}
}
