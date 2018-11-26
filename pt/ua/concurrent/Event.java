package pt.ua.concurrent;

/**
 * A simple module supporting two valued events.
 *
 * An event is a double-valued variable ({@code 0} and {@code 1}),
 * in which a client may wait for one of this two states.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class Event extends CObject
{
   /**
    * Creates an event with initial state and registering waiting threads.
    *
    * @param initial state value
    */
   public Event(boolean initial)
   {
      this(initial, true);
   }

   /**
    * Creates an event with initial state.
    *
    * @param initial  the initial state
    * @param registerAwaitingThreads if true, threads are registered when waiting
    */
   public Event(boolean initial, boolean registerAwaitingThreads)
   {
      super(registerAwaitingThreads);

      state = initial;
   }

   /**
    * Set the event state (true).
    */
   public synchronized void set()
   {
      state = true;
      broadcast();
   }

   /**
    * Reset the event state (false).
    */
   public synchronized void reset()
   {
      state = false;
      broadcast();
   }

   /**
    * Toggle the event state.
    */
   public synchronized void toggle()
   {
      state = !state;
      broadcast();
   }

   /**
    * Does current state matches argument?
    *
    * This method should be used with utmost care, because its result,
    * depending on the event's usage, might change immediately after
    * it has been called (race condition).
    *
    * @param state  the state to test
    *
    * @return true, if state matches the argument, false otherwise
    */
   public synchronized boolean stateMatches(boolean state)
   {
      return this.state == state;
   }

   /**
    * Wait for a given state value.
    *
    * @param state value to wait for
    */
   public synchronized void await(boolean state) throws ThreadInterruptedException
   {
      while (this.state != state)
         super.await();
   }

   protected boolean state = false;
}

