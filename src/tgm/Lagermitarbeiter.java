package tgm;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Lagermitarbeiter {

	private String path;
	private RandomAccessFile r;
	private List[] l;

	/**
	 * 
	 * @param path der Pfad zu der Datei wo die Teile liegen
	 */
	public Lagermitarbeiter(String path) {
		this.path=path;
		l= new List[4];
		for(int i=0;i<0;i++) {
			l[i]= new List();
		}
	}

	/**
	 * Sycronisiert die List mit den LagerFiels
	 * @param type den Type des Teile
	 */
	public void setAllToList(int type) {
		String zd= "" + this.path;
		try {
			switch(type) {
			case 0:
				zd+= "/Auge.csv";
				break;
			case 1:
				zd+= "/Rumpf.csv";
				break;
			case 2:
				zd+= "/Ketten.csv";
				break;
			case 3:
				zd+= "/Arm.csv";
				break;
			}
			
			r= new RandomAccessFile(zd, "rw");
		
			for(int i=0;i<l[type].getItemCount();i++) {
				try {
					r.writeUTF(l[type].getItem(i)+",");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Der auf eine bestimmten Typ von Teil im Lagerverweist.
	 * 0 = Auge
	 * 1 = Rumpf
	 * 2 = Kettenantrieb
	 * 3 = Arm
	 * @param index 
	 */
	public String getTeile(int type) throws NoItemsException {
		String erg="";
		if(l[type].getItemCount() >=1) {
			erg= l[type].getItem((l[type].getItemCount()-1));
			l[3].remove(l[type].getItemCount()-1);
			return erg;
		}else {
			throw new NoItemsException();
		}
	}

	/**
	 * Neue Teile werden dem Lagerhinzugefuegt.
	 * @param psgn
	 * @param type
	 */
	public void setTeile(String psgn, int type, boolean soll) {
		if(soll==true) {
			l[type].add(psgn);
		}
	}

	public class NoItemsException extends Exception {
		public NoItemsException() {
			super("Keine Teile dieses Types im Lager vorhanden! Lieferant wird informiert!");
		}
	}
}

