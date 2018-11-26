package pt.ua.concurrent;

/**
 * An externally triggered thread barrier abstract class.<BR>
 * In this type of barrier, the barrier is released by an externally usable (public) method ({@link #release() release}).
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, December 2015
 */
public abstract class ExternalBarrier extends Barrier
{
   /**
    * Constructs a new external barrier registering waiting threads.
    */
   public ExternalBarrier()
   {
      this(true);
   }

   /**
    * Constructs a new external barrier.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public ExternalBarrier(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);
   }

   /**
    * Release barrier (regardless of the number of waiting threads).
    *
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code count() == old(count())+ 1}</DD>
    * </DL>
    */
   public abstract void release();
}

