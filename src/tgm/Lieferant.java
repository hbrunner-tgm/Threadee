package tgm;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Lieferant implements Stoppable
{
    private final static Logger logger = Logger.getLogger("Lieferant");
    private Lagermitarbeiter lagermitarbeiter;

    public Lieferant(String pfadLog, Lagermitarbeiter lagermitarbeiter)
    {
        this.lagermitarbeiter = lagermitarbeiter;

        pfadLog += "lieferant.log";
        try
        {
            File f = new File(pfadLog);
            if(!f.exists()) f.createNewFile();

            FileHandler fh = new FileHandler(pfadLog);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);

        }
        catch (IOException e)
        {
            System.out.println("File logging disabled");
        }
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
