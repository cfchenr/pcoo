package pt.ua.concurrent;

import static java.lang.System.*;

/**
 * A composed condition variable for an exterior GroupMutex with a list of alternative
 * internal Sync objects (used, for example, to integrate internal, external and conditional
 * synchronization of shared objects).  A unique null reference is allowed to enable the
 * inclusion of lock-free schemes.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 */
public class GroupMutexComposedCV extends CObject implements SyncCV
{
   /**
    * Is list a valid array of Sync objects?
    *
    * @param list  the array of Sync objects
    *
    * @return true, if it is valid, false otherwise
    */
   public static boolean validList(Sync[] list)
   {
      boolean result = (list != null) && (list.length > 0);

      int count = 0;
      for(int i = 0; result && i < list.length; i++)
         if (list[i] == null)
            count++;
      result = (count <= 1);

      return result;
   }

   GroupMutexComposedCV(GroupMutex gmtx, Sync[] list) // package visibility (to be created within GroupMutex)
   {
      super(gmtx != null ? gmtx.registerAwaitingThreads() : false);

      assert gmtx != null;
      assert validList(list);

      this.gmtx = gmtx;
      this.list = new Sync[list.length];
      arraycopy(list, 0, this.list, 0, list.length);
   }

   public synchronized GroupMutex gmutex()
   {
      return gmtx;
   }

   public void await()
   {
      assert lockIsMine();

      SyncState syncState = null;
      Sync currentSync = null;
      int group = gmtx.activeGroup();
      for(int i = 0; currentSync == null && i < list.length; i++)
         if (list[i].lockIsMine())
            currentSync = list[i];
      synchronized(this)
      {
         if (currentSync != null)
            syncState = currentSync.getStateAndUnlock();
         gmtx.unlock(group);
         super.await();
      }
      gmtx.lock(group);
      if (currentSync != null)
         currentSync.recoverState(syncState);

      assert lockIsMine();
   }

   public synchronized void signal()
   {
      super.signal();
   }

   public synchronized void broadcast()
   {
      super.broadcast();
   }

   public synchronized boolean lockIsMine()
   {
      boolean result = gmtx.lockIsMine();
      boolean nullExists = false;
      int lockIsMineCount = 0;

      for(int i = 0; i < list.length; i++)
         if (list[i] == null)
            nullExists = true;
         else if (list[i].lockIsMine())
            lockIsMineCount++;

      return result && (nullExists && lockIsMineCount == 0 || lockIsMineCount == 1);
   }

   protected final GroupMutex gmtx;
   protected final Sync[] list;
}

