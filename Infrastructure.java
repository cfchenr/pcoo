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

        this.cco = cco;

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

    public boolean ghmContainers(Transport transport) throws InterruptedException {

        boolean check = false;

        while (guSpot().gTransport() != null) {

            for (int i = 0; i < cco.gTransports().size(); i++)
                if (cco.gTransports().get(i).gPosition() == position
                        && (cco.gTransports().get(i).gState() == "unloading"
                                || cco.gTransports().get(i).gState() == "waiting for unload"))

                    Thread.sleep((long) (1250));

        }

        check = false;

        for (int i = 0; i < lContainers.size(); i++)

            if (lContainers.get(i).gTransport() == null || lContainers.get(i).gTransport() == transport) {

                check = true;
                transport.sDestination(lContainers.get(i).gDestination());
                break;

            }

        if (check) {

            Stack<Infrastructure> select = new Stack<Infrastructure>();
            select.add(transport.gSource());
            select.add(transport.gDestination());

            int size = 0;
            boolean close = false;

            while (lContainers.size() > 0 && close == false) {

                for (int i = 0; i < lContainers.size(); i++) {

                    if (lContainers.get(i).gTransport() == null && lContainers.get(i).gDestination() == select.peek()
                            && size < transport.gmContainers()) {

                        lContainers.get(i).sTransport(transport);
                        size++;

                    }

                    if (size == transport.gmContainers())
                        break;

                }

                if (size == transport.gmContainers())
                    break;

                Infrastructure selectT = this;
                double minDistance = position.gDistance(transport.gDestination().gPosition());
                close = true;

                for (int i = 0; i < cco.gInfrastructures().size(); i++)

                    if (!select.contains(cco.gInfrastructures().get(i))
                            && position.gDistance(cco.gInfrastructures().get(i).gPosition()) > transport.gDestination()
                                    .gPosition().gDistance(cco.gInfrastructures().get(i).gPosition())) {

                        selectT = cco.gInfrastructures().get(i);
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

    // Quando descarregados, define o transporte associado a cada contentore como
    // "null".
    private void suContainers(Transport transport) throws InterruptedException {

        for (int i = 0; i < transport.gContainers().size(); i++)
            transport.gContainers().get(i).sTransport(null);

    }

    // Carrega o transporte.
    public void sLoad(Transport transport) throws InterruptedException {

        slMutex.acquire();
        lSpot.aTransport(transport);

        Stack<Infrastructure> select = new Stack<Infrastructure>();
        select.add(transport.gSource());
        select.add(transport.gDestination());

        int size = 0;
        boolean close = false;

        while (size < transport.gmContainers() && close == false) {

            for (int i = 0; i < lContainers.size(); i++) {

                if ((lContainers.get(i).gTransport() == null || lContainers.get(i).gTransport() == transport)
                        && lContainers.get(i).gDestination() == select.peek() && size < transport.gmContainers()) {

                    transport.aContainer(lContainers.get(i));
                    lContainers.remove(i);
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
            double minDistance = position.gDistance(transport.gDestination().gPosition());
            close = true;

            for (int i = 0; i < cco.gInfrastructures().size(); i++)

                if (!select.contains(cco.gInfrastructures().get(i))
                        && position.gDistance(cco.gInfrastructures().get(i).gPosition()) > transport.gDestination()
                                .gPosition().gDistance(cco.gInfrastructures().get(i).gPosition())) {

                    selectT = cco.gInfrastructures().get(i);
                    close = false;

                }

            select.add(selectT);

        }

        lSpot.dTransport();
        slMutex.release();

    }

    // Descarrega o transporte.
    public void sUnload(Transport transport) throws InterruptedException {

        suMutex.acquire();
        suContainers(transport);
        uSpot.aTransport(transport);

        while (!transport.isEmpty()) {

            if (transport.gContainers().peek().gDestination() == this)
                uContainers.add(transport.gContainers().pop());

            else
                lContainers.add(transport.gContainers().pop());

            Thread.sleep((long) (1250));

        }

        uSpot.dTransport();
        suMutex.release();

    }

    // Adiciona containers para carregar, a esta infraestrutura.
    public void addlContainers(ArrayList<Container> containers) {

        for (int i = 0; i < containers.size(); i++)
            lContainers.add(containers.get(i));

    }

    public void finish() {

        log.close();

    }

}