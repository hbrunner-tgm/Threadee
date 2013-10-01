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
	}

    @Override
    public void stop() {
    }
}
