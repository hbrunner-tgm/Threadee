package tgm;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Monteur implements Stoppable
{
	private final static Logger logger = Logger.getLogger("Monteur");
    private int id;
	private Lagermitarbeiter lagermitarbeiter;
    private Sekretariat sekretariat;

	public Monteur(String pfadLog, int id, Lagermitarbeiter lagermitarbeiter, Sekretariat sekretariat) {
		this.id = id;
        this.lagermitarbeiter = lagermitarbeiter;
        this.sekretariat = sekretariat;

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
	}

    @Override
    public void stop()
    {
    }
}
