package pt.ua.concurrent;

/**
 * Mutual exclusion lock.
 *
 * <P>This class implements a mutual exclusion scheme between threads.
 *
 * <P>The correct algorithm pattern to use Mutex objects should use {@code try/finally} blocks:
 * <pre>
 *    mtx.lock();
 *    try
 *    {
 *       ...
 *    }
 *    finally
 *    {
 *       mtx.unlock();
 *    }
 * </pre>
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 * @version 0.6, October 2013
 */
public class Mutex extends CObject implements Sync
{
   /**
    * Locking priority choices:
    * <BR><B>UNDEFINED</B>: undefined priority (default);
    * <BR><B>TIME_OF_ARRIVAL</B>: priority ordered by decreasing waiting time (not yet implemented!).
    */
   public enum Priority
   {
      UNDEFINED,
      TIME_OF_ARRIVAL
   };

   /**
    * Constructs a new non-recursive Mutex with UNDEFINED priority and registering waiting threads.
    */
   public Mutex()
   {
      this(false, true);
   }

   /**
    * Constructs a new Mutex with UNDEFINED priority and registering waiting threads.
    *
    * @param recursive if true, the created mutex will be recursive (non-recursive, if false)
    */
   public Mutex(boolean recursive)
   {
      this(recursive, true);
   }

   /**
    * Constructs a new Mutex with UNDEFINED priority.
    *
    * @param recursive if true, the created mutex will be recursive (non-recursive, if false)
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public Mutex(boolean recursive, boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);
      this.recursive = recursive;
      count = 0;
      owner = -1L;
   }

   /**
    * Locks mutex.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine() || recursive()} - not own lock unless if recursive</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code lockIsMine()} - lock is mine</DD>
    * </DL>
    */
   public synchronized void lock()
   {
      assert !lockIsMine() || recursive(): "mutex lock is already mine!";

      if (!lockIsMine())
      {
         while(count > 0)
         {
            super.await();
         }
         owner = CThread.currentThreadID();
      }
      count++;

      assert lockIsMine();
   }

   /**
    * Try to lock mutex (without waiting).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine() || recursive()} - not own lock unless if recursive</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code result && lockIsMine() || !result && !lockIsMine()}</DD>
    * </DL>
    *
    * @return true, if mutex locked, false otherwise
    */
   public synchronized boolean trylock()
   {
      assert !lockIsMine() || recursive(): "mutex lock is already mine!";

      boolean result = false;

      if (lockIsMine() || count == 0)
      {
         owner = CThread.currentThreadID();
         count++;
         result = true;
      }

      assert result && lockIsMine() || !result && !lockIsMine();

      return result;
   }

   /**
    * Unlocks mutex.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !lockIsMine() || lockCount() > 0}</DD>
    * </DL>
    */
   public synchronized void unlock()
   {
      assert lockIsMine(): "lock is not mine";

      count--;
      if (count == 0)
      {
         owner = -1L;
         broadcast();
      }

      assert !lockIsMine() || lockCount() > 0;
   }

   public synchronized boolean lockIsMine()
   {
      return CThread.currentThreadID() == owner; // safe operation!
   }

   /**
    * Is current mutex recursive?
    *
    * @return true, if mutex is recursive
    */
   public synchronized boolean recursive()
   {
      return recursive;
   }

   /**
    * Lock count of current mutex.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()}</DD>
    * </DL>
    *
    * @return the count value
    */
   public synchronized int lockCount()
   {
      assert lockIsMine(): "lock is not mine";

      return count;
   }

   /**
    * Create and return a new condition variable attached to current Mutex.
    *
    * @return the new condition variable
    */
   public synchronized MutexCV newCV()
   {
      MutexCV cv = new MutexCV(this);
      registerSyncCV(cv);
      return cv;
   }

   public synchronized SyncState getStateAndUnlock()
   {
      assert lockIsMine(): "lock is not mine";

      SyncState result = new SyncState(this, (Integer)count);

      while(count > 0)
         unlock();

      assert !lockIsMine();

      return result;
   }

   public synchronized void recoverState(SyncState state) throws ThreadInterruptedException
   {
      assert state != null && state.obj() == this;
      assert !lockIsMine(): "lock is mine";

      lock();
      count = (Integer)state.state();

      assert lockIsMine();
   }

   protected final boolean recursive;
   protected int count = 0;
   protected long owner;
}

