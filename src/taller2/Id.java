package taller2;

public class Id {
	private int id = 0;
	public int darId(){
		if(id>=T.nFilas)
			return -1;
		return id++;
	}
}
