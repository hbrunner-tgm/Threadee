package tgm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hauptklasse, welche alle Arbeiter initialisiert und startet
 *
 * @author Andreas Willinger
 * @version 0.1
 */

public class Sekretariat
{
    private HashMap<Object, Integer> monteurID;

    // Threads & Thread-Pools
    private Lagermitarbeiter lagermitarbeiter;
    private ThreadPoolExecutor executerMonteur, executerLieferant, executerWatchdog;
    private int anzahlMon, anzahlLief;
    private long zeit;
    private int mid;

	/**
	 * Alle benötigten Variablen dem Sekretariat zuordnen und Pools initialisieren
     *
	 * @param pfadLager der Pfad zum Lagerfile
	 * @param pfadLog der Pfad zum Logfile
	 * @param anzahlMon die Anzahl der Montuere
	 * @param anzahlLief die Anzahl der Lieferanten
	 * @param zeit wie lange das Programm laufen soll
	 */
	public Sekretariat(String pfadLager, String pfadLog, int anzahlMon, int anzahlLief, long zeit) {
        this.lagermitarbeiter = new Lagermitarbeiter(pfadLager);
        this.monteurID = new HashMap<Object, Integer>();

        this.executerLieferant = new ThreadPoolExecutor(anzahlLief, anzahlLief, zeit, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(anzahlLief));
        this.executerMonteur = new ThreadPoolExecutor(anzahlMon, anzahlMon, zeit, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(anzahlMon));
        this.executerWatchdog = new ThreadPoolExecutor(anzahlLief+anzahlMon, anzahlLief+anzahlMon+1, zeit, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(anzahlLief+anzahlMon));

        this.anzahlLief = anzahlLief;
        this.anzahlMon = anzahlMon;
        this.zeit = zeit;
	}

    /**
     * Alle Pools starten
     */
    public void startWork()
    {
        ArrayList<Runnable> watchdogs = new ArrayList<Runnable>();
        watchdogs.add(new WatchDog(this.lagermitarbeiter, this.zeit));

        Thread tLagermitarbeiter = new Thread(this.lagermitarbeiter);
        tLagermitarbeiter.start();

        // Lieferanten Thread-Pool starten
        for(int i = 0; i < this.anzahlLief; i++)
        {
            Lieferant lieferant = new Lieferant(this.lagermitarbeiter);
            executerLieferant.execute(lieferant);
            watchdogs.add(new WatchDog(lieferant, this.zeit));
        }

        // Monteur Thread-Pool starten
        int mId = 1;
        for(int i = 0; i < this.anzahlMon; i++)
        {
            Monteur monteur = new Monteur(mId, this.lagermitarbeiter);
            executerMonteur.execute(monteur);
            watchdogs.add(new WatchDog(monteur, this.zeit));
            mId++;
        }

        // WatchDoG Thread-Pool starten
        for(int i = 0; i <watchdogs.size(); i++)
        {
            executerWatchdog.execute(watchdogs.get(i));
        }
    }
}
