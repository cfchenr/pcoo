package pt.ua.concurrent;

/**
 * Group exclusion condition variable class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class GroupMutexCV extends CObject implements SyncCV
{
   GroupMutexCV(GroupMutex gmtx) // package visibility (exported to GroupMutex)!
   {
      super(gmtx != null ? gmtx.registerAwaitingThreads() : false);

      assert gmtx != null;

      this.gmtx = gmtx;
   }

   public synchronized GroupMutex gmutex()
   {
      return gmtx;
   }

   public void await()
   {
      assert lockIsMine();

      int group = gmtx.activeGroup();
      synchronized(this)
      {
         gmtx.unlock(group);
         super.await();
      }
      gmtx.lock(group);

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

   public boolean lockIsMine()
   {
      return gmtx.lockIsMine();
   }

   protected final GroupMutex gmtx;
}

