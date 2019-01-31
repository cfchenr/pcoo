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
    private String type;

    public Transport(int number, Infrastructure source, CCO cco) {

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

        assert gSource() != gDestination();

        Infrastructure temp1 = gSource();
        Infrastructure temp2 = gDestination();

        Thread.sleep((long) (1250));
        Infrastructure temp = gSource();
        sSource(gDestination());
        sDestination(temp);

        assert (gSource() == temp2 && gDestination() == temp1);

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

        assert number >= 0;

        this.number = number;

        assert gNumber() == number;

    }

    // Obtém o número do transporte.
    public int gNumber() {

        return number;

    }

    // Define a origem do transporte.
    public void sSource(Infrastructure source) {

        assert source != null;

        this.source = source;

        assert gSource() == source;

    }

    // Obtém a origem do transporte.
    public Infrastructure gSource() {

        return source;

    }

    // Define o destino do transporte.
    public void sDestination(Infrastructure destination) {

        this.destination = destination;

        assert gDestination() == destination;

    }

    // Obtém o destino do transporte.
    public Infrastructure gDestination() {

        return destination;

    }

    // Define a posição do transporte.
    public void sPosition(Position position) {

        assert position != null;

        this.position = position;

        assert gPosition() == position;

    }

    // Obtém a posição do transporte.
    public Position gPosition() {

        return position;

    }

    // Define o estado do transporte.
    public void sState(String state) {

        assert state != null;

        this.state = state;

        assert gState().equals(state);

    }

    // Obtém o estado do transporte.
    public String gState() {

        return state;

    }

    // Definir o estado dos serviços deste transporte.
    public void sServices(boolean tf) {

        assert (tf || !tf);

        ssServices(tf);
        sdServices(!tf);

        assert (gsServices() || gdServices());

    }

    // Definir ambos os estados de serviços deste transporte como false.
    public void nsServices() {

        ssServices(false);
        sdServices(false);

        assert (!gsServices() && ! gdServices());

    }

    // Obter os estados dos serviços deste transporte.
    public boolean gServices() {

        return (gsServices() && gdServices());

    }

    // Definir o estado dos serviços na origem deste transporte.
    public void ssServices(boolean tf) {

        assert (tf || !tf);

        sServices = tf;

        assert ((gsServices() && tf) || (!gsServices() && !tf));

    }

    // Obter o estado dos serviços na origem deste transporte.
    public boolean gsServices() {

        return sServices;

    }

    // Definir o estado dos serviços no destino deste transporte.
    public void sdServices(boolean tf) {

        assert (tf || !tf);

        dServices = tf;

        assert ((gdServices() && tf) || (!gdServices() && !tf));

    }

    // Obter o estado dos serviços no destino deste transporte.
    public boolean gdServices() {

        return dServices;

    }

    // Definir a permição para carregar.
    public void slPermition(boolean tf) {

        assert (tf || !tf);

        permitionToLoad = tf;

        assert ((glPermition() && tf) || (!glPermition() && !tf));

    }

    // Obter a permição para carregar.
    public boolean glPermition() {

        return permitionToLoad;

    }

    // Definir a permição para descarregar.
    public void suPermition(boolean tf) {

        assert (tf || !tf);

        permitionToUnload = tf;

        assert ((guPermition() && tf) || (!guPermition() && !tf));

    }

    // Obter a permição para descarregar.
    public boolean guPermition() {

        return permitionToUnload;

    }

    // Definir a permição para esperar.
    public void swPermition(boolean tf) {

        assert (tf || !tf);

        permitionToWait = tf;

        assert ((gwPermition() && tf) || (!gwPermition() && !tf));

    }

    // Obter a permição para esperar.
    public boolean gwPermition() {

        return permitionToWait;

    }

    // Definir a lista de contentores deste transporte.
    public void sContainers(Stack<Container> containers) {

        assert containers != null;

        this.containers = containers;

        assert gContainers() == containers;

    }

    // Obter a lista de contentores deste transporte.
    public Stack<Container> gContainers() {

        return containers;

    }

    // Adicionar um contentor a este transporte.
    public void aContainer(Container contentor) {

        assert contentor != null;

        int size = gContainers().size();

        gContainers().push(contentor);

        assert (gContainers().size() == size + 1 && gContainers().contains(contentor));

    }

    // Remover um contentor a este transporte.
    public Container rContainer () {

        assert gContainers().size() > 0;

        int size = gContainers().size();

        Container temp = gContainers().pop();

        assert (gContainers().size() == size - 1 && !gContainers().contains(temp));

        return temp;

    }

    // Definir o número máximo de contentores deste transporte.    
    public void smContainers(int mContainers) {

        assert mContainers > 0;

        this.mContainers = mContainers;

        assert gmContainers() == mContainers;

    }

    // Obter o número máximo de contentores deste transporte.
    public int gmContainers() {

        return mContainers;

    }

    // Definir a velocidade deste transporte.
    public void sVelocity(int velocity) {

        assert velocity > 0;

        this.velocity = velocity;

        assert gVelocity() == velocity;

    }

    // Obter a velocidade deste transporte.
    public int gVelocity() {

        return velocity;

    }

    // Definir o CCO que gere deste transporte.
    public void sCCO(CCO cco) {

        assert cco != null;

        this.cco = cco;

        assert gCCO() == cco;

    }

    // Obter o CCO que gere deste transporte.
    public CCO gCCO() {

        return cco;

    }

    // Define o tipo deste transporte.
    public void sType(String type) {

        assert type != null;

        this.type = type;

        assert gType().equals(type);

    }

    // Retorna o tipo deste transporte.
    public String gType() {

        return type;

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