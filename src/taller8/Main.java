package taller8;

public class Main {
	public static void main(String[] args) {
		Digest d = new Digest();
		byte[] codigo1 = d.calcular();
		System.out.println(d.verificar(codigo1));
	}
	
}
