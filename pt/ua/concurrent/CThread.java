package pt.ua.concurrent;

/**
 * Thread related static utilities.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.6, October 2013
 */
public class CThread extends Thread
{
   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    */
   public CThread()
   {
      super();

      target = null;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code target != null} - Runnable object reference</DD>
    * </DL>
    *
    * @param target {@code Runnable} to attach to thread
    */
   public CThread(Runnable target) 
   {
      super(target);

      assert target != null;

      this.target = target;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code name != null} - String object reference</DD>
    * </DL>
    *
    * @param name {@code String} ID to attach to thread
    */
   public CThread(String name) 
   {
      super(name);

      assert name != null;

      target = null;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param name {@code String} ID to attach to thread
    */
   public CThread(ThreadGroup group, String name) 
   {
      super(group, name);

      assert group != null;
      assert name != null;

      target = null;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code target != null} - Runnable object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    * </DL>
    *
    * @param target {@code Runnable} to attach to thread
    * @param name {@code String} ID to attach to thread
    */
   public CThread(Runnable target, String name) 
   {
      super(target, name);

      assert target != null;
      assert name != null;

      this.target = target;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code target != null} - Runnable object reference</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param target {@code Runnable} to attach to thread
    */
   public CThread(ThreadGroup group, Runnable target) 
   {
      super(group, target);

      assert group != null;
      assert target != null;

      this.target = target;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code target != null} - Runnable object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param target {@code Runnable} to attach to thread
    * @param name {@code String} ID to attach to thread
    */
   public CThread(ThreadGroup group, Runnable target, String name) 
   {
      super(group, target, name);

      assert group != null;
      assert target != null;
      assert name != null;

      this.target = target;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code target != null} - Runnable object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    *    <DD>{@code stackSize > 0L} - positive stack size</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param target {@code Runnable} to attach to thread
    * @param name {@code String} ID to attach to thread
    * @param stackSize future thread's stack size
    */
   public CThread(ThreadGroup group, Runnable target, String name, long stackSize) 
   {
      super(group, target, name, stackSize);

      assert group != null;
      assert target != null;
      assert name != null;
      assert stackSize > 0L;

      this.target = target;
      ctarget = null;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code target != null} - CRunnable object reference</DD>
    * </DL>
    *
    * @param ctarget {@code CRunnable} to attach to thread
    */
   public CThread(CRunnable ctarget) 
   {
      super(ctarget);

      assert ctarget != null;

      this.target = null;
      this.ctarget = ctarget;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code target != null} - CRunnable object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    * </DL>
    *
    * @param ctarget {@code CRunnable} to attach to thread
    * @param name {@code String} ID to attach to thread
    */
   public CThread(CRunnable ctarget, String name) 
   {
      super(ctarget, name);

      assert ctarget != null;
      assert name != null;

      this.target = null;
      this.ctarget = ctarget;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code target != null} - CRunnable object reference</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param ctarget {@code CRunnable} to attach to thread
    */
   public CThread(ThreadGroup group, CRunnable ctarget) 
   {
      super(group, ctarget);

      assert group != null;
      assert ctarget != null;

      this.target = null;
      this.ctarget = ctarget;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code target != null} - CRunnable object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param ctarget {@code CRunnable} to attach to thread
    * @param name {@code String} ID to attach to thread
    */
   public CThread(ThreadGroup group, CRunnable ctarget, String name) 
   {
      super(group, ctarget, name);

      assert group != null;
      assert ctarget != null;
      assert name != null;

      this.target = null;
      this.ctarget = ctarget;
   }

   /**
    * Constructor of a thread (direct replacement of Thread's constructor).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code group != null} - ThreadGroup object reference</DD>
    *    <DD>{@code target != null} - CRunnable object reference</DD>
    *    <DD>{@code name != null} - String object reference</DD>
    *    <DD>{@code stackSize > 0L} - positive stack size</DD>
    * </DL>
    *
    * @param group thread's {@code ThreadGroup}
    * @param ctarget {@code CRunnable} to attach to thread
    * @param name {@code String} ID to attach to thread
    * @param stackSize future thread's stack size
    */
   public CThread(ThreadGroup group, CRunnable ctarget, String name, long stackSize) 
   {
      super(group, ctarget, name, stackSize);

      assert group != null;
      assert ctarget != null;
      assert name != null;
      assert stackSize > 0L;

      this.target = null;
      this.ctarget = ctarget;
   }

   /**
    * Define the thread's termination policy.
    *
    * Four options are available:
    * <ul>
    * <li><code>TerminationPolicy.DEBUG</code>: a thread failure causes a stack dump and the termination of the whole program (default behavior)</li>
    * <li><code>TerminationPolicy.PROPAGATE</code>: a thread failure causes the propagation of the failure to the ``parent'' thread (through the <code>interrupt</code> service)</li>
    * <li><code>TerminationPolicy.IGNORE</code>: a thread failure is ignored for the whole program</li>
    * <li><code>TerminationPolicy.IGNORE_DEBUG</code>: stack dumped to console, but thread failure is ignored for the whole program</li>
    * </ul>
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !started()}</DD>
    * </DL>
    *
    * @param terminationPolicy policy (<code>TerminationPolicy.DEBUG</code>, <code>TerminationPolicy.PROPAGATE</code>, <code>TerminationPolicy.IGNORE</code>, <code>TerminationPolicy.IGNORE_DEBUG</code>)
    */
   public void setTerminationPolicy(TerminationPolicy terminationPolicy)
   {
      assert !started(): "Thread start was already requested!";

      this.terminationPolicy = terminationPolicy;
   }

   /**
    * Current CThread object's termination policy.
    *
    * @return current termination policy
    */
   public TerminationPolicy terminationPolicy()
   {
      return terminationPolicy;
   }

   /**
    * A <code>start</code> method imposing an explicit termination policy.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !started()}</DD>
    * </DL>
    *
    * @param terminationPolicy policy (<code>TerminationPolicy.DEBUG</code>, <code>TerminationPolicy.PROPAGATE</code>, <code>TerminationPolicy.IGNORE</code>, <code>TerminationPolicy.IGNORE_DEBUG</code>)
    * @see setTerminationPolicy
    */
   public void start(TerminationPolicy terminationPolicy)
   {
      assert !started(): "Thread start was already requested!";

      setTerminationPolicy(terminationPolicy);
      start();
   }

   /**
    * Replacement of default <code>start</code> method (required to properly implement termination policy).
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !started()}</DD>
    * </DL>
    */
   public void start()
   {
      assert !started(): "Thread start was already requested!";

      status = LifeCycleStatus.STARTED_ISSUED;
      if (terminationPolicy == TerminationPolicy.PROPAGATE)
         parent = currentThread();
      super.start();
   }

   /**
    * Was request to start thread issued?
    *
    * @return true, if request issued
    */
   public boolean started()
   {
      return status != LifeCycleStatus.CREATED;
   }

   /**
    * Thread terminated?
    *
    * @return true, if terminated
    */
   public boolean terminated()
   {
      return status == LifeCycleStatus.TERMINATED_WITH_SUCCESS || status == LifeCycleStatus.TERMINATED_WITH_FAILURE;
   }

   /**
    * Did thread failed (terminated with an exception) its execution?
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code terminated()}</DD>
    * </DL>
    *
    * @return true, if thread failed
    */
   public boolean failed()
   {
      assert terminated();

      return status == LifeCycleStatus.TERMINATED_WITH_FAILURE;
   }

   /**
    * Get the exception responsible for the thread's failure.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code terminated() && failed() && terminationPolicy() == TerminationPolicy.PROPAGATE}</DD>
    * </DL>
    *
    * @return the exception
    */
   public Throwable sourceException()
   {
      assert terminated() && failed() && terminationPolicy() == TerminationPolicy.PROPAGATE;

      return sourceException;
   }

   /**
    * Replacement of default {@code run} method to correctly handle exceptions,
    * and also to register thread execution start time.
    * The thread program must be defined in {@code arun} method.
    */
   public void run()
   {
      try
      {
         status = LifeCycleStatus.RUNNING;
         startTime = System.currentTimeMillis(); // store thread start execution time
         if (ctarget != null)
            ctarget.arun();
         else if (target != null)
            target.run();
         else
            arun();
         status = LifeCycleStatus.TERMINATED_WITH_SUCCESS;
      }
      catch(Throwable e)
      {
         status = LifeCycleStatus.TERMINATED_WITH_FAILURE;
         if (ctarget != null)
            ctarget.registerFailure(e);
         switch(terminationPolicy)
         {
            case DEBUG:
               e.printStackTrace();
               System.exit(1);
               break;
            case PROPAGATE:
               //assert parent != null;
               sourceException = e;
               parent.interrupt();
               break;
            case IGNORE:
               break;
            case IGNORE_DEBUG:
               e.printStackTrace();
               break;
         }
      }
   }

   /**
    * The new thread program method.
    */
   public void arun()
   {
   }

   /**
    * Has thread start time been registered?
    *
    * @return true, if time registered
    */
   public boolean startTimeRegistered()
   {
      return startTime >= 0;
   }

   /**
    * Time instance at which the thread has started execution.
    * Requires the usage of {@code arun} method either through {@code CThread} or {@code CRunnable}.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code startTimeRegistered()}</DD>
    * </DL>
    *
    * @return time value
    */
   public long startTime()
   {
      assert startTimeRegistered(): "start time not registered!";

      return startTime;
   }

   /**
    * Elapsed time since {@code startTime}.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code startTimeRegistered()}</DD>
    * </DL>
    *
    * @return time value
    */
   public long elapsedTime()
   {
      assert startTimeRegistered(): "start time not registered!";

      return System.currentTimeMillis() - startTime;
   }

   /**
    * Thread's join replacement, in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException.
    */
   public void ajoin() throws ThreadInterruptedException
   {
      ajoin(0, 0);
   }

   /**
    * Thread's join replacement, in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code millis >= 0} - non negative timeout</DD>
    * </DL>
    *
    * @param millis timeout (in milliseconds)
    */
   public void ajoin(long millis) throws ThreadInterruptedException
   {
      assert millis >= 0: "Invalid millisecond timeout value ("+millis+")";

      ajoin(millis, 0);
   }

   /**
    * Thread's join replacement, in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code millis >= 0} - non negative timeout</DD>
    *    <DD>{@code nanos >= 0} - non negative timeout</DD>
    * </DL>
    *
    * @param millis timeout (in milliseconds)
    * @param nanos timeout (in nanoseconds)
    */
   public void ajoin(long millis, int nanos) throws ThreadInterruptedException
   {
      assert millis >= 0: "Invalid millisecond timeout value ("+millis+")";
      assert nanos >= 0: "Invalid nanosecond timeout value ("+nanos+")";

      try
      {
         super.join(millis, nanos);
      }
      catch(InterruptedException e)
      {
         throw new ThreadInterruptedException(e);
      }
   }

   /**
    * A thread safe pause service in which:<BR>
    * - the checked exception InterruptedException, is replaced by the unchecked exception ThreadInterruptedException.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code millis >= 0} - non negative pause value</DD>
    * </DL>
    *
    * @param millis pause value (in milliseconds)
    */
   public static void pause(int millis) throws ThreadInterruptedException
   {
      assert millis >= 0: "Invalid millisecond pause value ("+millis+")";

      try
      {
         Thread.sleep(millis);
      }
      catch(InterruptedException e)
      {
         throw new ThreadInterruptedException(e);
      }
   }

   /**
    * Current thread.
    *
    * @return the {@code Thread} object
    */
   public static Thread currentThread()
   {
      return Thread.currentThread();
   }

   /**
    * Current thread id.
    *
    * @return the thread id
    */
   public static long currentThreadID()
   {
      return Thread.currentThread().getId();
   }

   /**
    * Termination policy for thread created through current CThread's object.
    */
   public enum TerminationPolicy
   {
       /**
        * A thread failure causes a stack dump to the console and the
        * termination of the whole program (default behavior).
        */
      DEBUG,
       /**
       * A thread failure causes the propagation of the failure to the
       * ``parent'' thread (through the <code>interrupt</code> service).
        */
      PROPAGATE,
       /**
        * A thread failure is ignored for the whole program.
        */
      IGNORE,
       /**
        * A thread failure causes a stack dumped to console,
        * but thread failure is ignored for the whole program.
        */
      IGNORE_DEBUG
   }

   protected volatile long startTime = -1;
   protected final Runnable target; // necessary because the run method in CThread replaced run method in
                                    // Thread (which calls target when a Thread was created with a Runnable).
   protected final CRunnable ctarget;
   protected volatile TerminationPolicy terminationPolicy = TerminationPolicy.DEBUG; // default
   protected volatile Thread parent = null;
   protected volatile Throwable sourceException = null;
   protected volatile LifeCycleStatus status = LifeCycleStatus.CREATED;

   /**
    * Thread created by current CThread's object current life cycle status.
    */
   protected enum LifeCycleStatus
   {
       /**
       * CThread object created (before start method issued)
        */
      CREATED,
       /**
       * start method invoked but thread is not yet running
        */
      STARTED_ISSUED,
       /**
       * thread is running
        */
      RUNNING,
       /**
       * thread terminated with success (without an exception)
        */
      TERMINATED_WITH_SUCCESS,
       /**
       * thread terminated with a failure (with an exception)
        */
      TERMINATED_WITH_FAILURE
   }
}
