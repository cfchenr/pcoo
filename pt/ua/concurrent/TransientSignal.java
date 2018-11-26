package pt.ua.concurrent;

/**
 * A transient signal module (signals thrown away if no one is waiting).
 * This mechanism is similar to the behavior of the signal/await of a condition variable.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class TransientSignal extends CObject implements Signal
{
   /**
    * Constructs a new TransientSignal registering waiting threads.
    */
   public TransientSignal()
   {
      this(true);
   }

   /**
    * Constructs a new TransientSignal.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public TransientSignal(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);
   }

   public synchronized void send()
   {
      if (waiting > 0)
      {
         arrived = true;
         signal();
      }
   }

   public synchronized void await()
   {
      while (!arrived)
      {
         waiting++;
         super.await();
         waiting--;
      }
      arrived = false;
   }

   protected boolean arrived = false;
   protected int waiting = 0;
}

