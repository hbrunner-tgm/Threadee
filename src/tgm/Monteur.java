package tgm;

import tgm.Lagermitarbeiter.NoItemsException;

public class Monteur implements Runnable {
	
	private int id;
	private Lagermitarbeiter l;
	
	public Monteur(int id) {
		this.id=id;
	}

	@Override
	public void run() {
		for(int i=0;i<4;i++) {
			try {
				l.getTeile(i);
			} catch (NoItemsException e) {
				System.out.println("Keine Teile mehr!");
			}
		}
	}
	
}
