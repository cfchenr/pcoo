package pt.ua.concurrent;

/**
 * A fixed sized thread barrier class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 * @version 0.6, December 2015
 */
public class FixedBarrier extends InternalBarrier
{
   /**
    * Constructs a new fixed size barrier for {@code size} threads registering waiting threads.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code size > 0}</DD>
    * </DL>
    *
    * @param size barrier's number of threads
    */
   public FixedBarrier(int size)
   {
      this(size, true);
   }

   /**
    * Constructs a new fixed size barrier for {@code size} threads.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code size > 0}</DD>
    * </DL>
    *
    * @param size barrier's number of threads
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public FixedBarrier(int size, boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      assert size > 0;

      this.size = size;
      waiting = 0;
      release = false;
   }

   /**
    * Define a new size for the barrier.
    * This operation may release the barrier (if waiting equal or exceed size).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code size > 0}</DD>
    * </DL>
    *
    * @param size barrier's number of threads
    */
   public synchronized void setSize(int size)
   {
      assert size > 0;

      this.size = size;
      if (waiting >= size)
         broadcast();
   }

   public synchronized int size()
   {
      return size;
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
      assert numberWaitingThreads() < size(); // invariant

      while(release)
         awaitCObject();
      waiting++;
      try
      {
         if (waiting == size)
         {
            release = true;
            count.incrementAndGet();
            broadcast();
         }
         else
         {
            while(!release)
               awaitCObject();
         }
      }
      finally
      {
         waiting--;
         if (waiting == 0)
         {
            release = false;
            broadcast();
         }
      }

      return count.get();
   }

   protected int waiting;
   protected boolean release;
   protected int size;
}

