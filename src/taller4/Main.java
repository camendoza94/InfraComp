package taller4;

public class Main {
	public static void main(String[] args) {
		Canal c = new Canal();
		new Consumidor(c).start();

		new Productor(c).start();
	}
}
