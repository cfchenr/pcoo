import java.util.*;
import pt.ua.concurrent.*;

public class Transport implements Runnable {

    private int number;
    protected int mContainers, velocity;
    private Infrastructure source, destination;
    private Stack<Container> containers;
    private CCO cco;
    private boolean sServices, dServices, permitionToLoad, permitionToUnload, permitionToWait;
    private Position position;
    private String state;

    public Transport(int number, Infrastructure source, Infrastructure destination, CCO cco) {

        sState("init");
        sNumber(number);
        sSource(source);
        sPosition(gSource().gPosition());
        sDestination(null);
        sContainers(new Stack<Container>());
        nsServices();
        slPermition(false);
        suPermition(false);
        swPermition(false);
        sCCO(cco);
        gCCO().aTransport(this);

    }

    public void run() {

        try {

            do {

                // Se o comboio estiver carregado, aguarda permissão para descarregar,
                // descarrega e pára (aguarda por novos serviços)
                if (containers.size() > 0) {

                    do {

                        state = "waiting for unloading";

                        cco.suPermition(this);

                    } while (!permitionToUnload);

                    state = "unloading";

                    source.sUnload(this);

                    sServices = false;
                    dServices = false;
                    state = "stop";

                    // Se estiver descarregado e se há serviços onde está, aguarda permissão para
                    // carregar, carrega e vai para o seu destino
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

    public void cDirection() throws InterruptedException {

        Thread.sleep((long) (1250));
        Infrastructure temp = source;
        source = destination;
        destination = temp;

    }

    public void goToDestination() throws InterruptedException {

        state = "in transit";

        Thread.sleep((long) (3000 * (10 * source.gPosition().gDistance(destination.gPosition())) / velocity));
        position = destination.gPosition();

        cDirection();

    }

    public void sNumber(int number) {

        this.number = number;

    }

    public int gNumber() {

        return number;

    }

    public void sSource(Infrastructure source) {

        this.source = source;

    }

    public Infrastructure gSource() {

        return source;

    }

    public void sDestination(Infrastructure destination) {

        this.destination = destination;

    }

    public Infrastructure gDestination() {

        return destination;

    }

    public void sPosition(Position position) {

        this.position = position;

    }

    public Position gPosition() {

        return position;

    }

    public void sState(String state) {

        this.state = state;

    }

    public String gState() {

        return state;

    }

    public void sServices(boolean tf) {

        ssServices(tf);
        sdServices(!tf);

    }

    public void nsServices() {

        ssServices(false);
        sdServices(false);

    }

    public boolean gServices() {

        return (gsServices() && gdServices());

    }

    public void ssServices(boolean tf) {

        sServices = tf;

    }

    public boolean gsServices() {

        return sServices;

    }

    public void sdServices(boolean tf) {

        dServices = tf;

    }

    public boolean gdServices() {

        return dServices;

    }

    public void slPermition(boolean tf) {

        permitionToLoad = tf;

    }

    public boolean glPermition() {

        return permitionToLoad;

    }

    public void suPermition(boolean tf) {

        permitionToUnload = tf;

    }

    public boolean guPermition() {

        return permitionToUnload;

    }

    public void swPermition(boolean tf) {

        permitionToWait = tf;

    }

    public boolean gwPermition() {

        return permitionToWait;

    }

    public void sContainers(Stack<Container> containers) {

        this.containers = containers;

    }

    public Stack<Container> gContainers() {

        return containers;

    }

    public void aContainer(Container contentor) {

        containers.push(contentor);

    }

    public void smContainers(int mContainers) {

        this.mContainers = mContainers;

    }

    public int gmContainers() {

        return mContainers;

    }

    public void sVelocity(int velocity) {

        this.velocity = velocity;

    }

    public int gVelocity() {

        return velocity;

    }

    public void sCCO(CCO cco) {

        this.cco = cco;

    }

    public CCO gCCO() {

        return cco;

    }

    public boolean isFull() {

        return containers.size() == mContainers;

    }

    public boolean isEmpty() {

        return containers.size() == 0;

    }

}