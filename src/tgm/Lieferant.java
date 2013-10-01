package tgm;

public class Lieferant implements Stoppable {

    private Lagermitarbeiter lagermitarbeiter;

    public Lieferant(Lagermitarbeiter lagermitarbeiter)
    {
        this.lagermitarbeiter = lagermitarbeiter;
    }

	@Override
	public void run() {
        /*

        BEISPIEL:

        this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_ARM, "ARM");
        String teil = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_ARM);
        System.out.println(teil);
        this.lagermitarbeiter.zuruckLegen(teil, Lagermitarbeiter.ETeil.TEIL_ARM);

        while(this.running)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {

            }
        }
        System.out.println("*** KILLED (LIEFERANT)");

         */
	}

    public void stop()
    {
    }
}
