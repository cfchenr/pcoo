package pt.ua.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Group mutex.
 *
 * <P>This class implements a group mutual exclusion scheme in which threads
 * within the same (active) group can proceed concurrently, and threads
 * from other groups are required to wait until their group becomes active.
 *
 * <P>The correct algorithm pattern to use GroupMutex objects should use {@code try/finally} blocks:
 * <pre>
 *    gmtx.lock(group);
 *    try
 *    {
 *       ...
 *    }
 *    finally
 *    {
 *       gmtx.unlock(group);
 *    }
 * </pre>
 *
 * <P>This class follows DbC(tm) methodology
 * (@see <a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class GroupMutex extends CObject implements Sync
{
   /**
    * Locking priority choices:
    * <BR><B>CURRENT_ACTIVE_GROUP</B>: active group is always preferred;
    * <BR><B>GROUP_NUMBER_INCREASING</B>: the higher the group number the higher its priority;
    * <BR><B>GROUP_NUMBER_DECREASING</B>: the lower the group number the higher its priority;
    * <BR><B>TIME_OF_ARRIVAL</B>: priority ordered by decreasing waiting time (not yet implemented!).
    */
   public enum Priority
   {
      CURRENT_ACTIVE_GROUP,
      GROUP_NUMBER_INCREASING,
      GROUP_NUMBER_DECREASING,
      TIME_OF_ARRIVAL
   };

   /**
    * Constructs a new GroupMutex with {@code numberOfGroups} groups and
    * CURRENT_ACTIVE_GROUP priority.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code numberOfGroups >= 2}</DD>
    * </DL>
    *
    * @param numberOfGroups  number of groups
    */
   public GroupMutex(int numberOfGroups)
   {
      this(numberOfGroups, Priority.CURRENT_ACTIVE_GROUP, true);
   }

   /**
    * Constructs a new GroupMutex with {@code numberOfGroups} groups and
    * with a selected priority.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code numberOfGroups >= 2}</DD>
    * </DL>
    *
    * @param numberOfGroups  number of groups
    * @param priority  locking priority
    */
   public GroupMutex(int numberOfGroups, Priority priority)
   {
      this(numberOfGroups, priority, true);
   }

   /**
    * Constructs a new GroupMutex with {@code numberOfGroups} groups and
    * with a selected priority.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code numberOfGroups >= 2}</DD>
    * </DL>
    *
    * @param numberOfGroups  number of groups
    * @param priority  locking priority
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public GroupMutex(int numberOfGroups, Priority priority, boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      assert numberOfGroups >= 2: "Invalid number of groups: "+numberOfGroups;
      assert priority != Priority.TIME_OF_ARRIVAL: "TIME_OF_ARRIVAL priority not yet implemented";

      this.numberOfGroups = numberOfGroups;
      this.priority = priority;
      mutex = new Mutex();
      groupCVar = new MutexCV[numberOfGroups];
      waitingGroupCount = new int[numberOfGroups];
      for(int i = 0; i < numberOfGroups; i++)
      {
         groupCVar[i] = mutex.newCV();
         registerSyncCV(groupCVar[i]);
         waitingGroupCount[i] = 0;
      }
      activeGroup = 0;
      activeGroupThreadCount = 0;
      totalWaitingThreadsCount = 0;
      lockThreads = new ConcurrentLinkedQueue<Thread>();
   }

   /**
    * Current lock priority.
    *
    * @return lock priority
    */
   public Priority priority()
   {
      Priority result;

      mutex.lock();
      try
      {
         result = priority;
      }
      finally
      {
         mutex.unlock();
      }

      return result;
   }

   /**
    * Change current lock priority.
    *
    * @param priority  locking priority
    */
   public void changePriority(Priority priority)
   {
      assert priority != Priority.TIME_OF_ARRIVAL: "TIME_OF_ARRIVAL priority not yet implemented";

      mutex.lock();
      try
      {
         if (priority != this.priority)
         {
            this.priority = priority;
            for(int g = 1; g <= numberOfGroups; g++)
               if (waitingGroupCount[g-1] > 0)
                  groupCVar[g-1].broadcast();
         }
      }
      finally
      {
         mutex.unlock();
      }
   }

   /**
    * Number of groups.
    *
    * @return number of groups
    */
   public int numberOfGroups()
   {
      return numberOfGroups; // lock not needed because is final!
   }

   /**
    * Current active group (unsafe operation if lock is owned
    * by the caller thread).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()}</DD>
    * </DL>
    *
    * @return current lock group
    */
   public int activeGroup()
   {
      assert lockIsMine(): "Lock not owned by caller thread";

      return activeGroup; // lock not needed because is volatile!
   }

   protected boolean lockWaitCondition(int group)
   {
      boolean result = false;
      switch(priority)
      {
         case CURRENT_ACTIVE_GROUP:
            result = activeGroup > 0 && activeGroup != group;
            break;
         case GROUP_NUMBER_INCREASING:
            for(int g = group+1; !result && g <= numberOfGroups; g++)
               result = waitingGroupCount[g-1] > 0;
            if (!result)
               result = activeGroup > 0 && activeGroup != group;
            break;
         case GROUP_NUMBER_DECREASING:
            for(int g = group-1; !result && g >= 1; g--)
               result = waitingGroupCount[g-1] > 0;
            if (!result)
               result = activeGroup > 0 && activeGroup != group;
            break;
         case TIME_OF_ARRIVAL:
            assert false: "TIME_OF_ARRIVAL priority not yet implemented";
            break;
      }
      return result;
   }

   /**
    * Lock to a group waiting until succeeds, or is interrupted.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group >= 1 && group <= numberOfGroups()}</DD>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code lockIsMine() && activeGroup() == group}</DD>
    * </DL>
    * 
    * @param group  group number
    */
   public void lock(int group)
   {
      assert group >= 1 && group <= numberOfGroups(): "Invalid group number: "+group;
      assert !lockIsMine(): "Lock already owned by caller thread";

      mutex.lock();
      try
      {
         if (Thread.currentThread().isInterrupted())
            throw new ThreadInterruptedException();

         if (lockWaitCondition(group))
         {
            waitingGroupCount[group-1]++;
            totalWaitingThreadsCount++;
            while(lockWaitCondition(group))
               groupCVar[group-1].await();
            waitingGroupCount[group-1]--;
            totalWaitingThreadsCount--;
         }
         activeGroup = group;
         activeGroupThreadCount++;
         lockThreads.add(Thread.currentThread());
      }
      finally
      {
         mutex.unlock();
      }

      assert lockIsMine() && activeGroup() == group;
   }

   /**
    * Attempt to lock a group (no waiting involved).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group >= 1 && group <= numberOfGroups()}</DD>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !result || (lockIsMine() && activeGroup() == group)}</DD>
    * </DL>
    * 
    * @param group  group number
    *
    * @return true if lock succeeds, otherwise it returns false
    */
   public boolean tryLock(int group)
   {
      assert group >= 1 && group <= numberOfGroups(): "Invalid group number: "+group;
      assert !lockIsMine(): "Lock already owned by caller thread";

      boolean result;

      if (Thread.currentThread().isInterrupted())
         throw new ThreadInterruptedException();

      mutex.lock();
      try
      {
         result = lockWaitCondition(group);
         if (result)
         {
            activeGroup = group;
            activeGroupThreadCount++;
            lockThreads.add(Thread.currentThread());
         }
      }
      finally
      {
         mutex.unlock();
      }

      assert !result || (lockIsMine() && activeGroup() == group);

      return result;
   }

   /**
    * Unlocks from a group.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group >= 1 && group <= numberOfGroups()}</DD>
    *    <DD>{@code lockIsMine() && activeGroup() == group}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * 
    * @param group  group number
    */
   public void unlock(int group)
   {
      assert group >= 1 && group <= numberOfGroups(): "Invalid group number: "+group;
      assert lockIsMine() && activeGroup() == group: "Lock not owned by caller thread or group "+group+" not active";

      mutex.lock();
      try
      {
         activeGroupThreadCount--;
         if (activeGroupThreadCount == 0)
         {
            if (totalWaitingThreadsCount > 0)
            {
               for(int g = 1; g <= numberOfGroups;g++)
                  if (g != group && waitingGroupCount[g-1] > 0)
                     groupCVar[g-1].broadcast();
            }
            activeGroup = 0;
         }
         lockThreads.remove(Thread.currentThread());
      }
      finally
      {
         mutex.unlock();
      }

      assert !lockIsMine();
   }

   public boolean lockIsMine()
   {
      boolean result;
      mutex.lock();
      try
      {
         result = lockThreads.contains(Thread.currentThread());
      }
      finally
      {
         mutex.unlock();
      }
      return result;
   }

   /**
    * Create and return a new condition variable attached to current GroupMutex.
    *
    * @return the new condition variable
    */
   public GroupMutexCV newCV()
   {
      GroupMutexCV cv = new GroupMutexCV(this);
      registerSyncCV(cv);
      return cv;
   }

   /**
    * Create and return a new composed condition variable attached to current GroupMutex and to an array of Sync's.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code GroupMutexComposedCV.validList(list)}</DD>
    * </DL>
    *
    * @param list  the array of Sync objects
    *
    * @return the new condition variable
    */
   public GroupMutexComposedCV newCV(Sync[] list)
   {
      assert GroupMutexComposedCV.validList(list): "array of Sync objects is not valid!";

      GroupMutexComposedCV cv = new GroupMutexComposedCV(this, list);
      registerSyncCV(cv);
      return cv;
   }


   public SyncState getStateAndUnlock()
   {
      assert lockIsMine();

      SyncState result = null;
      mutex.lock();
      try
      {
         result = new SyncState(this, (Integer)activeGroup);
      }
      finally
      {
         mutex.unlock();
      }

      unlock(activeGroup);

      assert !lockIsMine();

      return result;
   }

   public void recoverState(SyncState state) throws ThreadInterruptedException
   {
      assert state != null && state.obj() == this;
      assert !lockIsMine();

      lock((Integer)state.state());

      assert lockIsMine();
   }

   protected final int numberOfGroups;
   protected Priority priority;
   protected final Mutex mutex;
   protected final MutexCV[] groupCVar;
   protected volatile int activeGroup;
   protected int activeGroupThreadCount;
   protected int totalWaitingThreadsCount;
   protected final int[] waitingGroupCount;
   protected final ConcurrentLinkedQueue<Thread> lockThreads;
}

