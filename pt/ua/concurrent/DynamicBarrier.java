package pt.ua.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A dynamically sized thread barrier class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 * @version 0.6, December 2015
 */
public class DynamicBarrier extends InternalBarrier
{
   /**
    * Constructs a new dynamic barrier registering waiting threads.
    */
   public DynamicBarrier()
   {
      this(true);
   }

   /**
    * Constructs a new dynamic barrier.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public DynamicBarrier(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      waiting = 0;
      signedThreads = new ConcurrentLinkedQueue<Thread>();
   }

   /**
    * Is current thread registered in barrier?
    *
    * @return true, if thread is registered; false otherwise
    */
   public boolean isSignIn() // lock-free
   {
      return signedThreads.contains(Thread.currentThread());
   }

   /**
    * Add signature of current thread in the barrier.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code !isSignIn()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code isSignIn()}</DD>
    * </DL>
    */
   public void signIn() // lock-free
   {
      assert !isSignIn(); // no race-condition because it tests current thread

      signedThreads.add(Thread.currentThread());

      assert isSignIn(); // no race-condition because it tests current thread
   }

   /**
    * Remove signature of current thread in the barrier.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code isSignIn()}</DD>
    * </DL>
    * <DL><DT><B>Postcondition:</B></DT>
    *    <DD>{@code !isSignIn()}</DD>
    * </DL>
    */
   public void signOut() // lock-free
   {
      assert isSignIn(); // no race-condition because it tests current thread

      signedThreads.remove(Thread.currentThread());

      assert !isSignIn(); // no race-condition because it tests current thread
   }

   public int size() // lock-free
   {
      return signedThreads.size();
   }

   public synchronized int numberWaitingThreads()
   {
      return waiting;
   }

   /**
    * Caller will wait until all signed barrier threads are also waiting, situation
    * in which all of them are awakened.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code isSignIn()}</DD>
    * </DL>
    */
   public void await() throws ThreadInterruptedException
   {
      awaitCount();
   }

   /**
    * Caller will wait until all signed barrier threads are also waiting, situation
    * in which all of them are awakened.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code isSignIn()}</DD>
    * </DL>
    *
    * @return the current barrier {@link #count() count}
    */
   public synchronized long awaitCount() throws ThreadInterruptedException
   {
      assert isSignIn(): "caller thread is not signed in this barrier";

      assert numberWaitingThreads() < size(); // invariant

      while(release)
         awaitCObject();
      waiting++;
      try
      {
         if (waiting == size())
         {
            release = true;
            count.incrementAndGet();
            broadcast();
         }
         else
         {
            while(!release)
               awaitCObject();
         }
      }
      finally
      {
         waiting--;
         if (waiting == 0)
         {
            release = false;
            broadcast();
         }
      }

      return count.get();
   }

   protected int waiting;
   protected boolean release;
   protected final ConcurrentLinkedQueue<Thread> signedThreads;
}

