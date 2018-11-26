package pt.ua.concurrent;

/**
 * Java's interface for condition variables.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 */
public interface SyncCV extends InterruptibleAwaitingThreads
{
   /**
    * Is lock owned by me?
    *
    * @return true, if lock is mine
    */
   public boolean lockIsMine();

   /**
    * Condition variable wait service.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()} - lock owned by me</DD>
    * </DL>
    */
   public void await() throws ThreadInterruptedException;

   /**
    * Condition variable signal (notify) service.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()} - lock owned by me</DD>
    * </DL>
    */
   public void signal();

   /**
    * Condition variable broadcast (notifyAll) service.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code lockIsMine()} - lock owned by me</DD>
    * </DL>
    */
   public void broadcast();

}
