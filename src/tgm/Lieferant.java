package tgm;

public class Lieferant implements Stoppable {

    private Lagermitarbeiter lagermitarbeiter;

    public Lieferant(Lagermitarbeiter lagermitarbeiter)
    {
        this.lagermitarbeiter = lagermitarbeiter;
    }

	@Override
	public void run() {
		
	}

    public void stop()
    {
    }
}
