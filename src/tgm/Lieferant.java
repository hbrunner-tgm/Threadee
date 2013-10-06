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
		while(this.running) {
			
			switch(r.nextInt(6)+1)
			{
			case 1:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_ARM, this.pgnr("Arm"));
                logger.log(Level.INFO, "Neuer Arm wurde angeliefert!");
			case 2:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_AUGE, this.pgnr("Auge"));
                logger.log(Level.INFO, "Neues Auge wurde angeliefert!");
			case 3:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB, this.pgnr("Kettenantrieb"));
                logger.log(Level.INFO, "Neuer Kettenantrieb wurde angeliefert!");
			case 4:
				this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_RUMPF, this.pgnr("Rumpf"));
                logger.log(Level.INFO, "Neuer Rumpf wurde angeliefert!");
            case 5:
                this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_ANTENNE, this.pgnr("Antenne"));
                logger.log(Level.INFO, "Neue Antenne wurde angeliefert!");
            case 6:
                this.lagermitarbeiter.addTeil(Lagermitarbeiter.ETeil.TEIL_GREIFER, this.pgnr("Greifer"));
                logger.log(Level.INFO, "Neuer Greifer wurde angeliefert!");
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.log(Level.WARNING, "Thread wurde im Schlaf unterbrochen!");
			}
			
		}

	}

	/**
	 * Eine Methode die Random-Numbers genriert f??r das LagerFile
	 * @return als Text die Random-Numbers
	 */
	public String pgnr(String type) {
		String str=type+",";

		int[] array= new int[20];

		for(int i=0;i<array.length;i++) {
			if(i!=array.length-1)
				str=str+ r.nextInt(200)+",";
			else
				str=str+ r.nextInt(200);
		}

		return str;
	}
	
	public void stop() {
		this.running = false;
		logger.log(Level.INFO, "Lieferant arbeit beendet!");
	}
}
