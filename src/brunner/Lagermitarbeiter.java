package brunner;

public class Lagermitarbeiter implements Runnable {

	private String path;
	
	/**
	 * 
	 * @param path der Pfad zu der Datei wo die Teile liegen
	 */
	public Lagermitarbeiter(String path) {
		this.path=path;
	}
	
	@Override
	public void run() {
		
	}

}
