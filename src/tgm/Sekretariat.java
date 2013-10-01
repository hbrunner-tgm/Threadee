package tgm;

/**
 * Eine Klasse die alle eingabe Parmeter uebernimmt und die einzelen Threads startet.
 * @author helmuthbrunner
 */

public class Sekretariat {

	private String pfadLager, pfadLog;
	private int anzahlMon, anzahlLief;
	private long zeit;
	private int id;

	/**
	 * Default-Konstruktor fuesr die Klasse Sekretariat.
	 */
	public Sekretariat() {
		this.pfadLager= pfadLager;
		this.pfadLog= pfadLog;
		this.anzahlMon= 25;
		this.anzahlLief=12;
		this.zeit=25l;
	}

	/**
	 * Ein Konstruktor der Klasse Sekretariat der alle Parameter uebernimmt.
	 * @param pfadLager der Pfad zum Lagerfile
	 * @param pfadLog der Pfad zum Logfile
	 * @param anzahlMon die Anzahl der Montuere
	 * @param anzahlLief die Anzahl der Lieferanten
	 * @param zeit wie lange das Programm laufen soll
	 */
	public Sekretariat(String pfadLager, String pfadLog, int anzahlMon, int anzahlLief, long zeit) {
		this.setPfadLager(pfadLager);
		this.setPfadLog(pfadLog);
		this.setAnzahlMon(anzahlMon);
		this.setAnzahlLief(anzahlLief);
		this.setZeit(zeit);
	}

	/**
	 * Erstell eine neue Mitarbeiter ID
	 */
	public void newID() {
		id++;
	}
	
	/**
	 * Gibt die ID zurueck.
	 */
	public int getID() {
		return id;
	}
	
	public void startWorking() {
		/*
		 * Alle Threads erzeugen und Starten.
		 */
	}

	/**
	 * Gibt den Pfad zur Lagerdatei zurueck.
	 * @return den Pfad
	 */
	public String getPfadLager() {
		return pfadLager;
	}

	/**
	 * Eine Setter-Methode um den Pfad zum Lagerfile zusetzen.
	 * @param pfadLager Pfad zur Lagerdatei, darf nicht null oder kleiner als 1 sein
	 */
	public void setPfadLager(String pfadLager) {
		if(pfadLager!=null && pfadLager.length()>=1) {
			this.pfadLager = pfadLager;
		}
	}

	/**
	 * Gibt den Pfad zur Logdatei zurueck.
	 * @return den Pfad
	 */
	public String getPfadLog() {
		return pfadLog;
	}

	/**
	 * Eine Setter-Methode um den Pfad zum Logfile zusetzen.
	 * @param pfadLog Pfad zur Logdatei, darf nicht null oder kleiner als 1 sein
	 */
	public void setPfadLog(String pfadLog) {
		if(pfadLog!=null && pfadLager.length()>=1) {
			this.pfadLog = pfadLog;
		}
	}

	/**
	 * Gibt die Anzahl der Montuere zurueck.
	 * @return die Anzahl
	 */
	public int getAnzahlMon() {
		return anzahlMon;
	}

	/**
	 * Eine Setter-Methode um die Anzahl der Montuere zusetzen.
	 * @param anzahlMon die Anzahl, darf nicht kleiner als 0 sein
	 */
	public void setAnzahlMon(int anzahlMon) {
		if(anzahlMon>=0) {
			this.anzahlMon = anzahlMon;
		}
	}

	/**
	 * Gibt die Anzahl der Lieferant zurueck.
	 * @return die Anzahl
	 */
	public int getAnzahlLief() {
		return anzahlLief;
	}

	/**
	 * Eine Setter-Methode fuer die Lieferant.
	 * @param anzahlLief
	 */
	public void setAnzahlLief(int anzahlLief) {
		if(anzahlLief>=1) {
			this.anzahlLief = anzahlLief;
		}
	}
	
	/**
	 * Gibt die Zeit zurueck wie lange Threedeas produziert werden.
	 * @return die Zeit in Millisekunden
	 */
	public long getZeit() {
		return zeit;
	}

	/**
	 * Eine Setter-Methode die die Zeit setzt
	 * @param zeit in Millisekunden, dart nicht kleiner als 1 sein.
	 */
	public void setZeit(long zeit) {
		if(zeit>=1) {
			this.zeit = zeit;
		}
	}
}
