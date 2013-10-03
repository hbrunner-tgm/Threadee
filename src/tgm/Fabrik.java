package tgm;

/**
 * Startklasse.
 * Argumente an den Stellen: 0= Pfad zum File, 1= Pfad zur Logdatei, 2= Anzahl der Lieferanten, 3= Anzahl der Montuere, 4= Laufzeit in ms
 * @author helmuthbrunner
 */

public class Fabrik
{
	public static void main(String[] args)
    {
        CommandLineParser parser = new CommandLineParser(args);
        parser.parse();
	}
}
