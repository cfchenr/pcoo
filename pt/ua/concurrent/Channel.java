package pt.ua.concurrent;

/**
 * A simple module support a (generic) communication channel between two threads.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class Channel<T>
{
   /**
    * Creates a new channel registering waiting threads..
    */
   public Channel()
   {
      putPermit = new BinarySemaphore(1);
      takePermit = new BinarySemaphore(0);
      taken = new BinarySemaphore(0);
   }

   /**
    * Put obj in channel.
    *
    * @param obj reference to the object to be transmitted
    */
   public void put(T obj)
   {
      putPermit.acquire();
      item = obj;
      takePermit.release();
      // wait for take
      taken.acquire();
   }

   /**
    * Get obj from channel.
    *
    * @return reference to the object received
    */
   public T take()
   {
      T result;

      takePermit.acquire();
      result = item;
      item = null;
      putPermit.release();
      // notify put
      taken.release();

      return result;
   }

   /**
    * Requests the interruption all threads blocked on current channel.
    */
   public void interruptWaitingThreads() throws ThreadInterruptedException
   {
      putPermit.interruptWaitingThreads();
      takePermit.interruptWaitingThreads();
      taken.interruptWaitingThreads();
   }

   protected BinarySemaphore putPermit;
   protected BinarySemaphore takePermit;
   protected BinarySemaphore taken;
   protected T item = null;
}

