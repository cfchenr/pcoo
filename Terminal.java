import java.util.*;
import pt.ua.concurrent.*;

public class Terminal {

    private String name;
    private Railway lRailway, uRailway;
    private ArrayList<Contentor> lContentors;
    private ArrayList<Contentor> uContentors;
    private BinarySemaphore sMutex;
    private boolean close;

    public Terminal (String name, Railway lRailway, Railway uRailway) {

        this.name = name;
        this.lRailway = lRailway;
        this.uRailway = uRailway;
        lContentors = new ArrayList<Contentor>();
        uContentors = new ArrayList<Contentor>();
        sMutex = new BinarySemaphore(1);
        close = false;
        
    }

    public String gName () {

        return name;

    }

    public Railway glRailway () {

        return lRailway;

    }

    public Railway guRailway () {

        return uRailway;

    }

    public ArrayList<Contentor> glContentores () {

        return lContentors;

    }

    public ArrayList<Contentor> guContentores () {

        return uContentors;

    }

    public boolean gClose () throws InterruptedException {

        return close;

    }

    public boolean ghmContentores (Terminal terminal) {

        for (int i = 0; i < lContentors.size(); i++)
            if (lContentors.get(i).gDestination() == terminal) {
                return true;
            }

        return false;

    }

    public boolean glPermition () {

        return lRailway.gState();

    }

    public boolean guPermition () {

        return uRailway.gState();

    }

    public void slRailway (boolean tf, Train train) {

        lRailway.sState(tf);
        lRailway.sTrain(train);

    }

    public void suRailway (boolean tf, Train train) {

        uRailway.sState(tf);
        uRailway.sTrain(train);

    }

    public void sLoad (Train train) throws InterruptedException {

        sMutex.acquire();

        System.out.println(train.gNumber() + " go load at " + name + ".");

        lRailway.aTrain();

        int i = 0;

        while (!train.isFull()) {
            if (i == lContentors.size())
                break;

            if (lContentors.get(i).gDestination() == train.gDestination()) {

                train.aContentor(lContentors.get(i));
                lContentors.remove(i);

                i--;

            }

            i++;

        }

        lRailway.dTrain();

        System.out.println(train.gNumber() + " finish load at " + name + ".");

        sMutex.release();

    }

    public void sUnload (Train train) throws InterruptedException {

        sMutex.acquire();

        System.out.println(train.gNumber() + " go unload at " + name + ".");

        uRailway.aTrain();

        while (!train.isEmpty())
            uContentors.add(train.gContentores().pop());

        uRailway.dTrain();

        System.out.println(train.gNumber() + " finish unload at " + name + ".");

        sMutex.release();

    }

    public void sClose (boolean tf) throws InterruptedException {

        close = tf;

    }

    public void addlContentores (ArrayList<Contentor> contentores) {

        for (int i = 0; i < contentores.size(); i++)
            lContentors.add(contentores.get(i));

    }

}