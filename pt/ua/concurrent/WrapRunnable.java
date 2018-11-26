package pt.ua.concurrent;

/**
 * Runnable wrap class.
 *
 * <P> Transforms a Runnable into a CRunnable.
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 */
public class WrapRunnable extends CRunnable
{
   public WrapRunnable(Runnable run)
   {
      assert run != null;

      this.run= run;
   }

   public void arun()
   {
      run.run();
   }

   protected final Runnable run;
}

