package pt.ua.concurrent;

/**
 * Java's interface for locks.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 */
public interface Sync extends InterruptibleAwaitingThreads
{
   /**
    * Is lock owned by me?
    *
    * @return true, if lock is mine
    */
   public boolean lockIsMine();

   /**
    * Create and return a new condition variable attached to current Sync.
    *
    * @return the new condition variable
    */
   public SyncCV newCV();

   /**
    * Get current Sync state, and unlock it (if applicable).
    * (Internal service).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()}</DD>
    * </DL>
    *
    * @return the Sync state object.
    */
   public SyncState getStateAndUnlock();

   /**
    * Recover Sync state.
    * (Internal service).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code state != null && state.obj() == this} - my state object required</DD>
    *    <DD>{@code !lockIsMine()}</DD>
    * </DL>
    *
    * @param state Sync state object (returned by getStateAndUnlock)
    */
   public void recoverState(SyncState state) throws ThreadInterruptedException;
}
