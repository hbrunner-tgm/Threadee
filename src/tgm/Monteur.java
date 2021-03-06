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
    private int[] arm1sorted, arm2sorted, auge1sorted, auge2sorted, kettensorted, rumpfsorted, antennesorted, greifer1sorted, greifer2sorted;
    private SafeWriter sw;
    
	public Monteur(String pfadLog, int id, SafeWriter writer, Lagermitarbeiter lagermitarbeiter,  Sekretariat sekretariat) {
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
        
        this.sw = writer;
	}

	@Override
	public void run()
    {
        this.zusammenbauen();

        try
        {
            Thread.sleep(20);
        }
        catch (InterruptedException e)
        {
            logger.log(Level.WARNING, "Thread wurde im Schlaf unterbrochen!");
        }
	}
	
	public void zusammenbauen() {
		//Zusammen bauen den Threedeas
        String[] arrayarm1, arrayarm2, arrayauge1, arrayauge2, arrayketten, arrayrumpf, arrayantenne, arraygreifer1, arraygreifer2;

        String arm1 = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_ARM);
        logger.log(Level.INFO, "Hole 1. Arm aus dem Lager");
        if(arm1 == null)
        {
            logger.log(Level.WARNING, "Keine Arme vorhanden, breche ab");
            return;
        }
        String arm2 = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_ARM);
        logger.log(Level.INFO, "Hole 2. Arm aus dem Lager");
        if(arm2 == null)
        {
            logger.log(Level.WARNING, "Kein 2. Arm vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            return;
        }
        String auge1 = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_AUGE);
        logger.log(Level.INFO, "Hole 1. Auge aus dem Lager");
        if(auge1 == null)
        {
            logger.log(Level.WARNING, "Kein 1. Auge vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            return;
        }
        String auge2= this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_AUGE);
        logger.log(Level.INFO, "Hole 2. Auge aus dem Lager");
        if(auge2 == null)
        {
            logger.log(Level.WARNING, "Kein 2. Auge vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(auge1, Lagermitarbeiter.ETeil.TEIL_AUGE);
            return;
        }
        String kettenantrieb = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB);
        logger.log(Level.INFO, "Hole Kettenantrieb aus dem Lager");
        if(kettenantrieb == null)
        {
            logger.log(Level.WARNING, "Kein Kettenantrieb vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(auge1, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(auge2, Lagermitarbeiter.ETeil.TEIL_AUGE);
            return;
        }
        String rumpf = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_RUMPF);
        logger.log(Level.INFO, "Hole Rumpf aus dem Lager");
        if(rumpf == null)
        {
            logger.log(Level.WARNING, "Kein Rumpf vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(auge1, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(auge2, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(kettenantrieb, Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB);
            return;
        }
        String antenne = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_ANTENNE);
        logger.log(Level.INFO, "Hole Antenne aus dem Lager");
        if(rumpf == null)
        {
            logger.log(Level.WARNING, "Keine Antenne vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(auge1, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(auge2, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(kettenantrieb, Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB);
            this.lagermitarbeiter.zuruckLegen(rumpf, Lagermitarbeiter.ETeil.TEIL_RUMPF);
            return;
        }
        String greifer1 = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_GREIFER);
        logger.log(Level.INFO, "Hole 1. Greifer aus dem Lager");
        if(greifer1 == null)
        {
            logger.log(Level.WARNING, "Kein 1. Greifer vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(auge1, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(auge2, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(kettenantrieb, Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB);
            this.lagermitarbeiter.zuruckLegen(rumpf, Lagermitarbeiter.ETeil.TEIL_RUMPF);
            this.lagermitarbeiter.zuruckLegen(antenne, Lagermitarbeiter.ETeil.TEIL_ANTENNE);
            return;
        }
        String greifer2 = this.lagermitarbeiter.getTeil(Lagermitarbeiter.ETeil.TEIL_GREIFER);
        logger.log(Level.INFO, "Hole 2. Greifer aus dem Lager");
        if(greifer2 == null)
        {
            logger.log(Level.WARNING, "Kein 2. Greifer vorhanden, lege zurück");
            this.lagermitarbeiter.zuruckLegen(arm1, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(arm2, Lagermitarbeiter.ETeil.TEIL_ARM);
            this.lagermitarbeiter.zuruckLegen(auge1, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(auge2, Lagermitarbeiter.ETeil.TEIL_AUGE);
            this.lagermitarbeiter.zuruckLegen(kettenantrieb, Lagermitarbeiter.ETeil.TEIL_KETTENANTRIEB);
            this.lagermitarbeiter.zuruckLegen(rumpf, Lagermitarbeiter.ETeil.TEIL_RUMPF);
            this.lagermitarbeiter.zuruckLegen(antenne, Lagermitarbeiter.ETeil.TEIL_ANTENNE);
            this.lagermitarbeiter.zuruckLegen(greifer1, Lagermitarbeiter.ETeil.TEIL_GREIFER);
            return;
        }

        arrayarm1 = arm1.split(",");
        arrayarm2 = arm2.split(",");
        arrayauge1 = auge1.split(",");
        arrayauge2 = auge2.split(",");
        arrayketten= kettenantrieb.split(",");
        arrayrumpf= rumpf.split(",");
        arrayantenne= antenne.split(",");
        arraygreifer1= greifer1.split(",");
        arraygreifer2= greifer2.split(",");

        arm1sorted= new int[arrayarm1.length-1];
        arm2sorted= new int[arrayarm2.length-1];
        auge1sorted= new int[arrayauge1.length-1];
        auge2sorted = new int[arrayauge2.length-1];
        kettensorted= new int[arrayketten.length-1];
        rumpfsorted= new int[arrayrumpf.length-1];
        antennesorted = new int[arrayantenne.length-1];
        greifer1sorted = new int[arraygreifer1.length-1];
        greifer2sorted = new int[arraygreifer2.length-1];

        for(int i=1;i<arm1sorted.length;i++) {
            arm1sorted[i]= Integer.parseInt(arrayarm1[i]);
        }
        for(int i=1;i<arm2sorted.length;i++) {
            arm2sorted[i]= Integer.parseInt(arrayarm2[i]);
        }
        for(int i=1;i<auge1sorted.length;i++) {
            auge1sorted[i]= Integer.parseInt(arrayauge1[i]);
        }
        for(int i=1;i<auge2sorted.length;i++) {
            auge2sorted[i]= Integer.parseInt(arrayauge2[i]);
        }
        for(int i=1;i<kettensorted.length;i++) {
            kettensorted[i]= Integer.parseInt(arrayketten[i]);
        }
        for(int i=1;i<rumpfsorted.length;i++) {
            rumpfsorted[i]= Integer.parseInt(arrayrumpf[i]);
        }
        for(int i=1;i<antennesorted.length;i++) {
            antennesorted[i]= Integer.parseInt(arrayantenne[i]);
        }
        for(int i=1;i<greifer1sorted.length;i++) {
            greifer1sorted[i]= Integer.parseInt(arraygreifer1[i]);
        }
        for(int i=1;i<greifer2sorted.length;i++) {
            greifer2sorted[i]= Integer.parseInt(arraygreifer2[i]);
        }

        //Sortieren

        Arrays.sort(arm1sorted);
        Arrays.sort(arm2sorted);
        Arrays.sort(auge1sorted);
        Arrays.sort(auge2sorted);
        Arrays.sort(kettensorted);
        Arrays.sort(rumpfsorted);
        Arrays.sort(antennesorted);
        Arrays.sort(greifer1sorted);
        Arrays.sort(greifer2sorted);

        String output = "Threadee-ID"+this.sekretariat.getNextThreadee()+",Mitarbeiter-ID"+this.id+",";
        String auge1fertig, auge2fertig, arm1fertig, arm2fertig, rumpffertig, kettenantriebfertig, antennefertig, greifer1fertig, greifer2fertig;

        auge1fertig = "Auge,";
        for(int i = 0; i < auge1sorted.length; i++)
        {
            auge1fertig += auge1sorted[i];
            if(i!=(auge1sorted.length-1)) auge1fertig += ",";
        }
        auge2fertig = "Auge,";
        for(int i = 0; i < auge2sorted.length; i++)
        {
            auge2fertig += auge2sorted[i];
            if(i!=(auge2sorted.length-1)) auge2fertig += ",";
        }
        arm1fertig = "Arm,";
        for(int i = 0; i < arm1sorted.length; i++)
        {
            arm1fertig += arm1sorted[i];
            if(i!=(arm1sorted.length-1)) arm1fertig += ",";
        }
        arm2fertig = "Arm,";
        for(int i = 0; i < arm2sorted.length; i++)
        {
            arm2fertig += arm2sorted[i];
            if(i!=(arm2sorted.length-1)) arm2fertig += ",";
        }
        rumpffertig = "Rumpf,";
        for(int i = 0; i < rumpfsorted.length; i++)
        {
            rumpffertig += rumpfsorted[i];
            if(i!=(rumpfsorted.length-1)) rumpffertig += ",";
        }
        kettenantriebfertig = "Kettenantrieb,";
        for(int i = 0; i < kettensorted.length; i++)
        {
            kettenantriebfertig += kettensorted[i];
            if(i!=(kettensorted.length-1)) kettenantriebfertig += ",";
        }
        antennefertig = "Antenne,";
        for(int i = 0; i < antennesorted.length; i++)
        {
            antennefertig += antennesorted[i];
            if(i!=(antennesorted.length-1)) antennefertig += ",";
        }
        greifer1fertig = "Greifer,";
        for(int i = 0; i < greifer1sorted.length; i++)
        {
            greifer1fertig += greifer1sorted[i];
            if(i!=(greifer1sorted.length-1)) greifer1fertig += ",";
        }
        greifer2fertig = "Greifer,";
        for(int i = 0; i < greifer2sorted.length; i++)
        {
            greifer2fertig += greifer2sorted[i];
            if(i!=(greifer2sorted.length-1)) greifer2fertig += ",";
        }

        output += auge1fertig;
        output += ",";
        output += auge2fertig;
        output += ",";
        output += arm1fertig;
        output += ",";
        output += arm2fertig;
        output += ",";
        output += rumpffertig;
        output += ",";
        output += kettenantriebfertig;
        output += ",";
        output += antennefertig;
        output += ",";
        output += greifer1fertig;
        output += ",";
        output += greifer2fertig;

        if(!this.sw.writeLine(output)) logger.log(Level.SEVERE, "Failed writing data!");
        logger.log(Level.INFO, "Neues Threadee erzeugt: '"+output+"'");
	}

    @Override
    public void stop()
    {
    	this.running=false;
    	logger.log(Level.INFO, "Monteur arbeit beendet!");
    }
}
