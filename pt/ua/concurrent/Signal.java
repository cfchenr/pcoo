package pt.ua.concurrent;

/**
 * A simple signal abstraction module.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public interface Signal extends InterruptibleAwaitingThreads
{
   /**
    * Signaling (sender).
    */
   abstract public void send();

   /**
    * Waiting to be signaled (receiver).
    */
   abstract public void await() throws ThreadInterruptedException;
}

