package pt.ua.concurrent;

/**
 * Java's interface to extend support for Interruptible Awaiting Threads.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 */
public interface InterruptibleAwaitingThreads
{
   /**
    * Are threads registered when waiting (in await)?
    *
    * @return true, if threads are registered
    */
   public boolean registerAwaitingThreads();

   /**
    * Requests the interruption all threads blocked on current blocking module.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code registerAwaitingThreads()} - registering of waiting threads activated</DD>
    * </DL>
    */
   public void interruptWaitingThreads() throws ThreadInterruptedException;
}
