package tgm;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Lagermitarbeiter implements Stoppable
{
    private boolean isRunning;
    // basis pfad zum Lager
    private String basePath;

    // logging
    private final static Logger logging = Logger.getLogger("Lagermitarbeiter");

    // lagerbestände
    private ConcurrentLinkedQueue<String> arm;
    private ConcurrentLinkedQueue<String> auge;
    private ConcurrentLinkedQueue<String> rumpf;
    private ConcurrentLinkedQueue<String> kettenantrieb;

    // herausgenomme teile
    private ConcurrentLinkedQueue<String> pending;

    enum ETeil
    {
        TEIL_AUGE,
        TEIL_RUMPF,
        TEIL_KETTENANTRIEB,
        TEIL_ARM
    };

	public Lagermitarbeiter(String pfadLog, String pfadLager)
    {
        // lager pfad
        this.basePath = pfadLager;
        this.logging.setLevel(Level.ALL);

        pfadLog += "lagermitarbeiter.log";
        try
        {
            File f = new File(pfadLog);
            if(!f.exists()) f.createNewFile();

            FileHandler fh = new FileHandler(pfadLog);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logging.addHandler(fh);

        }
        catch (IOException e)
        {
            System.out.println("File logging disabled");
        }

        this.arm = new ConcurrentLinkedQueue<String>();
        this.auge = new ConcurrentLinkedQueue<String>();
        this.rumpf = new ConcurrentLinkedQueue<String>();
        this.kettenantrieb = new ConcurrentLinkedQueue<String>();
        // gerade aus dem lager geholte teile, noch nicht fertig
        this.pending = new ConcurrentLinkedQueue<String>();

        try
        {
            this.initialize();
        }
        catch (IOException e)
        {
            logging.log(Level.SEVERE, "Fataler Fehler während der Initialisierung des Lagermitarbeiters!");
        }
    }

    /**
     * Fügt ein neues Teil zum Lager hinzu, falls noch nicht vorhanden.
     *
     * @param type Der Typ des Teils (Verfügbar: TEIL_ARM, TEIL_AUGE, TEIL_KETTENANTRIEB, TEIL_RUMPF)
     * @param teil Das Teil selber
     * @noreturn
     */
    public void addTeil(ETeil type, String teil)
    {
        switch(type)
        {
            case TEIL_ARM:
                logging.log(Level.INFO, "Neuer Arm hinzugefügt: "+teil);
                this.arm.add(teil);
                break;
            case TEIL_AUGE:
                logging.log(Level.INFO, "Neuer Auge hinzugefügt: "+teil);
                this.auge.add(teil);
                break;
            case TEIL_RUMPF:
                logging.log(Level.INFO, "Neuer Rumpf hinzugefügt: "+teil);
                this.rumpf.add(teil);
                break;
            case TEIL_KETTENANTRIEB:
                logging.log(Level.INFO, "Neuer Kettenantrieb hinzugefügt: "+teil);
                this.kettenantrieb.add(teil);
                break;
        }
    }

    /**
     * Nächstes Teil aus dem Lager holen.
     * Diese Methode verschiebt das Teil in eine temporäre Map, nicht vergessen das Teil komplett zu entfernen!
     *
     * Das geschieht entweder durch removeTeil(String teil) oder durch zuruckLegen(String teil)
     *
     * @param type Typ des Teils, nachdem gesucht wird
     * @return Ein String, welches das Teil enthält, null bei Fehler/nicht existenz
     */
    public String getTeil(ETeil type)
    {
        String teil;

        switch(type)
        {
            case TEIL_ARM:
                teil = this.arm.poll();
                this.pending.add(teil);
                logging.log(Level.INFO, "Arm '"+teil+"' wurde aus dem Lager geholt");
                break;
            case TEIL_AUGE:
                teil = this.auge.poll();
                this.pending.add(teil);
                logging.log(Level.INFO, "Auge '"+teil+"' wurde aus dem Lager geholt");
                break;
            case TEIL_RUMPF:
                teil = this.rumpf.poll();
                this.pending.add(teil);
                logging.log(Level.INFO, "Rumpf '"+teil+"' wurde aus dem Lager geholt");
                break;
            case TEIL_KETTENANTRIEB:
                teil = this.kettenantrieb.poll();
                this.pending.add(teil);
                logging.log(Level.INFO, "Kettenantrieb '"+teil+"' wurde aus dem Lager geholt");
                break;
            default:
                teil = null;
                break;
        }
        return teil;

    }

    /**
     * Ein Teil komplett aus dem Lager entfernen.
     *
     * @param teil Teil, welches entfernt werden soll
     * @noreturn
     */
    public void removeTeil(String teil)
    {
        logging.log(Level.INFO, "Teil '"+teil+"' wurde aus dem Lager entfernt");
        this.pending.remove(teil);
    }

    /**
     * Ein Teil wieder ins Lager zurück legen.
     *
     * @param teil Teil, welches zurückgelegt werden soll.
     * @noreturn
     */
    public void zuruckLegen(String teil, ETeil type)
    {
        this.pending.remove(teil);

        switch(type)
        {
            case TEIL_ARM:
                this.arm.add(teil);
                logging.log(Level.INFO, "Arm '"+teil+"' wurde in das Lager zurückgelegt.");
                break;
            case TEIL_AUGE:
                this.auge.add(teil);
                logging.log(Level.INFO, "Auge '"+teil+"' wurde in das Lager zurückgelegt.");
                break;
            case TEIL_RUMPF:
                this.rumpf.add(teil);
                logging.log(Level.INFO, "Rumpf '"+teil+"' wurde in das Lager zurückgelegt.");
                break;
            case TEIL_KETTENANTRIEB:
                this.kettenantrieb.add(teil);
                logging.log(Level.INFO, "Kettenantrieb '"+teil+"' wurde in das Lager zurückgelegt.");
                break;
        }
    }

    /**
     * Programm initialisieren, diese Methode liest im Grunde nur die Daten wieder ein.
     *
     * @throws IOException
     */
    private void initialize() throws IOException
    {
        // augen einlesen
        LinkedList<String> auge = this.tryLoadFile(this.basePath+"augen.csv");
        if(auge == null) throw new IOException();
        // rümpfe einlesen
        LinkedList<String> rumpf = this.tryLoadFile(this.basePath+"rumpfe.csv");
        if(rumpf == null) throw new IOException();
        // kettenantriebe einlesen
        LinkedList<String> kettenantrieb = this.tryLoadFile(this.basePath+"kettenantriebe.csv");
        if(kettenantrieb == null) throw new IOException();
        // arme einlesen
        LinkedList<String> arm = this.tryLoadFile(this.basePath+"arme.csv");
        if(arm == null) throw new IOException();

        System.out.println(arm.size());
        // wenn's keine exception gab, alles in die lokalen variablen schreiben
        this.auge.addAll(auge);
        this.arm.addAll(arm);
        this.rumpf.addAll(rumpf);
        this.kettenantrieb.addAll(kettenantrieb);

        this.isRunning = true;
    }

    /**
     * Safe-way um eine Datei zu laden.
     *
     * @param path Eindeutiger, Absoluter Pfad zur Datei (mit endung)
     * @return LinkedList, welche pro element eine Zeile enthält. Bei Fehler null
     */
    private LinkedList<String> tryLoadFile(String path)
    {
        BufferedReader bf = null;

        try
        {
            File f = new File(path);
            if(f.exists()) f.delete();

            f.createNewFile();

            FileReader fr = new FileReader(path);
            bf = new BufferedReader(fr);

            LinkedList<String> list = new LinkedList<String>();
            String line = null;

            // bis zum Dateiende lesen
            while((line = bf.readLine()) != null)
            {
                list.add(line);
            }
            bf.close();

            return list;
        }
        catch (FileNotFoundException e)
        {
            logging.log(Level.SEVERE, "Unbekannter Fehler während dem einlesen des Lagerfiles '"+path+"'");
            return null;
        }
        catch (IOException e)
        {
            logging.log(Level.SEVERE, "Unbekannter Fehler während dem einlesen des Lagerfiles '"+path+"'");
            return null;
        }
    }

    /**
     * Safe-way, um eine Datei zu schreiben.
     *
     * @param path Eindeutiger, Absoluter Pfad zur Datei, die geschrieben werden soll.
     * @param data Object-Array, pro element eine Zeile
     * @return true, wenn Datei geschrieben wurde, false wenn nicht
     */
    private boolean tryWriteFile(String path, Queue<String> data)
    {
        try
        {
            File f = new File(path);

            if(f.exists()) f.delete();
            f.createNewFile();

            FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            String output = "";

            for(String line:data)
            {
                line +=  System.getProperty("line.separator");
                output += line;
            }
            bw.write(output);
            bw.close();

            return true;
        }
        catch (IOException e)
        {
            logging.log(Level.SEVERE, "Unbekannter Fehler während des Schreibens der Datei '" + path + "'");
            return false;
        }
    }

    /**
     * Alle Änderungen auf die Festplatte schreiben
     * @noreturn
     */
    private void saveChanges()
    {
        this.tryWriteFile(this.basePath+"augen.csv", this.auge);
        this.tryWriteFile(this.basePath+"arme.csv", this.arm);
        this.tryWriteFile(this.basePath+"kettenantriebe.csv", this.kettenantrieb);
        this.tryWriteFile(this.basePath+"rumpfe.csv", this.rumpf);
    }

    @Override
    public void run()
    {

        while(this.isRunning)
        {
            this.saveChanges();

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                logging.log(Level.WARNING, "Thread wurde im Schlaf unterbrochen!");
            }
        }

    }

    @Override
    public void stop()
    {
        this.isRunning = false;
        logging.log(Level.INFO, "Arbeit beendet.");
    }
}

