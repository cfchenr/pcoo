package pt.ua.concurrent;

/**
 * A replacement for Runnable to handle exceptions (and other details).
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public abstract class CRunnable extends CObject implements Runnable
{
   /**
    * Default {@code run} method is simply to call {@code arun}.
    */
   public void run()
   {
      arun();
   }
   
   /**
    * The new thread program method (replaces {@code run}).
    */
   public abstract void arun();

   /**
    * Did {@code arun} terminated with an exception?
    *
    * @return true, if arun has terminated with an exception false otherwis
    */
   public boolean failed()
   {
      return exception != null;
   }

   /**
    * Exception object that has terminated {@code arun} execution.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code failed()}</DD>
    * </DL>
    *
    * @return the exception object which has terminated arun execution
    */
   public Throwable exception()
   {
      assert failed();

      return exception;
   }

   /**
    * Register an exception.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code exception != null}</DD>
    * </DL>
    *
    * @param exception  exception object that has terminated {@code arun} (registered by CThread)
    */
   void registerFailure(Throwable exception)
   {
      assert exception != null;

      this.exception = exception;
   }

   protected Throwable exception = null;
}

