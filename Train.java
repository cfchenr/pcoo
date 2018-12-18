import java.util.*;

import javax.net.ssl.TrustManager;

import pt.ua.concurrent.*;

public class Train implements Runnable {

    private int number, mContentores, velocity;
    private Terminal source, destination;
    private Stack<Contentor> contentores;
    private CCO cco;
    private boolean close, services, sServices, dServices, permitionToLoad, permitionToUnload, permitionToWait;
    private Position position;
    private String state;

    public Train (int number, Terminal source, Terminal destination, CCO cco) throws InterruptedException {

        this.number = number;
        this.source = source;
        this.destination = null;

        mContentores = (int)(Math.random()*((40 - 15) + 1) + 15);
        velocity = (int)(Math.random()*((180 - 110) + 1) + 110);

        this.contentores = new Stack<Contentor>();
        this.cco = cco;

        close = false;
        services = false;
        sServices = false;
        dServices = false;

        permitionToLoad = false;
        permitionToUnload = false; 
        permitionToWait = false;    

        position = source.gPosition();

        state = "stop";
        cco.aTrain(this);

    }

    public void run () {

        try {

            do {

                // Se o comboio estiver carregado, aguarda permissão para descarregar, descarrega e pára (aguarda por novos serviços)
                if (contentores.size() > 0) {

                    do {

                        state = "waiting for unloading";

                        cco.suPermition(this);

                    } while (!permitionToUnload);

                    state = "unloading";

                    source.sUnload(this);

                    sServices = false;
                    dServices = false;
                    state = "stop";

                // Se estiver descarregado e se há serviços onde está, aguarda permissão para carregar, carrega e vai para o seu destino
                } else if (sServices) {

                    do {

                        state = "waiting for load";

                        cco.slPermition(this);

                    } while (!permitionToLoad);

                    state = "loading";   

                    source.sLoad(this);

                    goToDestination();             

                // Se estiver descarregado e se há serviços noutro local, vai para lá
                } else if (dServices) {

                    goToDestination();

                    sServices = false;
                    dServices = false;

                // Se estiver descarregado e não há serviços, pára
                } else {

                    state = "stop";

                }
                
            } while (true);


        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    public void cDirection () throws InterruptedException {

        Thread.sleep((long)(1250));
        Terminal temp = source;
        source = destination;
        destination = temp;

    }

    public void goToDestination () throws InterruptedException {

        state = "in transit";

        Thread.sleep((long)(3000*(10*source.gPosition().gDistance(destination.gPosition()))/velocity));
        position = destination.gPosition();

        cDirection();

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

    public void sDestination (Terminal destination) {

        this.destination = destination;

    }

    public int gmContentores () {

        return mContentores;

    }

    public Stack<Contentor> gContentores () {

        return contentores;

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

    public Position gPosition () {

        return position;

    }

    public String gState () {

        return state;
        
    }

    public void sState (String state) {

        this.state = state;

    }

    public void sServices (boolean tf) {

        sServices = tf;
        dServices = !tf;

    }

    public void nsServices () {

        sServices = false;
        dServices = false;

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

    public void sPosition (Position position) {

        this.position = position;

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