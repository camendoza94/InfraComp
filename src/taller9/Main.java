package taller9;

public class Main {
	public static void main(String[] args) {
		Asimétrico e = new Asimétrico();
		byte[] cifrada = e.cifrar();
		e.descifrar(cifrada);
	}
}
