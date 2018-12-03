import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class CCO implements Runnable {

    private BinarySemaphore sMutex;

    private ArrayList<Terminal> terminals;
    private ArrayList<Train> trains;

    private int requestId, initialSize;

    public CCO () throws InterruptedException {

        sMutex = new BinarySemaphore(1);

        terminals = new ArrayList<Terminal>();
        trains = new ArrayList<Train>(); 

        requestId = 0;
        initialSize = 0;

    }

    public void run () {

        try {

            do {

                for (int i = 0; i < trains.size(); i++)

                    if (trains.get(i).gState() == "stop")

                        sServices(trains.get(i));
                    

                System.out.println("============================================ Terminals ============================================");

                for (int k = 0; k < terminals.size(); k++) {

                    System.out.print(terminals.get(k).gName() + " contentores to load: ");
                    
                    for (int f = 0; f < terminals.get(k).glContentores().size(); f++) {

                        if (terminals.get(k).glContentores().get(f).gTrain() != null)

                            System.out.print(" " + terminals.get(k).glContentores().get(f).gNumber() + " [" + terminals.get(k).glContentores().get(f).gDestination().gName() + ", " + terminals.get(k).glContentores().get(f).gTrain().gNumber() + "]");
    
                        else 
    
                            System.out.print(" " + terminals.get(k).glContentores().get(f).gNumber() + " [" + terminals.get(k).glContentores().get(f).gDestination().gName() + ", " + terminals.get(k).glContentores().get(f).gTrain() + "]");

                    }

                    System.out.println();
                    System.out.print(terminals.get(k).gName() + " contentores unloaded: ");

                    for (int f = 0; f < terminals.get(k).guContentores().size(); f++) 
                        System.out.print(" " + terminals.get(k).guContentores().get(f).gNumber() + " ");
                    
                    System.out.println();

                }

                System.out.println("============================================   Trains  ============================================");

                for (int k = 0; k < trains.size(); k++) {

                    System.out.print(trains.get(k).gNumber() + " " + trains.get(k).gState());
                    
                    if (trains.get(k).gState() != "stop") {

                        System.out.print(": " + trains.get(k).gSource().gName() + " to " + trains.get(k).gDestination().gName());

                        if (trains.get(k).gContentores().size() > 0) {

                            System.out.print(": ");

                            for (int f = 0; f < trains.get(k).gContentores().size(); f++)
                                System.out.print(" " + trains.get(k).gContentores().get(f).gNumber() + " [" + trains.get(k).gContentores().get(f).gDestination().gName() + "] ");

                            System.out.print(" {" + trains.get(k).gContentores().size() + "/" + trains.get(k).gmContentores() + "} ");
                            
                        }

                    }

                    System.out.println();

                }

                Thread.sleep(1250);

            } while (true);
            
        } catch (InterruptedException e) {

            e.printStackTrace();

        }


    }

    public void slPermition (Train train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        //System.out.println(requestThisId + ": Train " + train.gNumber() + " asked CCO to load at " + train.gSource().gName() + ".");

        Terminal source = train.gSource();

        //System.out.print("CCO responded to the request " + requestThisId + ": ");

        if (source.glPermition()) {

            //System.out.println("request granted.\nRailway " + source.glRailway().gNumber() + " in " + source.gName() + " was reserved for train " + train.gNumber() + ".");
            train.swPermition(false);
            train.slPermition(true);
            source.srlRailway(train);
        
        } else {
        
            //System.out.println("request rejected.\nCCO gave permission to wait because the railway " + source.glRailway().gNumber() + " in " + source.gName() + " was reserved for the train " + source.glRailway().gReserve().gNumber() + ".");
            train.slPermition(false);
            train.swPermition(true);
        
        }

        sMutex.release();

    }

    public void suPermition (Train train) throws InterruptedException {

        sMutex.acquire();

        int requestThisId = requestId++;

        //System.out.println(requestThisId + ": Train " + train.gNumber() + " asked CCO to unload at " + train.gSource().gName() + ".");

        Terminal source = train.gSource();

        //System.out.print("CCO responded to the request " + requestThisId + ": ");

        if (source.guPermition()) {

            //System.out.println("request granted.\nRailway " + source.guRailway().gNumber() + " in " + source.gName() + " was reserved for train " + train.gNumber() + ".");
            train.swPermition(false);
            train.suPermition(true);
            source.sruRailway(train);
        
        } else {

            //System.out.println("request rejected.\nCCO gave permission to wait because the railway " + source.guRailway().gNumber() + " in " + source.gName() + " was reserved for the train " + source.guRailway().gReserve().gNumber() + ".");
            train.suPermition(false);
            train.swPermition(true);
        
        }

        sMutex.release();

    }

    public void sServices (Train train) throws InterruptedException {

        sMutex.acquire();

        //int requestThisId;

        //System.out.println("CCO check if there are contentores for " + train.gNumber() + " to move: " + train.gSource().gName() + " to " + train.gDestination().gName() + " (or ><).");

        //System.out.print("CCO  concluded that: ");
        
        // Se tiver contentores onde está para transportar, reserva-os e define o serviço do comboio
        if (train.gSource().ghmContentores(train)) {
            
            train.sServices(true);

        // Se não tvier serviços onde está, vê se há serviços em algum lado (o mais próximo possível)
        } else {

            double minDistance; 

            Terminal destination = train.gSource();
            
            //Obter um novo terminal com contentores para carregar (o mais próximo disponível da posição atual do comboio)

            if (train.gSource() != terminals.get(0))

                minDistance = train.gSource().gPosition().gDistance(terminals.get(0).gPosition());

            else 

                minDistance = train.gSource().gPosition().gDistance(terminals.get(1).gPosition());


            for (int i = 0; i < terminals.size(); i++) {

                if (train.gSource() != terminals.get(i) && terminals.get(i).glContentores().size() > 0 && train.gSource().gPosition().gDistance(terminals.get(i).gPosition()) < minDistance)

                    minDistance = train.gSource().gPosition().gDistance(terminals.get(i).gPosition());

                    destination = terminals.get(i);

                }


            if (destination != train.gSource()) {

                if (destination.ghmContentores(train)) {

                    System.out.println("Destino mais próximo do comboio " + train.gNumber() + " que contém contentores para transportar é " + destination.gName());

                    train.sDestination(destination);

                    train.sServices(false);

                }
            
            } else {

                System.out.println("Não há serviços para o comboio " + train.gNumber() + " é " + destination.gName());

                train.nsServices();

            }

        }

        /* if (train.gSource().ghmContentores(train, train.gDestination()) && train.gDestination().ghmContentores(train, train.gSource())) {

            //System.out.println("Yes.");
            train.sServices(true, true);

        }
        
        else if (train.gSource().ghmContentores(train, train.gDestination())) {

            //System.out.println("Yes.");
            train.sServices(true, false);

        }

        else if (train.gDestination().ghmContentores(train, train.gSource())) {
        
            //System.out.println("Yes.");
            train.sServices(false, true);
        
        }

        else {
        
            //System.out.println("No.");
            train.sServices(false, false);
        
        } */

        sMutex.release();

    }

    /* public void sClose (Train train) throws InterruptedException {

        sMutex.acquire();

        boolean close = true;

        for (int i = 0; i < trains.size(); i++) {

            if (trains.get(i).gState() == "load" || trains.get(i).gState() == "unload") {

                close = false;
                break;

            }

        }

        boolean closeFinal = true;

        for (int i = 0; i < terminals.size(); i++) {
         
            if (close && terminals.get(i).glContentores().size() == 0)
                terminals.get(i).sClose(true);

            else {

                terminals.get(i).sClose(false);
                closeFinal = false;

            }

        }

        train.sClose(closeFinal);

        sMutex.release();

    } */
    
    public ArrayList<Terminal> gTerminals () {

        return terminals;

    }

    public void aTerminal (Terminal terminal) {

        terminals.add(terminal);

    }

    public void rTerminal (Terminal terminal) {

        terminals.remove(terminal);

    }

    public ArrayList<Train> gTrains () {

        return trains;

    }

    public void aTrain (Train train) {

        trains.add(train);
        initialSize = trains.size();

    }

    public void rTrain (Train train) {

        trains.remove(train);

    }

}