package tgm;

import org.apache.commons.cli.*;

import javax.swing.*;

/**
 * Diese Klasse wird benutzt, um die Kommandozeilen Argumente im GNU Style zu parsen.
 *
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 01.10.13 19:34
 */
public class CommandLineParser
{
    private String[] args;
    private Options options;

    public CommandLineParser(String[] args)
    {
        this.args = args;
        // dies beinhaltet alle verf??gbaren argumente des programmes
        this.options = new Options();

        // alle argumente definieren
        Option lager = OptionBuilder.withArgName("verzeichnis")
                .hasArg()
                .withDescription("pfad zum lager-verzeichnis")
                .create( "lager" );

        Option logs = OptionBuilder.withArgName("verzeichnis")
                .hasArg()
                .withDescription("pfad zum log-verzeichnis")
                .create( "logs" );

        Option lieferanten = OptionBuilder.withArgName("anzahl")
                .hasArg()
                .withDescription("anzahl der liefaranten-threads")
                .create( "lieferanten" );

        Option monteure = OptionBuilder.withArgName("anzahl")
                .hasArg()
                .withDescription("anzahl der monteur-threads")
                .create( "monteure" );

        Option laufzeit = OptionBuilder.withArgName("ms")
                .hasArg()
                .withDescription("zeit wie lange die simulation laufen soll (in ms)")
                .create( "laufzeit" );

        // zu der liste der verf??gbaren attribute hinzuf??gen
        this.options.addOption(lager);
        this.options.addOption(logs);
        this.options.addOption(lieferanten);
        this.options.addOption(monteure);
        this.options.addOption(laufzeit);
    }

    /**
     * Den vorher definierten Kommandozeilen output parsen
     *
     *
     */
    public void parse()
    {
        GnuParser parser = new GnuParser();
        try
        {
            CommandLine line = parser.parse(this.options, this.args);

            if(line.hasOption("lager")
                && line.hasOption("logs")
                && line.hasOption("lieferanten")
                && line.hasOption("monteure")
                && line.hasOption("laufzeit"))
            {
                String lagerValue = line.getOptionValue("lager");
                String logsValue = line.getOptionValue("logs");
                String lieferantenValue =line.getOptionValue("lieferanten");
                String monteureValue = line.getOptionValue("monteure");
                String laufzeitValue = line.getOptionValue("laufzeit");

                int lieferanten, monteure;
                long laufzeit;

                try
                {
                    lieferanten = Integer.parseInt(lieferantenValue);
                    monteure = Integer.parseInt(monteureValue);
                    laufzeit = Long.parseLong(laufzeitValue);
                }catch(NumberFormatException e)
                {
                    this.printHelp();
                    return;
                }

                if(lieferanten > 0 && lieferanten <= 100 && monteure > 0 && monteure < 100 && laufzeit > 1)
                {
                    Sekretariat sekretariat = new Sekretariat(lagerValue, logsValue, monteure, lieferanten, laufzeit);
                    sekretariat.startWork();
                }
                else
                {
                    this.printHelp();
                }
            }
            else
            {
                this.printHelp();
            }
        }
        catch(ParseException e)
        {
            this.printHelp();
        }
    }

    private void printHelp()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Start", this.options);
    }
}
