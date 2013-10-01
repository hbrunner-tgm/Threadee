package tgm;

public class Monteur implements Stoppable {
	
	private int id;
	private Lagermitarbeiter l;
	
	public Monteur(int id, Lagermitarbeiter lagermitarbeiter) {
		this.id=id;
        this.l = lagermitarbeiter;
	}

	@Override
	public void run() {
		for(int i=0;i<4;i++) {
                //TODO: Implementieren
				//l.getTeile(i);
		}
	}

    @Override
    public void stop() {

    }
}
