package pt.ua.concurrent;

/**
 * A standard counting semaphore class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class Semaphore extends CObject
{
   /**
    * Creates a semaphore initialized with a zero counter and registering waiting threads.
    */
   public Semaphore()
   {
      this(0, true);
   }

   /**
    * Creates a semaphore initialized with a given counter and registering waiting threads.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code initialCount >= 0} - non negative counter</DD>
    * </DL>
    *
    * @param initialCount value
    */
   public Semaphore(int initialCount)
   {
      this(initialCount, true);
   }

   /**
    * Creates a semaphore initialized with a given counter.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code initialCount >= 0} - non negative counter</DD>
    * </DL>
    *
    * @param initialCount value
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public Semaphore(int initialCount, boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      assert initialCount >= 0: "negative counter: "+initialCount;

      count = initialCount;
   }

   /**
    * Acquires (decrements) the semaphore.
    */
   public synchronized void acquire()
   {
      assert count >= 0: "internal invariant error";

      while (count == 0)
      {
         super.await();
      }
      count--;
   }

   /**
    * Releases (increments) the semaphore.
    */
   public synchronized void release()
   {
      count++;
      broadcast();
   }

   protected int count;
}

