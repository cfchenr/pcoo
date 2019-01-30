import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class Infrastructure {

    private String name;
    private Spot lSpot, uSpot;
    private WriteLogFile log;
    private ArrayList<Container> lContainers;
    private ArrayList<Container> uContainers;
    private CCO cco;
    private BinarySemaphore slMutex, suMutex;
    private boolean close;
    private Position position;

    public Infrastructure(String name, int x, CCO cco) {

        sName(name);
        slSpot(new Spot(1));
        suSpot(new Spot(2));
        slContainers(new ArrayList<Container>());
        suContainers(new ArrayList<Container>());
        slMutex = new BinarySemaphore(1);
        suMutex = new BinarySemaphore(1);
        sPosition(new Position(x));
        close = false;
        sCCO(cco);
        gCCO().aInfrastructure(this);

    }

    // Define o nome desta infraestrutura.
    private void sName(String name) {

        this.name = name;

    }

    // Retorna o nome desta infraestrutura.
    public String gName() {

        return name;

    }

    // Define o local de carregamento desta infraestrutura.
    private void slSpot(Spot lSpot) {

        this.lSpot = lSpot;

    }

    // Retorna o local de carregamento desta infraestrutura.
    public Spot glSpot() {

        return lSpot;

    }

    // Define o local de descarregamento desta infraestrutura.
    private void suSpot(Spot uSpot) {

        this.uSpot = uSpot;

    }

    // Retorna o local de descarregamento desta infraestrutura.
    public Spot guSpot() {

        return uSpot;

    }

    // Define a lista de containers a carregar desta infraestrutura.
    private void slContainers(ArrayList<Container> lContainers) {

        this.lContainers = lContainers;

    }

    // Retorna a lista de containers a carregar desta infraestrutura.
    public ArrayList<Container> glContainers() {

        return lContainers;

    }

    // Define a lista de containers a descarregar desta infraestrutura.
    private void suContainers(ArrayList<Container> uContainers) {

        this.uContainers = uContainers;

    }

    // Retorna a lista de containers a descarregar desta infraestrutura.
    public ArrayList<Container> guContainers() {

        return uContainers;

    }

    // Define a posição desta infraestutura.
    private void sPosition(Position position) {

        this.position = position;

    }

    // Retorna a posição desta infraestutura.
    public Position gPosition() {

        return position;

    }

    // Definir a infraestrutura como fechada ou aberta.
    public void sClose(boolean tf) throws InterruptedException {

        close = tf;

    }

    // Retorna o estado desta infraestrutura (fechada ou aberta).
    public boolean gClose() throws InterruptedException {

        return close;

    }

    // Obter e reservar os contentores a mover.
    public boolean ghmContainers(Transport transport) throws InterruptedException {

        boolean check = false;

        // Enquanto o local de descarregamento estiver ocupado, obtém o comboio que está
        // lá e se o seu estado for a descarregar ou em espera para descarregar, faz o
        // processo esperar.
        while (guSpot().gTransport() != null) {

            for (int i = 0; i < gCCO().gTransports().size(); i++)
                if (gCCO().gTransports().get(i).gPosition() == gPosition()
                        && (gCCO().gTransports().get(i).gState() == "unloading"
                                || gCCO().gTransports().get(i).gState() == "waiting for unload"))

                    Thread.sleep((long) (1250));

        }

        // Percorre todos os contentores que estão na lista de carregamento e quando
        // encontrar um que ainda não tenha nenhum transporte associado ou que o seu
        // transporte seja o transporte que efetuou o pedido, então passa a ser
        // verdadeiro o valor que define se existe ou não contentores a mover.
        for (int i = 0; i < glContainers().size(); i++)

            if (glContainers().get(i).gTransport() == null || glContainers().get(i).gTransport() == transport) {

                check = true;
                transport.sDestination(glContainers().get(i).gDestination());
                break;

            }

        if (check) {

            Stack<Infrastructure> select = new Stack<Infrastructure>();
            select.add(transport.gSource());
            select.add(transport.gDestination());

            int size = 0;
            boolean close = false;

            while (glContainers().size() > 0 && close == false) {

                // Reserva os contentores que estão disponíveis para carregamento (ou seja, não
                // tenham nenhum transporte associado e cujo o seu destino é o mesmo que o
                // destino do transporte ou um destino mais próximo do que do local onde se
                // encontra) enquanto o número de contentores a ser transportado não exceda a
                // capacidade máxima do transporte.
                for (int i = 0; i < glContainers().size(); i++) {

                    if (glContainers().get(i).gTransport() == null
                            && glContainers().get(i).gDestination() == select.peek()
                            && size < transport.gmContainers()) {

                        glContainers().get(i).sTransport(transport);
                        size++;

                    }

                    if (size == transport.gmContainers())
                        break;

                }

                // Se o transporte ainda não estiver lotado obtém uma infraestrutura que esteja
                // mais próxima do local de destino do transporte do que do local de origem do
                // transporte. (Este processo é repetido até percorrer todas as infraestruturas
                // ou o transporte estiver totalmente preenchido).
                if (size == transport.gmContainers())
                    break;

                Infrastructure selectT = this;
                close = true;

                for (int i = 0; i < gCCO().gInfrastructures().size(); i++)

                    if (!select.contains(gCCO().gInfrastructures().get(i))
                            && gPosition().gDistance(gCCO().gInfrastructures().get(i).gPosition()) > transport
                                    .gDestination().gPosition()
                                    .gDistance(gCCO().gInfrastructures().get(i).gPosition())) {

                        selectT = gCCO().gInfrastructures().get(i);
                        close = false;

                    }

                select.add(selectT);

            }

        }

        return check;

    }

    // Retorna se o local de carregamento está livre.
    public boolean glPermition() {

        return (lSpot.gReserve() == null);

    }

    // Retorna se o local de descarregamento está livre.
    public boolean guPermition() {

        return (uSpot.gReserve() == null);

    }

    // Reserva o local de carregamento para o @transport.
    public void srlSpot(Transport transport) {

        lSpot.sReserve(transport);

    }

    // Retorna o transporte que está no local de carregamento.
    public Transport grlSpot() {

        return lSpot.gReserve();

    }

    // Reserva o local de descarregamento para o @transport.
    public void sruSpot(Transport transport) {

        uSpot.sReserve(transport);

    }

    // Retorna o transporte que está no local de descarregamento.
    public Transport gruSpot() {

        return uSpot.gReserve();

    }

    // Quando descarregados, define o transporte associado a cada contentor como
    // "null".
    private void suContainers(Transport transport) throws InterruptedException {

        for (int i = 0; i < transport.gContainers().size(); i++)
            transport.gContainers().get(i).sTransport(null);

    }

    // Carrega o transporte.
    // O algoritmo implementado neste método segue a ideia do algoritmo implementado
    // no método ghmContainers(Transport transport), contudo neste método o
    // algoritmo remove os contentores da lista de carregamentos e adiciona-os ao
    // transporte em causa.
    public void sLoad(Transport transport) throws InterruptedException {

        slMutex.acquire();
        glSpot().aTransport(transport);

        Stack<Infrastructure> select = new Stack<Infrastructure>();
        select.add(transport.gSource());
        select.add(transport.gDestination());

        int size = 0;
        boolean close = false;

        while (size < transport.gmContainers() && close == false) {

            for (int i = 0; i < glContainers().size(); i++) {

                if ((glContainers().get(i).gTransport() == null || glContainers().get(i).gTransport() == transport)
                        && glContainers().get(i).gDestination() == select.peek() && size < transport.gmContainers()) {

                    transport.aContainer(glContainers().get(i));
                    glContainers().remove(i);
                    i--;
                    Thread.sleep((long) (1250));
                    size++;

                }

                if (size == transport.gmContainers())
                    break;

            }

            if (size == transport.gmContainers())
                break;

            Infrastructure selectT = this;
            close = true;

            for (int i = 0; i < gCCO().gInfrastructures().size(); i++)

                if (!select.contains(gCCO().gInfrastructures().get(i))
                        && gPosition().gDistance(gCCO().gInfrastructures().get(i).gPosition()) > transport
                                .gDestination().gPosition().gDistance(gCCO().gInfrastructures().get(i).gPosition())) {

                    selectT = gCCO().gInfrastructures().get(i);
                    close = false;

                }

            select.add(selectT);

        }

        glSpot().rTransport();
        slMutex.release();

    }

    // Descarrega o transporte.
    public void sUnload(Transport transport) throws InterruptedException {

        suMutex.acquire();
        // Informa que o transporte chegou ao local de descarregamento.
        guSpot().aTransport(transport);
        // Remove a associação entre os contentores transportados e o transporte, uma
        // vez que estes vão ser descarregados.
        suContainers(transport);

        // Enquanto o transporte não estiver vazio, se o destino dos contentores forem o
        // local onde se encontram, então vão para a lista de contentores decarregados,
        // caso contrário vão para a lista de contentores a carregar, para que um outro
        // transporte os leve para o seu destino ou um local mais próximo do seu
        // destino.
        while (!transport.isEmpty()) {

            if (transport.gContainers().peek().gDestination() == this)
                guContainers().add(transport.rContainer());

            else
                glContainers().add(transport.rContainer());

            Thread.sleep((long) (1250));

        }

        // Informa que o transporte terminou o descarregamento e remove-o do local de
        // descarregamento.
        guSpot().rTransport();
        suMutex.release();

    }

    // Adiciona contentores para carregar, a esta infraestrutura.
    public void alContainers(ArrayList<Container> containers) {

        for (int i = 0; i < containers.size(); i++)
            glContainers().add(containers.get(i));

    }

    // Define o CCO que gere esta infraestrutura.
    public void sCCO(CCO cco) {

        this.cco = cco;

    }

    // Retorna o CCO que gere esta infraestrutura.
    public CCO gCCO() {

        return cco;

    }

}