package tgm;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Lagermitarbeiter implements Stoppable
{
    private boolean isRunning;
    // basis pfad zum Lager
    private String basePath;

    // lagerbestände
    private HashMap<String, ETeil> teile;
    private HashMap<String, ETeil> pending;

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
        // gerade aus dem lager geholte teile, noch nicht fertig
        this.pending = new HashMap<String, ETeil>();

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
     * @return true wenn das Element hinzugefügt wurde, false falls nicht
     */
    public boolean addTeil(ETeil type, String teil)
    {
        synchronized (this)
        {
            // existierst das Teil schon?
            if(this.teile.get(teil) == null)
            {
                this.teile.put(teil, type);

                return true;
            }
            else
            {
                return false;
            }
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
        synchronized (this)
        {
            // durch alle vorhandenen teile durchloopen und nach dem Teil suchen
            for(Map.Entry<String, ETeil> teil : this.teile.entrySet())
            {
                if(teil.getValue() == type)
                {
                    // im Lager entfernen
                    this.teile.remove(teil.getKey());
                    // temporäre Map, nimmt elemente wie dieses auf
                    this.pending.put(teil.getKey(), teil.getValue());

                    return teil.getKey();
                }
            }
            return null;
        }
    }

    /**
     * Ein Teil komplett aus dem Lager entfernen.
     *
     * @param teil Teil, welches entfernt werden soll
     * @return true, wenn Teil exiistiert hat und gelöscht wurde, false wenn nicht
     */
    public boolean removeTeil(String teil)
    {
        synchronized (this)
        {
            if(this.pending.get(teil) == null) return false;
            this.pending.remove(teil);
            return true;
        }
    }

    /**
     * Ein Teil wieder ins Lager zurück legen.
     *
     * @param teil Teil, welches zurückgelegt werden soll.
     * @return true, wenn Teil erfolgreich zurückgelegt wurde, false wenn nicht
     */
    public boolean zuruckLegen(String teil)
    {
        synchronized (this)
        {
            // benötigt zur eindeutigen indentifiezierung im Lager
            ETeil eteil = this.pending.get(teil);

            if(eteil != null)
            {
                this.pending.remove(teil);
                this.teile.put(teil, eteil);

                return true;
            }
        }
        return false;
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
        for(String teil: auge)
        {
            this.teile.put(teil, ETeil.TEIL_AUGE);
        }
        for(String teil: arm)
        {
            this.teile.put(teil, ETeil.TEIL_ARM);
        }
        for(String teil: rumpf)
        {
            this.teile.put(teil, ETeil.TEIL_RUMPF);
        }
        for(String teil: kettenantrieb)
        {
            this.teile.put(teil, ETeil.TEIL_KETTENANTRIEB);
        }

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
    private boolean tryWriteFile(String path, Object[] data)
    {
        try
        {
            RandomAccessFile r = new RandomAccessFile(path, "rw");

            for(Object line:data)
            {
                r.writeUTF((String)line);
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
            // temporäre variablen, benötigt um Teile in die richtige datei zu schreiben
            LinkedList<String> augen = new LinkedList<String>();
            LinkedList<String> arme = new LinkedList<String>();
            LinkedList<String> ketten = new LinkedList<String>();
            LinkedList<String> rumpfe = new LinkedList<String>();

            synchronized (this)
            {
                // alle daten speichern
                for(Map.Entry<String, ETeil> teil : this.teile.entrySet())
                {
                    switch(teil.getValue())
                    {
                        case TEIL_AUGE:
                            augen.add(teil.getKey());
                            break;
                        case TEIL_ARM:
                            arme.add(teil.getKey());
                            break;
                        case TEIL_KETTENANTRIEB:
                            ketten.add(teil.getKey());
                            break;
                        case TEIL_RUMPF:
                            rumpfe.add(teil.getKey());
                            break;
                    }
                }
            }

            this.tryWriteFile(this.basePath+"augen.csv", augen.toArray());
            this.tryWriteFile(this.basePath+"arme.csv", arme.toArray());
            this.tryWriteFile(this.basePath+"kettenantriebe.csv", ketten.toArray());
            this.tryWriteFile(this.basePath+"rumpfe.csv", rumpfe.toArray());

            try
            {
                Thread.sleep(1000);
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

