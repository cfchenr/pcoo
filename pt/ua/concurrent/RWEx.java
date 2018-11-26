package pt.ua.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

//TODO: TIME_OF_ARRIVAL priority

/**
 * Readers-writer exclusion class.
 *
 * <P>The correct algorithm pattern to use RWEx objects should use {@code try/finally} blocks:
 * <pre>
 *    rwex.lockWriter(); // rwex.lockReader();
 *    try
 *    {
 *       ...
 *    }
 *    finally
 *    {
 *       rwex.unlockWriter(); // rwex.unlockReader(); // rwex.unlock();
 *    }
 * </pre>
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class RWEx extends CObject implements Sync
{
   /**
    * Locking priority choices:
    * <BR><B>WRITER</B>: priority to writers (a waiting writer prevents new readers from getting the lock);
    * <BR><B>READERS</B>: priority to readers (a waiting reader prevents new writers from getting the lock);
    * <BR><B>TIME_OF_ARRIVAL</B>: priority ordered by decreasing waiting time (not yet implemented!).
    */
   public enum Priority {WRITER, READERS, TIME_OF_ARRIVAL};

   /**
    * Constructs a new non-recursive RWEx with WRITER priority and registering waiting threads.
    */
   public RWEx()
   {
      this(Priority.WRITER, false, true);
   }

   /**
    * Constructs a new RWEx with WRITER priority.
    *
    * @param recursive if true, the created rwex will be recursive (non-recursive, if false)
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public RWEx(boolean recursive, boolean registerAwaitingThreads)
   {
      this(Priority.WRITER, recursive, registerAwaitingThreads);
   }

   /**
    * Constructs a new non-recursive RWEx registering waiting threads.
    *
    * @param priority waiting priority (WRITER or READERS)
    */
   public RWEx(Priority priority)
   {
      this(priority, false, true);
   }

   /**
    * Constructs a new non-recursive RWEx.
    *
    * @param priority waiting priority (WRITER or READERS)
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public RWEx(Priority priority, boolean registerAwaitingThreads)
   {
      this(priority, false, registerAwaitingThreads);
   }

   /**
    * Constructs a new RWEx.
    *
    * @param priority waiting priority (WRITER or READERS)
    * @param recursive if true, the created rwex will be recursive (non-recursive, if false)
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public RWEx(Priority priority, boolean recursive, boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      assert priority != Priority.TIME_OF_ARRIVAL: "TIME_OF_ARRIVAL priority not yet implemented!";

      this.priority = priority;
      this.recursive = recursive;
      readersSet = new ConcurrentLinkedQueue<Thread>();
   }

   /**
    * Current priority.
    *
    * @return the priority value
    */
   public synchronized Priority priority()
   {
      return priority;
   }

   /**
    * Change current priority.
    *
    * @param priority waiting priority (WRITER or READERS)
    */
   public synchronized void changePriority(Priority priority)
   {
      assert priority != Priority.TIME_OF_ARRIVAL: "TIME_OF_ARRIVAL priority not yet implemented!";

      if (priority != this.priority)
      {
         this.priority = priority;
         super.broadcast();
      }
   }

   /**
    * Reader lock.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code readerLockIsMine()}</DD>
    * </DL>
    */
   public synchronized void lockReader()
   {
      assert !lockIsMine() || recursive(): "lock is already mine!";

      if (!lockIsMine())
      {
         waitingReaders++;
         switch(priority)
         {
            case WRITER:
               while (activeWriterCount > 0 || waitingWriters > 0 || changeToWriterRequestPending())
                  super.await();
               break;
            case READERS:
               while (activeWriterCount > 0)
                  super.await();
               break;
            case TIME_OF_ARRIVAL:
               assert false: "TIME_OF_ARRIVAL priority not yet implemented!";
               break;
         }
         waitingReaders--;
      }
      registerReader();

      assert readerLockIsMine();
   }

   /**
    * Writer lock.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code writerLockIsMine()}</DD>
    * </DL>
    */
   public synchronized void lockWriter()
   {
      assert !lockIsMine(): "lock is already mine";

      waitingWriters++;
      switch(priority)
      {
         case WRITER:
            while (activeWriterCount > 0 || activeReaders > 0)
               super.await();
            break;
         case READERS:
            while (activeWriterCount > 0 || activeReaders > 0 || waitingReaders > 0)
               super.await();
            break;
         case TIME_OF_ARRIVAL:
            assert false: "TIME_OF_ARRIVAL priority not yet implemented!";
            break;
      }
      registerWriter();
      waitingWriters--;

      assert writerLockIsMine();
   }

   /**
    * Try to lock as a reader (without waiting).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code result && readerLockIsMine() || !result && !readerLockIsMine()}</DD>
    * </DL>
    *
    * @return true, if locked, false otherwise
    */
   public synchronized boolean trylockReader()
   {
      assert !lockIsMine(): "lock is already mine";

      boolean result = false;
      switch(priority)
      {
         case WRITER:
            result = activeWriterCount == 0 && waitingWriters == 0 && !changeToWriterRequestPending();
            break;
         case READERS:
            result = activeWriterCount == 0;
            break;
         case TIME_OF_ARRIVAL:
            assert false: "TIME_OF_ARRIVAL priority not yet implemented!";
            break;
      }
      if (result)
         registerReader();

      assert result && readerLockIsMine() || !result && !readerLockIsMine();

      return result;
   }

   /**
    * Try to lock as a writer (without waiting).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code result && writerLockIsMine() || !result && !writerLockIsMine()}</DD>
    * </DL>
    *
    * @return true, if locked, false otherwise
    */
   public synchronized boolean trylockWriter()
   {
      assert !lockIsMine(): "lock is already mine";

      boolean result = false;
      switch(priority)
      {
         case WRITER:
            result = activeWriterCount == 0 && activeReaders == 0;
            break;
         case READERS:
            result = activeWriterCount == 0 && activeReaders == 0 && waitingReaders == 0;
            break;
         case TIME_OF_ARRIVAL:
            assert false: "TIME_OF_ARRIVAL priority not yet implemented!";
            break;
      }
      if(result)
         registerWriter();

      assert result && writerLockIsMine() || !result && !writerLockIsMine();

      return result;
   }

   /**
    * Unlocks active lock (which might be either a reader or a writer lock).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    */
   public synchronized void unlock()
   {
      assert lockIsMine(): "lock is not mine";

      if (readerLockIsMine())
         unlockReader();
      else
         unlockWriter();

      assert recursive() || !lockIsMine();
   }

   /**
    * Unlocks reader.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code readerLockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    */
   public synchronized void unlockReader()
   {
      assert readerLockIsMine(): "reader lock is not mine";

      unregisterReader();
      if (activeReaders == 0)
         super.broadcast();

      assert recursive() || !lockIsMine();
   }

   /**
    * Unlocks writer.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code writerLockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    */
   public synchronized void unlockWriter()
   {
      assert writerLockIsMine(): "writer lock is not mine";

      unregisterWriter();
      if (activeWriterCount == 0)
         super.broadcast();

      assert recursive() || !lockIsMine();
   }

   public synchronized boolean lockIsMine()
   {
      return readerLockIsMine() || writerLockIsMine();
   }

   /**
    * Is reader lock owned by me?
    *
    * @return true, if lock is mine
    */
   public synchronized boolean readerLockIsMine()
   {
      return (activeReaders > 0) && readersSet.contains(Thread.currentThread());
   }

   /**
    * Is writer lock owned by me?
    *
    * @return true, if lock is mine
    */
   public synchronized boolean writerLockIsMine()
   {
      return (activeWriterCount > 0) && (CThread.currentThreadID() == writerID);
   }

   /**
    * Is current rwex recursive?
    *
    * @return true, if rwex is recursive
    */
   public synchronized boolean recursive()
   {
      return recursive;
   }

   /**
    * Writer lock count of current lock.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code writerLockIsMine()}</DD>
    * </DL>
    *
    * @return the count value
    */
   public synchronized int writerLockCount()
   {
      assert writerLockIsMine(): "writer lock is not mine";

      return activeWriterCount;
   }

   /**
    * Create and return a new condition variable attached to current RWEx.
    *
    * @return the new condition variable
    */
   public synchronized RWExCV newCV()
   {
      return new RWExCV(this);
   }

   public synchronized SyncState getStateAndUnlock()
   {
      assert lockIsMine(): "lock is not mine";

      long st;
      if (activeWriterCount > 0)
      {
         st = 0x100000000L | activeWriterCount; // 33 bit is one
         while(activeWriterCount > 0)
            unlockWriter();
      }
      else
      {
         int count = 0;
         while(readerLockIsMine())
         {
            count++;
            unlockReader();
         }
         st = count; // 33 bit is zero
      }

      SyncState result = new SyncState(this, (Long)(st));

      assert !lockIsMine();

      return result;
   }

   public synchronized void recoverState(SyncState state) throws ThreadInterruptedException
   {
      assert state != null && state.obj() == this;
      assert !lockIsMine(): "lock is already mine";

      long st = (Long)state.state();
      int count = (int)(st & 0x0FFFFFFFFL);
      if ((st & 0x100000000L) != 0) // writer lock
      {
         for(int i = 0; i < count; i++)
            lockWriter();
      }
      else
      {
         for(int i = 0; i < count; i++)
            lockReader();
      }

      assert lockIsMine();
   }

   /**
    * Change from a writer lock to a reader lock.
    * <DL><DT><B>Precondition:</B></DT>
    * <DD>{@code writerLockIsMine()}</DD>
    * <DD>{@code !recursive() || writerLockCount() == 1}</DD></DL>
    * <DL><DT><B>Postcondition:</B></DT>
    * <DD>{@code readerLockIsMine()}</DD></DL>
    */
   public synchronized void changeToReader()
   {
      assert writerLockIsMine(): "writer lock is not mine";
      assert !recursive() || writerLockCount() == 1: "recursive lock with counter greater than one not allowed";

      unregisterWriter();
      registerReader();
      super.broadcast(); // to wakeup possible waiting readers

      assert readerLockIsMine();
   }

   /**
    * Attempts to change from a reader lock to a writer lock.
    * <DL><DT><B>Precondition:</B></DT>
    * <DD>{@code readerLockIsMine()}</DD>
    * <DD>{@code !recursive()}</DD></DL>
    * <DL><DT><B>Postcondition:</B></DT>
    * <DD>{@code result && writerLockIsMine() || !result && readerLockIsMine()}</DD></DL>
    *
    * @return true, if lock change succeeds, false otherwise
    */
   public synchronized boolean tryToChangeToWriter()
   {
      assert readerLockIsMine(): "reader lock is not mine";
      assert !recursive(): "recursive lock not allowed";

      if (Thread.currentThread().isInterrupted())
         throw new ThreadInterruptedException();

      boolean result = (changeToWriterID == -1L); // nobody else waiting to change

      if (result)
      {
         changeToWriterID = CThread.currentThreadID();
         waitingWriters++;
         while (activeReaders > 1)
            super.await();
         waitingWriters--;
         unregisterReader();
         registerWriter();
      }

      assert result && writerLockIsMine() || !result && readerLockIsMine();

      return result;
   }

   protected boolean changeToWriterRequestPending()
   {
      return changeToWriterID != -1L;
   }


   protected void registerReader()
   {
      activeReaders++;
      readersSet.add(Thread.currentThread());
   }

   protected void unregisterReader()
   {
      activeReaders--;
      readersSet.remove(Thread.currentThread());
   }

   protected void registerWriter()
   {
      if (activeWriterCount == 0)
         writerID = CThread.currentThreadID();
      activeWriterCount++;
   }

   protected void unregisterWriter()
   {
      activeWriterCount--;
      if (activeWriterCount == 0)
         writerID = -1L;
   }

   protected final boolean recursive;
   protected int activeReaders = 0;
   protected int waitingReaders =  0;
   protected int activeWriterCount = 0;
   protected int waitingWriters =  0;
   protected long changeToWriterID = -1L;
   protected Priority priority;
   protected final ConcurrentLinkedQueue<Thread> readersSet;
   protected long writerID = -1L;          // current writer
}

