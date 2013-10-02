package tgm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Klasse zum sicheren schreiben von Dateien.
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 02.10.13 20:55
 */
public class SafeWriter
{
    private BufferedWriter bw;
    private boolean isInitialized;

    public SafeWriter(String path)
    {
        try
        {
            File f = new File(path);

            if(!f.exists()) f.createNewFile();

            FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
            this.bw = new BufferedWriter(fw);

            this.isInitialized = true;
        }
        catch(IOException e)
        {
            this.isInitialized = false;
        }
    }

    /**
     * Schreibt eine einzelne Zeile in die Datei und beendet sie mit einem OS-abhängigen EOL Zeichen.
     *
     * @param line Zeile, die geschrieben werden soll
     * @return true, wenn Zeile geschrieben wurde, falls wenn nicht
     */
    public synchronized boolean writeLine(String line)
    {
        if(!this.isInitialized) return false;

        try
        {
            this.bw.write(line+System.getProperty("line.separator"));

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
