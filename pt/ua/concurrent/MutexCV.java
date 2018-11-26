package pt.ua.concurrent;

/**
 * Mutex condition variable class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class MutexCV extends CObject implements SyncCV
{
   MutexCV(Mutex mtx) // package visibility (exported to Mutex)!
   {
      super(mtx != null ? mtx.registerAwaitingThreads() : false);

      assert mtx != null: "A new conditional variable requires a Mutex object!";

      this.mtx = mtx;
   }

   public synchronized Mutex mutex()
   {
      return mtx;
   }

   public void await()
   {
      assert lockIsMine();

      int lockCount = mtx.lockCount();
      try
      {
         synchronized(this)
         {
            for(int i = 0; i < lockCount; i++)
               mtx.unlock();
            super.await();
         }
      }
      finally
      {
         for(int i = 0; i < lockCount; i++)
            mtx.lock();
      }

      assert lockIsMine(): "postcondition error!";
   }

   public boolean await(long millis)
   {
      assert millis > 0: "Invalid millisecond timeout value ("+millis+")";
      assert lockIsMine();

      boolean res = false;

      int lockCount = mtx.lockCount();
      try
      {
         synchronized(this)
         {
            for(int i = 0; i < lockCount; i++)
               mtx.unlock();
            res = super.await(millis);
         }
      }
      finally
      {
         for(int i = 0; i < lockCount; i++)
            mtx.lock();
      }

      assert lockIsMine(): "postcondition error!";
      return res;
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
      return mtx.lockIsMine();
   }

   protected final Mutex mtx;
}

