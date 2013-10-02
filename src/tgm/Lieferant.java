package tgm;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Lieferant implements Stoppable
{
	private final static Logger logger = Logger.getLogger("Lieferant");
	private Lagermitarbeiter lagermitarbeiter;
	private boolean running;
	private Random r;

	public Lieferant(String pfadLog, Lagermitarbeiter lagermitarbeiter)
	{
		this.lagermitarbeiter = lagermitarbeiter;
		this.running=true;
		this.r= new Random();

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

		while(true) {
			
			switch(r.nextInt(4)+1)
			{
			case 1:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_ARM, this.pgnr());
			case 2:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_AUGE, this.pgnr());
			case 3:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB, this.pgnr());
			case 4:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_RUMPF, this.pgnr());
			}
			
			try {
				Thread.sleep(this.getNextDeliveryTime());
			} catch (InterruptedException e) {
				logger.log(Level.WARNING, "Thread wurde im Schlaf unterbrochen!");
			}
			
		}

	}

	/**
	 * Eine Methode die Random-Numbers genriert f??r das LagerFile
	 * @return als Text die Random-Numbers
	 */
	public String pgnr() {
		r.setSeed(78160);
		String str="";

		int[] array= new int[20];

		for(int i=0;i<array.length;i++) {
			if(i!=array.length-1)
				str=str+ r.nextInt(200)+",";
			else
				str=str+ r.nextInt(200);
		}

		return str;
	}

	/**
	 * Liefert die Zeit die dann den Thread kurz pausiert.
	 * @return die neue Zeit
	 */
	private long getNextDeliveryTime() {
		long out;
		do{
			out= (long) r.nextInt(2000);
		}while(out<100 && out % 10 >0);
		return out;
	}
	
	public void stop() {
		this.running = false;
		logger.log(Level.INFO, "Lieferant arbeit beendet!");
	}
}
