import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class Terminal {

    private String name;
    private Railway lRailway, uRailway;
    private WriteLogFile log;
    private ArrayList<Contentor> lContentors;
    private ArrayList<Contentor> uContentors;
    private CCO cco;
    private BinarySemaphore slMutex, suMutex;
    private boolean close;
    private Position position;

    public Terminal (String name, int x, CCO cco) {

        this.name = name;
        
        lRailway = new Railway (1);
        uRailway = new Railway (2);

        lContentors = new ArrayList<Contentor>();
        uContentors = new ArrayList<Contentor>();

        slMutex = new BinarySemaphore(1);
        suMutex = new BinarySemaphore(1);
        position = new Position(x);

        close = false;

        this.cco = cco;
        cco.aTerminal(this);

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

    public Position gPosition () {

        return position;

    }

    public boolean gClose () throws InterruptedException {

        return close;

    }

    public boolean ghmContentores (Train train) {

        boolean check = false;

        for (int i = 0; i < lContentors.size(); i++)

            if (lContentors.get(i).gTrain() == null || lContentors.get(i).gTrain() == train) {

                check = true;
                train.sDestination(lContentors.get(i).gDestination());
                break;

            }

        if (check) {

            Stack<Terminal> select = new Stack<Terminal>();
            select.add(train.gSource());
            select.add(train.gDestination());

            int size = 0;
            boolean close = false;

            while (lContentors.size() > 0 && close == false) {

                for (int i = 0; i < lContentors.size(); i++) {

                    if (lContentors.get(i).gTrain() == null && lContentors.get(i).gDestination() == select.peek() && size < train.gmContentores()) {

                        lContentors.get(i).sTrain(train);
                        size++;

                    }

                    if (size == train.gmContentores())
                        break;

                }

                if (size == train.gmContentores())
                    break;
   
                Terminal selectT = this;
                double minDistance = position.gDistance(train.gDestination().gPosition());
                close = true;

                for (int i = 0; i < cco.gTerminals().size(); i++)

                    if (!select.contains(cco.gTerminals().get(i)) && position.gDistance(cco.gTerminals().get(i).gPosition()) > train.gDestination().gPosition().gDistance(cco.gTerminals().get(i).gPosition())) {
                        
                        selectT = cco.gTerminals().get(i);
                        close = false;

                    }


                select.add(selectT);

            }

        }

        return check;

    }

    public boolean glPermition () {

        return (lRailway.gReserve() == null);

    }

    public boolean guPermition () {

        return (uRailway.gReserve() == null);

    }

    public void srlRailway (Train train) {

        lRailway.sReserve(train);

    }

    public void sruRailway (Train train) {

        uRailway.sReserve(train);

    }

    private void suContentores (Train train) throws InterruptedException {

        for (int i = 0; i < train.gContentores().size(); i++)
            train.gContentores().get(i).sTrain(null);

    }

    public void sLoad (Train train) throws InterruptedException {

        slMutex.acquire();
        lRailway.aTrain(train);

        Stack<Terminal> select = new Stack<Terminal>();
        select.add(train.gSource());
        select.add(train.gDestination());

        int size = 0;
        boolean close = false;

        while (size < train.gmContentores() && close == false) {

            for (int i = 0; i < lContentors.size(); i++) {

                if ((lContentors.get(i).gTrain() == null || lContentors.get(i).gTrain() == train) && lContentors.get(i).gDestination() == select.peek() && size < train.gmContentores()) {

                    train.aContentor(lContentors.get(i));
                    lContentors.remove(i);
                    i--;
                    Thread.sleep((long)(1250));
                    size++;

                }

                if (size == train.gmContentores())
                    break;

            }

            if (size == train.gmContentores())
                break;
               
            Terminal selectT = this;
            double minDistance = position.gDistance(train.gDestination().gPosition());
            close = true;

            for (int i = 0; i < cco.gTerminals().size(); i++)

                if (!select.contains(cco.gTerminals().get(i)) && position.gDistance(cco.gTerminals().get(i).gPosition()) > train.gDestination().gPosition().gDistance(cco.gTerminals().get(i).gPosition())) {
                
                    selectT = cco.gTerminals().get(i);
                    close = false;

                }

            select.add(selectT);
        
        }

        lRailway.dTrain();
        slMutex.release();

    }

    public void sUnload (Train train) throws InterruptedException {

        suMutex.acquire();
        suContentores(train);
        uRailway.aTrain(train);

        while (!train.isEmpty()) {

            if (train.gContentores().peek().gDestination() == this)
                uContentors.add(train.gContentores().pop());

            else
                lContentors.add(train.gContentores().pop());

            Thread.sleep((long)(1250));

        }

        uRailway.dTrain();
        suMutex.release();

    }

    public void sClose (boolean tf) throws InterruptedException {

        close = tf;

    }

    public void addlContentores (ArrayList<Contentor> contentores) {

        for (int i = 0; i < contentores.size(); i++)
            lContentors.add(contentores.get(i));

    }

    public void finish () {

        log.close();

    }

}