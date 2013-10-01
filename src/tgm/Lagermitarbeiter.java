package tgm;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Lagermitarbeiter implements Stoppable
{
    private boolean isRunning;
    // basis pfad zum Lager
    private String basePath;

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

	public Lagermitarbeiter(String path)
    {
        // lager pfad
        this.basePath = path;

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
                this.arm.add(teil);
                break;
            case TEIL_AUGE:
                this.auge.add(teil);
                break;
            case TEIL_RUMPF:
                this.rumpf.add(teil);
                break;
            case TEIL_KETTENANTRIEB:
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
                break;
            case TEIL_AUGE:
                teil = this.auge.poll();
                this.pending.add(teil);
                break;
            case TEIL_RUMPF:
                teil = this.rumpf.poll();
                this.pending.add(teil);
                break;
            case TEIL_KETTENANTRIEB:
                teil = this.kettenantrieb.poll();
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
                break;
            case TEIL_AUGE:
                this.auge.add(teil);
                break;
            case TEIL_RUMPF:
                this.rumpf.add(teil);
                break;
            case TEIL_KETTENANTRIEB:
                this.kettenantrieb.add(teil);
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
            return null;
        }
        catch (IOException e)
        {
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
            RandomAccessFile r = new RandomAccessFile(path, "rw");

            for(String line:data)
            {
                r.writeUTF(line + "\n");
            }
            r.close();

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    @Override
    public void run()
    {
        while(this.isRunning)
        {
            this.tryWriteFile(this.basePath+"augen.csv", this.auge);
            this.tryWriteFile(this.basePath+"arme.csv", this.arm);
            this.tryWriteFile(this.basePath+"kettenantriebe.csv", this.kettenantrieb);
            this.tryWriteFile(this.basePath+"rumpfe.csv", this.rumpf);

            try
            {
                Thread.sleep(2500);
            }
            catch(InterruptedException e)
            {

            }
        }
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }
}

