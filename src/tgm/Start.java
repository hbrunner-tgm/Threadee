package tgm;

/**
 * Startklasse.
 * Argumente an den Stellen: 0= Pfad zum File, 1= Pfad zur Logdatei, 2= Anzahl der Lieferanten, 3= Anzahl der Montuere, 4= Laufzeit in ms
 * @author helmuthbrunner
 */

public class Start
{
	public static void main(String[] args)
    {
        String[] attr = new String[]{"--lager","E:/Schule/4. Jahrgang/SEW/Praxis/lager/","--logs","E:/Schule/4. Jahrgang/SEW/Praxis/logs/","--lieferanten","15","--monteure","25","--laufzeit","10000"};

        CommandLineParser parser = new CommandLineParser(attr);
        parser.parse();
	}
}
