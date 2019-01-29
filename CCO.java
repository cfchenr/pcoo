import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class CCO implements Runnable {

    private BinarySemaphore sMutex;

    private ArrayList<Infrastructure> infrastructures;
    private ArrayList<Transport> transports;

    public CCO() throws InterruptedException {

        // Semáforo binário para garantir que cada tarefa é atribuida unicamente a um
        // processo de cada vez.
        sMutex = new BinarySemaphore(1);

        // Lista de infraestruturas gerenciadas por este cco.
        infrastructures = new ArrayList<Infrastructure>();
        // Lista de transportes gerenciados por este cco.
        transports = new ArrayList<Transport>();

    }

    public void run() {

        try {

            // Percorre todos os transportes existentes no cco e caso exista algum
            // transportes
            // parado verifica se existem algum serviço para ele realizar.
            do
                for (int i = 0; i < transports.size(); i++)

                    if (transports.get(i).gState() == "stop")

                        sServices(transports.get(i));

            while (true);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    // Determina se o @transport tem permição para carregar na sua origem.
    public void slPermition(Transport transport) throws InterruptedException {

        sMutex.acquire();

        Infrastructure source = transport.gSource();

        if (source.glPermition()) {

            // Caso tenha permição, o estado de permição para esperar é "false" e o estado
            // de permição de carregamento é "true".
            transport.swPermition(false);
            transport.slPermition(true);

            // O transportes carrega na sua origem.
            source.srlSpot(transport);

        } else {

            // Caso não tenha permição, o estado de permição de carregamento é "false" e o
            // estado de permição para esperar é "true".
            transport.slPermition(false);
            transport.swPermition(true);

        }

        sMutex.release();

    }

    // Determina se o @transport tem permição para descarregar no seu destino
    // (como o transporte nesta fase já chegou ao destino, este passa a ser a sua
    // origem).
    public void suPermition(Transport transport) throws InterruptedException {

        sMutex.acquire();

        Infrastructure source = transport.gSource();

        if (source.guPermition()) {

            // Caso tenha permição, o estado de permição para esperar é "false" e o estado
            // de permição de descarregamento é "true".
            transport.swPermition(false);
            transport.suPermition(true);

            // O transportes descarrega no seu destino.
            source.sruSpot(transport);

        } else {

            // Caso não tenha permição, o estado de permição de descarregamento é "false" e
            // o estado de permição para esperar é "true".
            transport.suPermition(false);
            transport.swPermition(true);

        }

        sMutex.release();

    }

    // Define o serviço para o @transport.
    public void sServices(Transport transport) throws InterruptedException {

        sMutex.acquire();

        // Se existirem contentores para serem movidos a partir da origem do
        // transportes,
        // então define os serviços do transportes como "true".
        if (transport.gSource().ghmContainers(transport))

            transport.sServices(true);

        // Caso não existam contentores para serem movidos a partir da origem do
        // transportes, procura a infraestrutura mais próxima deste, que contenham
        // contentores
        // para mover.
        else {

            double minDistance;
            Infrastructure destination = transport.gSource();

            if (transport.gSource() != infrastructures.get(0))

                minDistance = transport.gSource().gPosition().gDistance(infrastructures.get(0).gPosition());

            else

                minDistance = transport.gSource().gPosition().gDistance(infrastructures.get(1).gPosition());

            // Percorre todas as infraestruturas para verifiar qual é a infraestrutura mais
            // próxima que
            // contém contentores para carregar.
            for (int i = 0; i < infrastructures.size(); i++) {

                if (transport.gSource() != infrastructures.get(i) && infrastructures.get(i).glContainers().size() > 0
                        && transport.gSource().gPosition()
                                .gDistance(infrastructures.get(i).gPosition()) < minDistance) {

                    minDistance = transport.gSource().gPosition().gDistance(infrastructures.get(i).gPosition());

                    destination = infrastructures.get(i);

                }

            }

            // Verifica se a infraestrutura encontrada é diferente da infraestrutura onde o
            // transportes se
            // encontra.
            if (destination != transport.gSource()) {

                // Verifica se a infraestrutura encontrada contém contentores para mover.
                if (destination.ghmContainers(transport)) {

                    // Define o destino do transportes para a infraestrutura encontrada.
                    transport.sDestination(destination);

                    // Define que o transportes tem serviços para realizar no seu destino.
                    transport.sServices(false);

                }

                // Caso contrário, define que o transportes não tem serviços para realizar.
            } else {

                transport.nsServices();

            }

        }

        sMutex.release();

    }

    // Retorna a lista de infraestruturas gerenciadas por este cco.
    public ArrayList<Infrastructure> gInfrastructures() {

        return infrastructures;

    }

    // Adiciona a @infrastructure para ser gerenciado por este cco.
    public void aInfrastructure(Infrastructure infrastructure) {

        infrastructures.add(infrastructure);

    }

    // Remove a @infrastructure, deixando este de ser gerenciado por este cco.
    public void rInfrastructure(Infrastructure infrastructure) {

        infrastructures.remove(infrastructure);

    }

    // Adiciona o @transport para ser gerenciado por este cco.
    public void aTransport(Transport transport) {

        transports.add(transport);

    }

    // Remove o @transport, deixando este de ser gerenciado por este cco.
    public void rTransport(Transport transport) {

        transports.remove(transport);

    }

    // Retorna a lista de transportes gerenciados por este cco.
    public ArrayList<Transport> gTransports() {

        return transports;

    }
    
}