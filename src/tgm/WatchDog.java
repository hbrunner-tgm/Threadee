package tgm;

public class WatchDog implements Runnable
{
    private Stoppable worker;
    private long zeit;

    public WatchDog(Stoppable worker, long zeit)
    {
        this.worker = worker;
        this.zeit = zeit;
    }

    @Override
    public void run() {
        try
        {
            Thread.sleep(this.zeit);
        }
        catch(InterruptedException e)
        {

        }
        this.worker.stop();
    }
}
