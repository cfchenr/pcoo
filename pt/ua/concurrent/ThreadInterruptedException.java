package pt.ua.concurrent;

/**
 * InterruptedException unchecked exception replacement.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, November 2011
 */
public class ThreadInterruptedException extends RuntimeException
{
   public ThreadInterruptedException()
   {
      super();
   }

   public ThreadInterruptedException(Throwable t)
   {
      super(t);
   }

   static final long serialVersionUID = 5353158321577702632L;
}
