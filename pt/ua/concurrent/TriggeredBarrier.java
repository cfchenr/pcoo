package pt.ua.concurrent;

/**
 * An externally triggered thread barrier class.<BR>
 * In this type of barrier, the barrier is released by an externally usable (public) method ({@link #release() release}).
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, December 2015
 */
public class TriggeredBarrier extends ExternalBarrier
{
   /**
    * Constructs a new triggered barrier registering waiting threads.
    */
   public TriggeredBarrier()
   {
      this(true);
   }

   /**
    * Constructs a new triggered size barrier.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public TriggeredBarrier(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      waiting = 0;
   }

   public synchronized int numberWaitingThreads()
   {
      return waiting;
   }

   public void await() throws ThreadInterruptedException
   {
      awaitCount();
   }

   public synchronized long awaitCount() throws ThreadInterruptedException
   {
      waiting++;
      try
      {
         long oldCounter = count.get();
         long nextCounter = oldCounter + 1L;
         if (nextCounter < 0L)
            nextCounter = 0L;
         while(count.get() < nextCounter || nextCounter == 0L && count.get() >= oldCounter)
            awaitCObject();
      }
      finally
      {
         waiting--;
      }
      return count.get();
   }

   public synchronized void release()
   {
      count.incrementAndGet();
      if (count.get() < 0)
         count.set(0L);
      broadcast();
   }

   protected int waiting;
}

