package pt.ua.concurrent;

import static java.lang.System.*;
import java.util.Queue;
import java.util.LinkedList;

/**
 * An actor module (object-oriented approach for message passing concurrent programming).
 *
 * <P>This class implements, in an object-oriented way, the message passing
 * thread communication mechanism.
 * <BR><B>Important Note:</B> external synchronization is not yet implemented.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 * @version 0.6, November 2012
 */
public class Actor extends Thread
{
   /**
    * Constructs a new Actor (note that its attached thread is not started here).
    */
   public Actor()
   {
      pending = new LinkedList<Routine>();
      pendingCSync = new LinkedList<Routine>();
   }

   /**
    * routine calls pending for execution
    */
   protected final Queue<Routine> pending;

   /**
    * routine calls pending due to conditional synchronization
    */
   protected final Queue<Routine> pendingCSync;

   /**
    * Indicates that the routine attached to future is done.
    * (This method is to be used by actor descendant classes.)
    *
    *  <P><B>requires</B>: {@code future != null}
    * <BR><B>requires</B>: {@code isAlive()}
    *
    * @param future  the future object
    */
   protected void futureDone(Future future)
   {
      assert future != null: "Future object required";
      assert isAlive(): "Actor was not started!";

      future.routineDone();
   }

   private synchronized Routine outPendingRoutine()
   {
      Routine result;
      while(pending.isEmpty())
      {
         try { wait(); }
         catch(InterruptedException e)
         { throw new ThreadInterruptedException(e); }
      }
      result = pending.remove();
      return result;
   }

   /**
    * Queue's a routine for execution by the actor.
    * (This method is to be used by actor descendant classes.)
    *
    *  <P><B>requires</B>: {@code r != null}
    *
    * @param r  the routine object
    */
   protected synchronized void inPendingRoutine(Routine r)
   {
      assert r != null: "Routine object required";

      while(!isAlive()) // conditional synchronization
      {
         try { wait(); }
         catch(InterruptedException e)
         { throw new ThreadInterruptedException(e); }
      }

      pending.add(r);
      notifyAll();
   }

   protected boolean terminated = false;

   /**
    * Terminate te execution of actor.
    * The actor will terminate as soon as its pending queue is empty.
    *
    *  <P><B>requires</B>: {@code isAlive()}
    */
   public synchronized void terminate()
   {
      assert isAlive(): "Actor was not started!";

      terminated = true;
      interrupt();
   }

   /**
    * Is the actor terminated?
    *
    * @return {@code boolean} true if the actor has finished, false otherwise
    */
   public synchronized boolean terminated()
   {
      assert isAlive(): "Actor was not started!";

      return pending.isEmpty() && terminated;
   }

   // external synchronization is incomplete!
   /*
   protected Thread owner = null;

   private synchronized boolean grabIsMine()
   {
      return owner != null && owner == Thread.currentThread();
   }

   private synchronized void grab()
   {
      assert !grabIsMine(): "Actor is already grabbed by me";

      while(!isAlive()) // conditional synchronization
      {
         try { wait(); }
         catch(InterruptedException e)
         { throw new ThreadInterruptedException(e); }
      }

      try
      {
         while(owner != null)
            wait();
         owner = Thread.currentThread();
      }
      catch(InterruptedException e)
      {
         throw new ThreadInterruptedException(e);
      }
   }

   private synchronized void release()
   {
      assert grabIsMine(): "Actor not grabbed by me";

      owner = null;
      notifyAll();
   }
   */

   protected boolean runUniqueInvocation = true;

   /**
    * Has run not executed yet?
    * (This method is only for internal usage to ensure that run method is
    * executed only by the thread's start method.)
    *
    * @return {@code boolean} true if run has not yet started execution, false otherwise
    */
   protected boolean runUniqueInvocation()
   {
      return runUniqueInvocation;
   }

   /**
    * The actor's message handling routine.
    * (This method is to be executed only by thread's start method.)
    */
   public void run()
   {
      assert isAlive(): "Actor was not started!";
      assert runUniqueInvocation(): "Method run can only be executed once by start()";

      synchronized(this)
      {
         notifyAll(); // to notify possible isAlive waiting (inPendingRoutine)
      }

      runUniqueInvocation = false;
      out.println("Actor started...");
      while(!terminated())
      {
         Routine r = null;
         boolean interrupted = false;
         try
         {
            r = outPendingRoutine();
         }
         catch(ThreadInterruptedException e)
         {
            interrupted = true;
         }
         if (!interrupted)
         {
            if (!r.concurrentPrecondition())
               pendingCSync.add(r); // conditional synchronization
            else
            {
               r.execute();
               boolean precondition = true;
               while (precondition && !pendingCSync.isEmpty())
               {
                  r = pendingCSync.peek();
                  precondition = r.concurrentPrecondition();
                  if (precondition)
                  {
                     pendingCSync.remove();
                     r.execute();
                  }
               }
            }
         }
      }
      out.println("Actor terminated...");
   }

   /**
    * ADT of actor's routines closure.
    */
   protected abstract class Routine
   {
      /**
       * Routine's concurrent precondition (when false and the actor
       * is not grabbed, it is implemented by the actor as a conditional
       * synchronization point).
       *
       * @return {@code boolean} true if precondition is met
       */
      public boolean concurrentPrecondition() { return true; }

      /**
       * Actor's routine implementation (it is executed by the actor's thread).
       */
      public abstract void execute();
   }
}

