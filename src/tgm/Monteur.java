package tgm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Monteur implements Stoppable
{
	private final static Logger logger = Logger.getLogger("Monteur");
    private int id;
	private Lagermitarbeiter lagermitarbeiter;
    private Sekretariat sekretariat;
    private boolean running;
    private int[] armsorted, augesorted, kettensorted, rumpfsorted;

	public Monteur(String pfadLog, int id, Lagermitarbeiter lagermitarbeiter, Sekretariat sekretariat) {
		this.id = id;
        this.lagermitarbeiter = lagermitarbeiter;
        this.sekretariat = sekretariat;
        this.running=true;

        pfadLog += "monteur.log";
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
	public void run()
    {
		
		String[] arrayarm, arrayauge, arrayketten, arrayrumpf;
		
		while(true)
		{
			
			arrayarm= this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_ARM).split(",");
			arrayauge= this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_AUGE).split(",");
			arrayketten= this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB).split(",");
			arrayrumpf= this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_RUMPF).split(",");

			
			armsorted= new int[arrayarm.length-1];
			augesorted= new int[arrayauge.length-1];
			kettensorted= new int[arrayketten.length-1];
			rumpfsorted= new int[arrayrumpf.length-1];
			
			for(int i=1;i<armsorted.length;i++) {
				armsorted[i]= Integer.parseInt(arrayarm[i]);
			}
			for(int i=1;i<augesorted.length;i++) {
				augesorted[i]= Integer.parseInt(arrayauge[i]);
			}
			for(int i=1;i<kettensorted.length;i++) {
				kettensorted[i]= Integer.parseInt(arrayketten[i]);
			}
			for(int i=1;i<rumpfsorted.length;i++) {
				rumpfsorted[i]= Integer.parseInt(arrayrumpf[i]);
			}
			
			//Sortieren
			
			Arrays.sort(armsorted);
			Arrays.sort(augesorted);
			Arrays.sort(kettensorted);
			Arrays.sort(rumpfsorted);
			
		}
	}
	
	public void zusammenbauen() {
		//Zusammen baue den Threedeas
	}

    @Override
    public void stop()
    {
    	this.running=false;
    	logger.log(Level.INFO, "Monteur arbeit beendet!");
    }
}
