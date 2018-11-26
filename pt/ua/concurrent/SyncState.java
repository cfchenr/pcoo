package pt.ua.concurrent;

public class SyncState
{
   // package visibility (to be created only within pt.ua.concurrent Sync classes)!
   SyncState(Sync obj, Object state)
   {
      assert obj != null;

      this.obj = obj;
      this.state = state;
   }

   public Sync obj()
   {
      return obj;
   }

   public Object state()
   {
      return state;
   }

   protected final Sync obj;
   protected final Object state;
}

