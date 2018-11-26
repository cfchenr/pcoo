package pt.ua.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class implements an active (thread) metronome class, able to periodically
 * synchronize any * number of threads.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, December 2015
 */
public class Metronome
{
   /**
    * Constructs a new metronome with a specified time period.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code clockPeriod > 0L}</DD>
    * </DL>
    *
    * @param clockPeriod the metronome's period in milliseconds
    */
   public Metronome(long clockPeriod)
   {
      assert clockPeriod > 0L;

      triggeredBarrier = new TriggeredBarrier();
      clock = Executors.newScheduledThreadPool(1);
      clock.scheduleAtFixedRate(new Runnable() {public void run() {triggeredBarrier.release();} }, clockPeriod, clockPeriod, TimeUnit.MILLISECONDS);
   }

   /**
    * Is current metronome still active?
    *
    * @return true, if active, false otherwise
    */
   public boolean active()
   {
      return clock != null;
   }

   /**
    * Caller will wait until the next metronome tick.
    *
    * @return number of ticks so far 
    */
   public long sync()
   {
      assert active();

      return triggeredBarrier.awaitCount();
   }

   /**
    * Terminate current metronome.
    *
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !active()}</DD>
    * </DL>
    */
   public void terminate()
   {
      clock.shutdown();
      clock = null;

      assert !active();
   }

   protected final TriggeredBarrier triggeredBarrier;
   protected volatile ScheduledExecutorService clock = null;
}

