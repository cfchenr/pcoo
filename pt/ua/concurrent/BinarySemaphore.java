package pt.ua.concurrent;

/**
 * Binary semaphore class.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class BinarySemaphore extends BoundedSemaphore
{
   /**
    * Creates a binary semaphore with value 1
    */
   public BinarySemaphore()
   {
      super(1);
   }

   /**
    * Creates a binary semaphore with initial value initialCount
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code initialCount >= 0 && initialCount <= 1} - valid count</DD>
    * </DL>
    *
    * @param initialCount initial value
    */
   public BinarySemaphore(int initialCount)
   {
      super(1, initialCount);
   }
}
