package pt.ua.concurrent;

/**
 * Bounded semaphore class (a semaphore that cannot exceed a given value).
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class BoundedSemaphore extends Semaphore
{
   /**
    * Creates a semaphore bounded by a maximum value of maxCount
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code maxCount > 0} - positive bound</DD>
    * </DL>
    *
    * @param maxCount bound value
    */
  public BoundedSemaphore(int maxCount)
  {
    super();

    assert maxCount > 0;

    this.maxCount = maxCount;
  }

   /**
    * Creates a semaphore bounded by a maximum value of maxCount and
    * starting with value initialCount
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code maxCount > 0} - positive bound</DD>
    *    <DD>{@code initialCount >= 0 && initialCount <= maxCount} - valid count</DD>
    * </DL>
    *
    * @param maxCount bound value
    * @param initialCount initial value
    */
  public BoundedSemaphore(int maxCount, int initialCount)
  {
    super(initialCount);

    assert maxCount > 0;
    assert initialCount >= 0 && initialCount <= maxCount;

    this.maxCount = maxCount;
  }

   /**
    * Releases the semaphore.
    *
    * <DL><DT><B>Precondition:</B></DT>
    *    <DD>{@code count < maxCount} - counter within bound limits</DD>
    * </DL>
    */
  public synchronized void release()
  {
    assert count < maxCount;

    super.release();
  }

   /**
    * Bound value.
    *
    * @return the value
    */
  public int maxCount()
  {
     return maxCount;
  }

  protected final int maxCount;
}
