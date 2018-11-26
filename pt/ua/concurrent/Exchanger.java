package pt.ua.concurrent;

/**
 * A simple module support a (generic) exchanger (bidirectional) communication channel between two threads.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class Exchanger<T> extends CObject
{
   /**
    * Constructs a new Exchanger registering waiting threads.
    */
   public Exchanger()
   {
      this(true);
   }

   /**
    * Constructs a new Exchanger.
    *
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public Exchanger(boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);
   }

   /**
    * Make the argument of a thread the result of the other (exchange values between two threads).
    *
    * @param obj reference to the object to be exchanged (send)
    * @return reference to the object exchanged (received)
    */
   public synchronized T exchange(T obj) throws ThreadInterruptedException
   {
      T result;

      while(exchangeActive)
         await();
      activeThreads++;
      if (activeThreads == 1) // first thread to arrive
      {
         argument = obj;
         while(activeThreads != 2)
            await();
         result = argument;
         argument = null;
         activeThreads--;
         broadcast();
      }
      else // if (activeThreads == 2) // second thread to arrive
      {
         // assert activeThreads == 2;

         exchangeActive = true;
         result = argument;
         argument = obj;
         broadcast();
         while(activeThreads != 1)
            await();
         activeThreads--;
         exchangeActive = false;
         broadcast();
      }

      return result;
   }

   protected int activeThreads = 0;
   protected boolean exchangeActive = false;
   protected T argument = null;
}

