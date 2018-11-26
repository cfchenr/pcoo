package pt.ua.concurrent;

/**
 * A persistent signal module (signals are kept until a successful delivery).
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class PersistentSignal extends CObject implements Signal
{
   /**
    * Constructs a new PersistentSignal registering waiting threads.
    */
   public PersistentSignal()
   {
      this(true);
   }

   /**
    * Constructs a new PersistentSignal.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public PersistentSignal(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);
   }

   public synchronized void send()
   {
      while (arrived)
         super.await();
      arrived = true;
      broadcast();
   }

   public synchronized void await()
   {
      while (!arrived)
         super.await();
      arrived = false;
      broadcast();
   }

   protected boolean arrived = false;
}

