package pt.ua.concurrent;

/**
 * Mutex set condition variable class.
 *
 * This class is useful to link a condition variable to a set of mutexes (instead of one).
 * In this way, we can safely wait for a condition that depends on more than one Mutex.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2016
 */
public class MutexSetCV extends CObject implements SyncCV
{
   /**
    * Constructs a new MutexSet with an ordered set of Mutex objects (the locking order is the array's index order).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code set != null && set.length > 0}</DD>
    * </DL>
    *
    * @param set the ordered set of Mutex objects
    */
   public MutexSetCV(Mutex[] set)
   {
      assert set != null && set.length > 0: "A new condition variable requires a valid set of metexes - "+(set==null ? "null set" : "set lenght="+set.length)+"!";

      this.set = new Mutex[set.length];
      System.arraycopy(set, 0, this.set, 0, set.length);
      this.lockCount = new int[set.length];
   }

   public void await()
   {
      assert lockIsMine(): "lock is not mine";

      for(int m = 0; m < lockCount.length; m++)
         lockCount[m] = set[m].lockCount();
      try
      {
         synchronized(this)
         {
            for(int m = lockCount.length-1; m >= 0; m--) // unlock in the reverse lock order
               for(int i = 0; i < lockCount[m]; i++)
                  set[m].unlock();
            super.await();
         }
      }
      finally
      {
         for(int m = 0; m < lockCount.length; m++)
            for(int i = 0; i < lockCount[m]; i++)
               set[m].lock();
      }

      assert lockIsMine(): "postcondition error!";
   }

   public synchronized void signal()
   {
      assert lockIsMine(): "signal requires the ownership of the lock!";

      super.signal();
   }

   public synchronized void broadcast()
   {
      assert lockIsMine(): "broadcast requires the ownership of the lock!";

      super.broadcast();
   }

   public boolean lockIsMine()
   {
      boolean result = true;
      for(int m = 0; result && m < set.length; m++)
         result = set[m].lockIsMine();
      return result;
   }

   protected final Mutex[] set;
   protected final int[] lockCount;
}

