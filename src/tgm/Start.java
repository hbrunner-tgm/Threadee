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
		String pfadLager= args[0], pfadLog= args[1];
		int lief= Integer.parseInt(args[2]), mon= Integer.parseInt(args[3]), lauf= Integer.parseInt(args[4]);
		
		Sekretariat r= new Sekretariat(pfadLager, pfadLog, mon, lief, lauf);
		
	}
}
