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

    public Terminal (String name, int x, CCO cco) throws IOException {

        this.name = name;
        lRailway = new Railway (1);
        uRailway = new Railway (2);
        log = new WriteLogFile("output/terminal" + name + ".txt");
        lContentors = new ArrayList<Contentor>();
        uContentors = new ArrayList<Contentor>();
        this.cco = cco;
        slMutex = new BinarySemaphore(1);
        suMutex = new BinarySemaphore(1);
        close = false;
        position = new Position(x);
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

    public boolean ghmContentores (Train train, Terminal terminal) {

        boolean check = false;

        for (int i = 0; i < lContentors.size(); i++) {

            if (lContentors.get(i).gTrain() == null) {

                if (lContentors.get(i).gDestination() == terminal) {

                    check = true;
                    lContentors.get(i).sTrain(train);

                    System.out.println("Contentores com o mesmo destino do comboio.");

                } else if (terminal.gPosition().gDistance(lContentors.get(i).gDestination().gPosition()) < position.gDistance(lContentors.get(i).gDestination().gPosition())) {

                    check = true;
                    lContentors.get(i).sTrain(train);

                    System.out.println("O terminal passado no argumento fica mais próximo do destino do contentor do que o terminal onde o contentor está.");

                } else {

                    System.out.println("Fica mais longe.");

                }

            } else if (lContentors.get(i).gTrain() == train) {

                check = true;

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

    public void srlContentores (Train train) throws InterruptedException {

        slMutex.acquire();

        System.out.print("Terminal " + name + " as reserved contentor ");

        int i = 0;

        for (int j = 0; j < lContentors.size(); j++) {        

            if (lContentors.get(j).gTrain() == null) {
                if ((train.gDestination().gPosition().gDistance(lContentors.get(j).gDestination().gPosition()) < position.gDistance(lContentors.get(j).gDestination().gPosition())) && i < train.gmContentores()) {

                    lContentors.get(j).sTrain(train);
                    System.out.print(" " + lContentors.get(j).gNumber() + " ");
                    i++;
                
                }

            }

        }

        System.out.println(" for train " + train.gNumber());

        slMutex.release();

    }

    private void suContentores (Train train) throws InterruptedException {

        for (int i = 0; i < train.gContentores().size(); i++)
            train.gContentores().get(i).sTrain(null);

    }

    public void sLoad (Train train) throws InterruptedException {

        slMutex.acquire();

        System.out.print(train.gNumber() + " go load at " + name + ":");

        lRailway.aTrain(train);

        for (int i = 0; i < lContentors.size(); i++)

            if (lContentors.get(i).gTrain() == train) {

                train.aContentor(lContentors.get(i));
                System.out.println(" [" + lContentors.get(i).gNumber() + "] ");
                lContentors.remove(i);

            }

        lRailway.dTrain();

        System.out.println(train.gNumber() + " finish load at " + name + ".");

        slMutex.release();

    }

    public void sUnload (Train train) throws InterruptedException {

        suMutex.acquire();

        System.out.print(train.gNumber() + " go unload at " + name + ":");

        suContentores(train);

        uRailway.aTrain(train);

        while (!train.isEmpty()) {
            if (train.gContentores().peek().gDestination() == this) {
                System.out.println(" [" + train.gContentores().peek().gNumber() + "] ");
                uContentors.add(train.gContentores().pop());
            } else {
                System.out.println(" {" + train.gContentores().peek().gNumber() + "} ");
                lContentors.add(train.gContentores().pop());
            }
        }

        uRailway.dTrain();

        System.out.println(train.gNumber() + " finish unload at " + name + ".");

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