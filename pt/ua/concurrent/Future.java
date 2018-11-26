package pt.ua.concurrent;

/**
 * Actor's Futures class.
 *
 * <P>This class handles future results from actor routines.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class Future<T>
{
   /**
    * Constructs a new future (either for a function or a procedure).
    *
    * @param isFunction  true if is a function future (with a result), or false for a procedure
    */
   public Future(boolean isFunction)
   {
      this.isFunction = isFunction;
   }

   /**
    * Is this future applied to a function?
    *
    * @return {@code boolean} true if is a function future, otherwise it is a procedure future
    */
   public synchronized boolean isFunction()
   {
      return isFunction;
   }

   /**
    * Waits until the actor's routine attached to this future is done.
    * This routine implements an eager remote invocation behavior.
    */
   public synchronized void done()
   {
      try
      {
         while(!isDone)
            wait();
      }
      catch(InterruptedException e)
      {
         throw new ThreadInterruptedException(e);
      }
   }

   /**
    * The result of the actor's function attached to this future.
    * If necessary, it waits (done) until the function is done.
    *
    *  <P><B>requires</B>: {@code isFunction()}
    *
    * @return {@code T} the function's result
    */
   public synchronized T result()
   {
      assert isFunction();

      done();
      return res;
   }

   synchronized void routineDone() // package visibility (due to lack of selective exports)
   {
      isDone = true;
      notifyAll();
   }

   /**
    * Sets the result of the actor's function attached to this future.
    * This method should only be used by actor's!
    *
    *  <P><B>requires</B>: {@code isFunction()}
    *
    * @param res  the function's result
    */
   public synchronized void setResult(T res)
   {
      assert isFunction();

      this.res = res;
   }

   protected final boolean isFunction;
   protected T res;
   protected boolean isDone;
}

