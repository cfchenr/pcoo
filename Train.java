import java.util.*;
import pt.ua.concurrent.*;

public class Train implements Runnable {

    private int number, mContentores;
    private Terminal source, destination;
    private Stack<Contentor> contentores;
    private CCO cco;
    private boolean services, permitionToLoad, permitionToUnload, permitionToWait, close;

    public Train (int number, Terminal source, Terminal destination, int mContentores, CCO cco) throws InterruptedException {

        this.number = number;
        this.source = source;
        this.destination = destination;
        this.mContentores = mContentores;
        this.contentores = new Stack<Contentor>();
        this.cco = cco;
        
        cco.sTrainClose(this);
        cco.sServices(this);
        permitionToLoad = false;
        permitionToUnload = false; 
        permitionToWait = false;    

    }

    public synchronized void run () {

        try {
    
            while (services) {

                if (services)
                    cco.slPermition(this);
                
                if (permitionToLoad)
                    source.sLoad(this);

                while (permitionToWait)
                    wait();

                cco.suPermition(this);

                if (permitionToUnload)
                    destination.sUnload(this);

                while (permitionToWait)
                        wait();

                cDirection();

                cco.sServices(this);

            }

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    public void cDirection () {

        Terminal temp = source;
        source = destination;
        destination = temp;

    }

    public int gNumber () {

        return number;

    }

    public Terminal gSource () {

        return source;
        
    }

    public Terminal gDestination () {

        return destination;

    }

    public int gmContentores () {

        return mContentores;

    }

    public Stack<Contentor> gContentores () {

        return contentores;

    }

    public boolean gClose () {

        return close;

    }

    public boolean gServices () {

        return services;

    }

    public boolean glPermition () {

        return permitionToLoad;

    }

    public boolean guPermition () {

        return permitionToUnload;

    }

    public boolean gwPermition () {

        return permitionToWait;

    }

    public void sClose (boolean tf) {

        close = tf;

    }

    public void sServices (boolean tf) {

        services = tf;

    }

    public void slPermition (boolean tf) {

        permitionToLoad = tf;

    }

    public void suPermition (boolean tf) {

        permitionToUnload = tf;

    }

    public void swPermition (boolean tf) {

        permitionToWait = tf;

    }

    public void aContentor (Contentor contentor) {

        contentores.push(contentor);

    }

    public boolean isFull () {

        return contentores.size() == mContentores;

    }

    public boolean isEmpty () {

        return contentores.size() == 0;

    }

}