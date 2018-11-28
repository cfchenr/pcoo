import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class CCO {

    private BinarySemaphore sMutex;
    private ArrayList<Terminal> terminals;
    private ArrayList<Train> trains;
    private WriteLogFile log;
    private int requestId;

    public CCO () throws IOException {

        sMutex = new BinarySemaphore(1);
        terminals = new ArrayList<Terminal>();
        trains = new ArrayList<Train>(); 
        log = new WriteLogFile("output/cco.txt");
        requestId = 0;

    }

    public ArrayList<Terminal> gTerminals () {

        return terminals;

    }

    public void slPermition (Train train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        System.out.println(requestThisId + ": Train " + train.gNumber() + " asked CCO to load at " + train.gSource().gName() + ".");

        Terminal source = train.gSource();

        System.out.print("CCO responded to the request " + requestThisId + ": ");

        if (source.glPermition()) {

            System.out.println("request granted.\nRailway " + source.glRailway().gNumber() + " in " + source.gName() + " was reserved for train " + train.gNumber() + ".");
            train.swPermition(false);
            train.slPermition(true);
            source.srlRailway(train);
        
        } else {
        
            System.out.println("request rejected.\nCCO gave permission to wait because the railway " + source.glRailway().gNumber() + " in " + source.gName() + " was reserved for the train " + source.glRailway().gReserve().gNumber() + ".");
            train.slPermition(false);
            train.swPermition(true);
        
        }

        sMutex.release();

    }

    public void suPermition (Train train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        System.out.println(requestThisId + ": Train " + train.gNumber() + " asked CCO to unload at " + train.gSource().gName() + ".");

        Terminal source = train.gSource();

        System.out.print("CCO responded to the request " + requestThisId + ": ");

        if (source.guPermition()) {

            System.out.println("request granted.\nRailway " + source.guRailway().gNumber() + " in " + source.gName() + " was reserved for train " + train.gNumber() + ".");
            train.swPermition(false);
            train.suPermition(true);
            source.sruRailway(train);
        
        } else {

            System.out.println("request rejected.\nCCO gave permission to wait because the railway " + source.guRailway().gNumber() + " in " + source.gName() + " was reserved for the train " + source.guRailway().gReserve().gNumber() + ".");
            train.suPermition(false);
            train.swPermition(true);
        
        }

        sMutex.release();

    }

    private void ssServices (Train train) throws InterruptedException {

        if (train.gSource().ghmContentores(train, train.gDestination()))
            train.ssServices(true);
        
        else
            train.ssServices(false);
        
    }

    private void sdServices (Train train) throws InterruptedException {

        if (train.gDestination().ghmContentores(train, train.gSource()))
            train.sdServices(true);
        
        else
            train.sdServices(false);


    }

    public void sServices (Train train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        System.out.println(requestThisId + ": Train " + train.gNumber() + " asked CCO if there are contentores for him to move: " + train.gSource().gName() + " to " + train.gDestination().gName() + " (ou ><).");

        System.out.print("CCO responded to the request " + requestThisId + ": ");

        ssServices(train);

        sdServices(train);

        train.sServices();

        if (train.gServices())
            System.out.println("Yes.");
        else
            System.out.println("No.");

        sMutex.release();

    }

    public void sClose (Train train) throws InterruptedException {

        sMutex.acquire();

        boolean close = true;

        for (int i = 0; i < trains.size(); i++) {

            if (trains.get(i).gState() == "load" || trains.get(i).gState() == "unload") {

                System.out.println("Comboio " + trains.get(i).gNumber() + ": " + trains.get(i).gState());
                close = false;
                break;

            }

        }

        if (close) {

            System.out.println("###########################################################################################################");
            System.out.println("###########################################################################################################");
            System.out.println("########################## Nenhum comboio a carregar, descarregar ou em transito ##########################");
            System.out.println("###########################################################################################################");
            System.out.println("###########################################################################################################");
        
        }

        boolean closeFinal = true;

        for (int i = 0; i < terminals.size(); i++) {
         
            if (close && terminals.get(i).glContentores().size() == 0)
                terminals.get(i).sClose(true);

            else {
                System.out.print("Contentores para carregar em " + terminals.get(i).gName() + ": " + terminals.get(i).glContentores().size() + " -> ");
                for (int j = 0; j < terminals.get(i).glContentores().size(); j++) {
                    if (terminals.get(i).glContentores().get(j).gTrain() == null)
                        System.out.print(" " + terminals.get(i).glContentores().get(j).gNumber() + " [" + terminals.get(i).glContentores().get(j).gDestination().gName() + "] {" + terminals.get(i).glContentores().get(j).gTrain() + "}");
                    else
                        System.out.print(" " + terminals.get(i).glContentores().get(j).gNumber() + " [" + terminals.get(i).glContentores().get(j).gDestination().gName() + "] {" + terminals.get(i).glContentores().get(j).gTrain().gNumber() + "}");

                }

                System.out.println();

                if (terminals.get(i).glRailway().gReserve() != null)
                    System.out.println("linha de carregamento reservada para: " + terminals.get(i).glRailway().gReserve().gNumber());
                else
                    System.out.println("linha de carregamento livre.");
                if (terminals.get(i).guRailway().gReserve() != null)
                    System.out.println("linha de descarregamento reservada para: " + terminals.get(i).guRailway().gReserve().gNumber());
                else
                    System.out.println("linha de descarregamento livre.");

                terminals.get(i).sClose(false);
                closeFinal = false;

            }

        }

        train.sClose(closeFinal);

        sMutex.release();

    }

    public void finish () {

        log.close();

    } 
    
    public void aTerminal (Terminal terminal) {

        terminals.add(terminal);

    }

    public void aTrain (Train train) {

        trains.add(train);

    }

}