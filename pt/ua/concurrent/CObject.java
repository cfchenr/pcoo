package pt.ua.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A replacement for Object to ease and augment the usage of concurrent related methods.
 *
 * <P>This class is only usable by extension.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 */
public class CObject implements SyncCV
{
   /**
    * Constructor of an object without registering waiting threads in await service.
    */
   public CObject()
   {
      this(false);
   }

   /**
    * Constructor of an object.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public CObject(boolean registerAwaitingThreads)
   {
      super();
      if (registerAwaitingThreads)
      {
         __waitingSet__ = new ConcurrentLinkedQueue<Thread>();
         __syncCVQueue__ = new ConcurrentLinkedQueue<SyncCV>();
      }
      else
      {
         __waitingSet__ = null;
         __syncCVQueue__ = null;
      }
   }

   /**
    * Are threads registered when waiting (in await)?
    *
    * @return true, if threads are registered
    */
   public boolean registerAwaitingThreads()
   {
      return __waitingSet__ != null;
   }

   /**
    * Is lock owned by me?
    *
    * @return true, if lock is mine
    */
   public boolean lockIsMine()
   {
      return syncronizedLockIsMine();
   }

   /**
    * Is native (synchronized) lock owned by me?
    *
    * @return true, if native lock is mine
    */
   public final boolean syncronizedLockIsMine()
   {
      return Thread.holdsLock(this);
   }

   /**
    * Object's wait replacement, in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException;<BR>
    * - supports the registration of waiting threads for interrupt purposes.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code syncronizedLockIsMine()} - native lock owned by me</DD>
    * </DL>
    */
   public void await() throws ThreadInterruptedException
   {
      assert syncronizedLockIsMine(): "waiting requires the ownership of the lock!";

      __private__await__(0L, 0);
   }

   /**
    * Object's wait replacement, in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException;<BR>
    * - supports the registration of waiting threads for interrupt purposes;
    * - timeout expiration can be tested using the method's result.
    *
    * <b>Important</b>: Unlike Object's timed wait methods, CObject's timeout await methods require a non-zero timeout.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code millis >= 0} - non negative timeout</DD>
    *    <DD>{@code syncronizedLockIsMine()} - native lock owned by me</DD>
    * </DL>
    *
    * @param millis timeout (in milliseconds)
    *
    * @return false if await terminated due to a timeout expiration, true otherwise
    */
   public boolean await(long millis) throws ThreadInterruptedException
   {
      assert millis > 0: "Invalid millisecond timeout value ("+millis+")";
      assert syncronizedLockIsMine(): "waiting requires the ownership of the lock!";

      return __private__await__(millis, 0);
   }

   /**
    * Object's wait replacement, in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException;<BR>
    * - supports the registration of waiting threads for interrupt purposes;
    * - timeout expiration can be tested using the method's result.
    *
    * <b>Important</b>: Unlike Object's timed wait methods, CObject's timeout await methods require a non-zero timeout.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code millis > 0 || nanos > 0} - positive timeout</DD>
    *    <DD>{@code syncronizedLockIsMine()} - native lock owned by me</DD>
    * </DL>
    *
    * @param millis timeout (in milliseconds)
    * @param nanos timeout (in nanoseconds)
    *
    * @return false if await terminated due to a timeout expiration, true otherwise
    */
   public boolean await(long millis, int nanos) throws ThreadInterruptedException
   {
      assert millis > 0 || nanos > 0: "Invalid timeout value ("+millis+" milliseconds, "+nanos+" nanosseconds)";
      assert syncronizedLockIsMine(): "waiting requires the ownership of the lock!";

      return __private__await__(millis, nanos);
   }

   /**
    * Just to avoid redundancy and to implement stronger contracts in timed await methods
    * (private because it is not exported to descendants).
    *
    * @return false if await terminated due to a timeout expiration, true otherwise
    */
   private boolean __private__await__(long millis, int nanos) throws ThreadInterruptedException
   {
      long t = 0L;
      if (millis > 0 || nanos > 0)
      {
         if (nanos == 0)
            t = System.currentTimeMillis();
         else
            t = System.nanoTime();
      }
      Thread currentThread = Thread.currentThread();
      if (registerAwaitingThreads())
         __waitingSet__.add(currentThread);
      if (__interrupted__ || currentThread.isInterrupted())
         throw new ThreadInterruptedException();

      try
      {
         super.wait(millis, nanos);
         if (millis > 0 || nanos > 0)
         {
            if (nanos == 0)
            {
               if (System.currentTimeMillis() - t >= millis)
                  return false;
            }
            else
            {
               if (System.nanoTime() - t >= millis*1000000L+nanos)
                  return false;
            }
         }
      }
      catch(InterruptedException e)
      {
         throw new ThreadInterruptedException(e);
      }
      finally
      {
         if (registerAwaitingThreads())
            __waitingSet__.remove(currentThread);
      }
      return true;
   }

   /**
    * Object's nofify replacement.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code syncronizedLockIsMine()} - native lock owned by me</DD>
    * </DL>
    */
   public void signal()
   {
      assert syncronizedLockIsMine(): "signaling requires the ownership of the lock!";

      notify();
   }

   /**
    * Object's nofifyAll replacement.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code syncronizedLockIsMine()} - native lock owned by me</DD>
    * </DL>
    */
   public void broadcast()
   {
      assert syncronizedLockIsMine(): "broadcasting requires the ownership of the lock!";

      notifyAll();
   }

   /**
    * Requests the interruption all threads blocked on current lock and/or related condition variables.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code registerAwaitingThreads()} - registering of waiting threads activated</DD>
    * </DL>
    */
   public void interruptWaitingThreads()
   {
      assert registerAwaitingThreads(): "registering of waiting threads is required";

      __interrupted__ = true;

      // interrupt waiting threads:
      Thread t;
      while((t = __waitingSet__.poll()) != null)
         t.interrupt();

      // interrupt registered SyncCV waiters:
      SyncCV s;
      while((s = __syncCVQueue__.poll()) != null)
         s.interruptWaitingThreads();
   }

   /**
    * Dump waiting threads information (for debugging purposes). MOS: TODO!
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code registerAwaitingThreads()} - registering of waiting threads activated</DD>
    * </DL>
   public void dumpState(PrintWriter out)
   {
      assert registerAwaitingThreads(): "registering of waiting threads is required";
      assert out != null;

      // interrupt waiting threads:

      // interrupt registered SyncCV waiters:
   }
    */

   /**
    * Register a new condition variable object.
    * This service is meant to be used be descendants implementing Sync interface.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code s != null} - SyncCV object reference</DD>
    * </DL>
    */
   protected void registerSyncCV(SyncCV s)
   {
      assert s != null: "SyncCV object required";

      if (registerAwaitingThreads())
         __syncCVQueue__.add(s);
   }

   /**
    * Unregister a condition variable object.
    * This service is meant to be used be descendants implementing Sync interface.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code s != null} - SyncCV object reference</DD>
    * </DL>
    */
   protected void unregisterSyncCV(SyncCV s)
   {
      assert s != null: "SyncCV object required";

      if (registerAwaitingThreads())
         __syncCVQueue__.remove(s);
   }

   // Wait-free queue of SyncCV attached to CObject 
   private final ConcurrentLinkedQueue<SyncCV> __syncCVQueue__;

   // Object scoped waiting set (lock free implementation)
   private final ConcurrentLinkedQueue<Thread> __waitingSet__;

   private volatile boolean __interrupted__ = false;
}

