package pt.ua.concurrent;

/**
 * Readers-writer exclusion condition variable class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class RWExCV extends CObject implements SyncCV
{
   RWExCV(RWEx rwe) // package visibility (exported to RWEx)!
   {
      super(rwe != null ? rwe.registerAwaitingThreads() : false);

      assert rwe != null;

      this.rwe = rwe;
   }

   public synchronized RWEx rwe()
   {
      return rwe;
   }

   public void await()
   {
      assert lockIsMine(): "lock is not mine";

      int writerLockCount = 0;
      int readerLockCount = 0;
      try
      {
         synchronized(this)
         {
            if (rwe.writerLockIsMine())
            {
               writerLockCount = rwe.writerLockCount();
               for(int i = 0; i < writerLockCount; i++)
                  rwe.unlockWriter();
            }
            else
            {
               while(rwe.readerLockIsMine())
               {
                  readerLockCount++;
                  rwe.unlockReader();
               }
            }
            super.await();
         }
      }
      finally
      {
         if (writerLockCount > 0)
         {
            for(int i = 0; i < writerLockCount; i++)
               rwe.lockWriter();
         }
         else
         {
            for(int i = 0; i < readerLockCount; i++)
               rwe.lockReader();
         }
      }

      assert lockIsMine();
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
      return rwe.lockIsMine();
   }

   protected final RWEx rwe;
}

