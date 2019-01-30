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

                // Se o transporte estiver carregado, aguarda permissão para descarregar,
                // descarrega e pára (aguarda por novos serviços).
                if (gContainers().size() > 0) {

                    do {

                        sState("waiting for unloading");

                        gCCO().suPermition(this);

                    } while (!guPermition());

                    sState("unloading");

                    gSource().sUnload(this);

                    nsServices();

                    sState("stop");

                    // Se estiver descarregado e se há serviços onde está, aguarda permissão para
                    // carregar, carrega e vai para o seu destino.
                } else if (gsServices()) {

                    do {

                        sState("waiting for load");

                        gCCO().slPermition(this);

                    } while (!glPermition());

                    sState("loading");

                    gSource().sLoad(this);

                    gtDestination();

                    // Se estiver descarregado e se há serviços noutro local, vai para lá.
                } else if (gdServices()) {

                    gtDestination();

                    nsServices();

                    // Se estiver descarregado e não há serviços, pára.
                } else {

                    sState("stop");

                }

            } while (true);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    // Mudança de direção, ou seja, o seu destino passa a ser a origem e vice-versa.
    public void cDirection() throws InterruptedException {

        Thread.sleep((long) (1250));
        Infrastructure temp = gSource();
        sSource(gDestination());
        sDestination(temp);

    }

    // Faz o transporte avançar para o seu destino.
    public void gtDestination() throws InterruptedException {

        sState("in transit");

        Thread.sleep((long) (3000 * (10 * gSource().gPosition().gDistance(gDestination().gPosition())) / gVelocity()));
        sPosition(gDestination().gPosition());

        cDirection();

    }

    // Define o número do transporte.
    public void sNumber(int number) {

        this.number = number;

    }

    // Obtém o número do transporte.
    public int gNumber() {

        return number;

    }

    // Define a origem do transporte.
    public void sSource(Infrastructure source) {

        this.source = source;

    }

    // Obtém a origem do transporte.
    public Infrastructure gSource() {

        return source;

    }

    // Define o destino do transporte.
    public void sDestination(Infrastructure destination) {

        this.destination = destination;

    }

    // Obtém o destino do transporte.
    public Infrastructure gDestination() {

        return destination;

    }

    // Define a posição do transporte.
    public void sPosition(Position position) {

        this.position = position;

    }

    // Obtém a posição do transporte.
    public Position gPosition() {

        return position;

    }

    // Define o estado do transporte.
    public void sState(String state) {

        this.state = state;

    }

    // Obtém o estado do transporte.
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

        gContainers().push(contentor);

    }

    public Container rContainer () {

        return gContainers().pop();

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

    // Obtém se o transporte está cheio.
    public boolean isFull() {

        return gContainers().size() == gmContainers();

    }

    // Obtém se o transporte está vazio.
    public boolean isEmpty() {

        return gContainers().size() == 0;

    }

}