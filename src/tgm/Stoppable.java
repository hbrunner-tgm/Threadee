package tgm;

/**
 * Stoppable Klasse, wird hauptst??chlich von Watchdog(s) benutzt
 *
 * @author Andreas Willinger
 * @version 0.1
 * @since 01.10.13 15:19
 */
public interface Stoppable
    extends Runnable
{
    public void stop();
}
