package pt.ua.concurrent;

/**
 * An internally triggered thread barrier abstract class.<BR>
 * In this type of barrier, the participating threads are the ones that release the barrier (when the
 * number of waiting threads reach the barrier defined size).
 *
 * <DL><DT><B>Invariant:</B></DT>
 *    <DD>{@code size() > 0}</DD>
 *    <DD>{@code numberWaitingThreads() >= 0 && numberWaitingThreads() < size()}</DD>
 * </DL>
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, December 2015
 */
public abstract class InternalBarrier extends Barrier
{
   /**
    * Constructs a new internal barrier registering waiting threads.
    */
   public InternalBarrier()
   {
      this(true);
   }

   /**
    * Constructs a new internal barrier.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public InternalBarrier(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);
   }

   /**
    * Number of threads defined for the barrier.
    *
    * @return barrier size
    */
   public abstract int size();
}

