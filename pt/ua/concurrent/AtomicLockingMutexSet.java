package pt.ua.concurrent;

/**
 * Atomic locking operation on an unordered set of mutexes.
 *
 * <P>Locking on a set of mutexes in which waiting *without* locking on any of the mutexes.
 * A lock operation is applied only to a first mutex, and the remaining mutexe are locked with
 * trylock. A failure in one of these, implies that that mutex is putted in the head of the
 * mutex list, and a new attempt is tried (until it succeeds).
 *
 * The advantage of this compound locking module is that waiting (lock operation) is done in
 * the first mutex (hence there is no waiting with a locked mutex), and the mutex list elements
 * are permuted automatically to ensure this compound atomic locking).
 *
 * <P>The correct algorithm pattern to use AtomicLockingMutexSet objects should use {@code try/finally} blocks:
 * <pre>
 *    mstx.lock();
 *    try
 *    {
 *       ...
 *    }
 *    finally
 *    {
 *       mstx.unlock();
 *    }
 * </pre>
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2016
 */
public class AtomicLockingMutexSet extends CObject implements Sync
{
   /**
    * All the elements of the array are reference to objects.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code array != null && array.length > 0}</DD>
    * </DL>
    *
    * @param array the array
    * @return true, if all elements are defined
    */
   public static boolean allDefined(Mutex[] array)
   {
      assert array != null && array.length > 0: "invalid array - "+(array==null ? "null" : "lenght="+array.length)+"!";

      boolean result = true;
      for(int i = 0; result && i < array.length; i++)
         result = array[i] != null;
      return result;
   }

   /**
    * Constructs a AtomicLockingMutexSet with <tt>array</tt> elements.
    * Mutex recusivity is ignored (i.e. works as if a non-recursive locking mechanism).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code array != null && array.length > 0}</DD>
    *    <DD>{@code allDefined(array)}</DD>
    * </DL>
    *
    * @param array the array
    */
   public AtomicLockingMutexSet(Mutex[] array)
   {
      assert array != null && array.length > 0: "invalid array - "+(array==null ? "null" : "lenght="+array.length)+"!";
      assert allDefined(array): "invalid array - null element!";

      this.array = new Mutex[array.length];
      System.arraycopy(array, 0, this.array, 0, array.length);
      lockOrder = new int[array.length];
      for(int i = 0; i < array.length; i++)
         lockOrder[i] = i;
   }

   /**
    * Locks mutex.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code lockIsMine()}</DD>
    * </DL>
    */
   public void lock()
   {
      assert !lockIsMine(): "mutex-set lock is already mine!";

      boolean locked;
      do
      {
         array[lockOrder[0]].lock(); // waits without owning other lock (including synchronized(this))
         locked = true;
         synchronized(this) {
            int p = 0;
            for(int i = 1; locked && i < array.length; i++)
            {
               locked = array[lockOrder[i]].trylock();
               if (!locked)
               {
                  p = i;
                  for(i--;i >= 0; i--)
                     array[lockOrder[i]].unlock();
               }
            }
            if (!locked)
            {
               int tmp = lockOrder[0];
               lockOrder[0] = lockOrder[p];
               lockOrder[p] = tmp;
            }
         }
      }
      while(!locked);
      owner = CThread.currentThreadID(); // safe: volatile field

      //assert lockIsMine();
   }

   /**
    * Try to lock mutex (without waiting).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code result && lockIsMine() || !result && !lockIsMine()}</DD>
    * </DL>
    *
    * @return true, if mutex locked, false otherwise
    */
   public synchronized boolean trylock()
   {
      assert !lockIsMine(): "mutex-set lock is already mine!";

      boolean result = true;

      int p = 0;
      for(int i = 0; result && i < array.length; i++)
      {
         result = array[lockOrder[i]].trylock();
         if (!result && i > 0)
         {
            p = i;
            for(i--;i >= 0; i--)
               array[lockOrder[i]].unlock();
         }
      }

      if (result)
         owner = CThread.currentThreadID();
      else if (p > 0)
      {
         int tmp = lockOrder[0];
         lockOrder[0] = lockOrder[p];
         lockOrder[p] = tmp;
      }

      //assert lockIsMine();

      return result;
   }

   /**
    * Unlocks mutex.
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

         for(int i = array.length-1; i >= 0; i--)
            array[lockOrder[i]].unlock();
         owner = -1L;

         //assert !lockIsMine();
   }

   /**
    * Is lock owned by me?
    *
    * @return true, if lock is mine
    */
   public boolean lockIsMine()
   {
      return CThread.currentThreadID() == owner; // safe operation!
   }

   /**
    * Create and return a new condition variable attached to current AtomicLockingMutexSet.
    *
    * @return the new condition variable
    */
   public synchronized MutexCV newCV()
   {
      assert false: "Not yet implemented!";

      // TODO: create a MutexSetCV

      return null;
   }

   public synchronized SyncState getStateAndUnlock()
   {
      assert lockIsMine(): "lock is not mine";
         assert false: "Not yet implemented!";

         // TODO: 

         return null;
   }

   public synchronized void recoverState(SyncState state) throws ThreadInterruptedException
   {
      assert state != null && state.obj() == this;
      assert !lockIsMine(): "lock is mine";
         assert false: "Not yet implemented!";

         // TODO: 
   }

   protected final Mutex[] array;
   protected final int[] lockOrder;
   protected volatile long owner;
}

