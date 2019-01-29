import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class CCO implements Runnable {

    private BinarySemaphore sMutex;

    private ArrayList<Terminal> terminals;
    private ArrayList<Transport> trains;

    private int requestId, initialSize;

    public CCO() throws InterruptedException {

        sMutex = new BinarySemaphore(1);

        terminals = new ArrayList<Terminal>();
        trains = new ArrayList<Transport>();

        requestId = 0;
        initialSize = 0;

    }

    public void run() {

        try {

            do

                for (int i = 0; i < trains.size(); i++)

                    if (trains.get(i).gState() == "stop")

                        sServices(trains.get(i));

            while (true);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    public void slPermition(Transport train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        Terminal source = train.gSource();

        if (source.glPermition()) {

            train.swPermition(false);
            train.slPermition(true);
            source.srlRailway(train);

        } else {

            train.slPermition(false);
            train.swPermition(true);

        }

        sMutex.release();

    }

    public void suPermition(Transport train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        Terminal source = train.gSource();

        if (source.guPermition()) {

            train.swPermition(false);
            train.suPermition(true);
            source.sruRailway(train);

        } else {

            train.suPermition(false);
            train.swPermition(true);

        }

        sMutex.release();

    }

    public void sServices(Transport train) throws InterruptedException {

        sMutex.acquire();

        if (train.gSource().ghmContentores(train))

            train.sServices(true);

        else {

            double minDistance;
            Terminal destination = train.gSource();

            if (train.gSource() != terminals.get(0))

                minDistance = train.gSource().gPosition().gDistance(terminals.get(0).gPosition());

            else

                minDistance = train.gSource().gPosition().gDistance(terminals.get(1).gPosition());

            for (int i = 0; i < terminals.size(); i++) {

                if (train.gSource() != terminals.get(i) && terminals.get(i).glContentores().size() > 0
                        && train.gSource().gPosition().gDistance(terminals.get(i).gPosition()) < minDistance)

                    minDistance = train.gSource().gPosition().gDistance(terminals.get(i).gPosition());

                destination = terminals.get(i);

            }

            if (destination != train.gSource()) {

                if (destination.ghmContentores(train)) {

                    train.sDestination(destination);

                    train.sServices(false);

                }

            } else {

                train.nsServices();

            }

        }

        sMutex.release();

    }

    public ArrayList<Terminal> gTerminals() {

        return terminals;

    }

    public void aTerminal(Terminal terminal) {

        terminals.add(terminal);

    }

    public void rTerminal(Terminal terminal) {

        terminals.remove(terminal);

    }

    public ArrayList<Transport> gTransports() {

        return trains;

    }

    public void aTransport(Transport train) {

        trains.add(train);
        initialSize = trains.size();

    }

    public void rTransport(Transport train) {

        trains.remove(train);

    }

}