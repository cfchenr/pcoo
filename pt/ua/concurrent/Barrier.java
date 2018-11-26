package pt.ua.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread barrier abstract class.
 *
 * <DL><DT><B>Invariant:</B></DT>
 *    <DD>{@code numberWaitingThreads() >= 0}</DD>
 * </DL>
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 * @version 0.6, December 2015
 */
public abstract class Barrier extends CObject
{
   /**
    * Constructs a new barrier registering waiting threads.
    */
   public Barrier()
   {
      this(true);
   }

   /**
    * Constructs a new barrier.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public Barrier(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      count = new AtomicLong(0L);
      //count = new AtomicLong(Long.MAX_VALUE-2); // DEBUG
   }

   /**
    * Number of threads currently waiting on the barrier.
    *
    * @return number of threads
    */
   public abstract int numberWaitingThreads();

   /**
    * Caller will wait until the thread barrier is released, situation
    * in which all waiting threads are awakened.
    *
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code count() == old(count())+ 1}</DD>
    * </DL>
    */
   public abstract void await() throws ThreadInterruptedException;

   /**
    * Caller will wait until the thread barrier is released, situation
    * in which all waiting threads are awakened.
    *
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code count() == old(count())+ 1}</DD>
    * </DL>
    *
    * @return the current barrier {@link #count() count}
    */
   public abstract long awaitCount() throws ThreadInterruptedException;

   /**
    * Number of times the barrier was released (to be more precise: the remainder
    * of the integer division for 2^63)
    *
    * @return the current barrier {@link #count() count}
    */
   public long count()
   {
      return count.get();
   }

   protected void awaitCObject() throws ThreadInterruptedException
   {
      // to bypass descendant naming problems!
      super.await();
   }

   protected final AtomicLong count;
}

