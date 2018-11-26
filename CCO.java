import java.util.*;
import pt.ua.concurrent.*;

public class CCO {

    private BinarySemaphore sMutex;
    private ArrayList<Terminal> terminals;

    public CCO (BinarySemaphore sMutex, ArrayList<Terminal> terminals) {

        this.sMutex = sMutex;
        this.terminals = terminals; 

    }

    public ArrayList<Terminal> gTerminals () {

        return terminals;

    }

    public void slPermition (Train train) throws InterruptedException {

        sMutex.acquire();

        Terminal source = train.gSource();

        System.out.println("Train " + train.gNumber() + " asked CCO to load at " + source.gName() + ".");
        System.out.print("CCO responded to the request: ");

        if (source.glPermition() && source.ghmContentores(train.gDestination())) {

            System.out.println("request granted.\nRailway " + source.glRailway().gNumber() + " in " + source.gName() + " was reserved for train " + train.gNumber() + ".");
            train.swPermition(false);
            train.slPermition(true);
            train.gSource().slRailway(false, train);
        
        } else if (source.ghmContentores(train.gDestination())) {
        
            System.out.println("request rejected.\nCCO gave permission to wait because the railway " + source.glRailway().gNumber() + " in " + source.gName() + " was reserved for the train " + source.glRailway().gTrain().gNumber() + ".");
            train.slPermition(false);
            train.swPermition(true);
        
        } else {
        
            System.out.println("request rejected.\nCCO gave permission to wait because there are no contentores to load.");
            train.slPermition(false);
            train.swPermition(true);
        
        } 

        sMutex.release();

    }

    public void suPermition (Train train) throws InterruptedException {

        sMutex.acquire();

        Terminal destination = train.gDestination();

        System.out.println("Train " + train.gNumber() + " asked CCO to unload at " + destination.gName() + ".");
        System.out.print("CCO responded to the request: ");

        if (destination.guPermition()) {

            System.out.println("request granted.\nRailway " + destination.guRailway().gNumber() + " in " + destination.gName() + " was reserved for train " + train.gNumber() + ".");
            train.swPermition(false);
            train.suPermition(true);
            train.gDestination().suRailway(false, train);
        
        } else {
            System.out.println("request rejected.\nCCO gave permission to wait because the railway " + destination.guRailway().gNumber() + " in " + destination.gName() + " was reserved for the train " + destination.guRailway().gTrain().gNumber() + ".");
            train.suPermition(false);
            train.swPermition(true);
        
        }

        sMutex.release();

    }

    public void sServices (Train train) throws InterruptedException {

        sMutex.acquire();

        System.out.println("Train " + train.gNumber() + " asked CCO if there are contentores for him to move.");
        System.out.print("CCO responded to the request: ");

        if (train.gDestination().ghmContentores(train.gSource()) || train.gSource().ghmContentores(train.gDestination())) {
            System.out.println("Yes.");
            train.sServices(true);
        } else {
            System.out.println("No.");
            train.sServices(false);
        }

        sMutex.release();

    }

    public void sTerminalClose (Terminal terminal) throws InterruptedException {

        sMutex.acquire();

        if (terminal.glContentores().size() > 0)
            terminal.sClose(false);
        else
            terminal.sClose(true);

        sMutex.release();

    }

    public void sTrainClose (Train train) throws InterruptedException {

        sMutex.acquire();

        for (int i = 0; i < terminals.size(); i++)
            if (terminals.get(i).gClose()) {
                train.sClose(true);
                return;
            }

        train.sClose(false);

        sMutex.release();

    }

}