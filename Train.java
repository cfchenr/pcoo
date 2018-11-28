import java.util.*;
import pt.ua.concurrent.*;

public class Train implements Runnable {

    private int number, mContentores, velocity;
    private Terminal source, destination;
    private Stack<Contentor> contentores;
    private CCO cco;
    private boolean close, services, sServices, dServices, permitionToLoad, permitionToUnload, permitionToWait;
    private Position position;
    private String state;

    public Train (int number, Terminal source, Terminal destination, CCO cco) throws InterruptedException {

        this.number = number;
        this.source = source;
        this.destination = destination;
        mContentores = (int)(Math.random()*((40 - 15) + 1) + 15);
        velocity = (int)(Math.random()*((180 - 110) + 1) + 110);
        this.contentores = new Stack<Contentor>();
        this.cco = cco;
        close = false;
        services = false;
        sServices = false;
        dServices = false;
        permitionToLoad = false;
        permitionToUnload = false; 
        permitionToWait = false;    
        position = source.gPosition();
        state = "stop";
        cco.aTrain(this);

    }

    public void run () {

        try {
/* 
            boolean close = true;

            do {

                System.out.print(number + " ha terminais abertos?");
                cco.sTerminalClose();

                close = true;
                for (int c = 0; c < cco.gTerminals().size(); c++)
                    if (!cco.gTerminals().get(c).gClose())
                        close = false;

                if (!close)
                    System.out.println(" Sim.");
                else {
                    state = "stop";
                    System.out.println(" Nao.");
                }

                System.out.print(number + " ha servicos?");
                cco.sServices(this);

                if (sServices) {

                    System.out.println(" Sim, na origem.");

                    do {

                        System.out.print(number + " tem permicao para carregar em " + source.gName() + "?");
                        cco.slPermition(this);

                        if (permitionToLoad) {
                            state = "load";
                            System.out.println(" Sim.");
                            break;
                        }
                        else {

                            if (source.glRailway().gTrain() != null)
                                System.out.println(" Nao. Linha reservada para o comboio " + source.glRailway().gReserve().gNumber());
                            else
                                System.out.println(" Nao.")
                                ;
                        }

                        System.out.print(number + " ainda ha servicos na origem?");
                        cco.sServices(this);

                        if (sServices)
                            System.out.println(" Sim.");
                        else
                            System.out.println(" Nao.");

                        Thread.sleep((long)(10000));

                    } while (sServices || permitionToWait);
                    
                    if (sServices) {

                        System.out.println(number + " a carregar...");
                        source.sLoad(this);
                        Thread.sleep((long)(contentores.size()*100));
                        System.out.println(number + " terminou de carregar");

                        state = "transit";
                        goToDestination();

                        do {
        
                            System.out.print(number + " tem permicao para descarregar em " + source.gName() + "?");
                            cco.suPermition(this);
        
                            if (permitionToUnload) {
                                state = "unload";
                                System.out.println(" Sim.");
                            } else 
                                System.out.println(" Nao. Linha reservada para o comboio " + source.guRailway().gReserve().gNumber());

                            Thread.sleep((long)(10000));
                            
                        } while (permitionToWait);

                        if (permitionToUnload) {
                         
                            System.out.println(number + " a descarregar...");
                            Thread.sleep((long)(contentores.size()*100));
                            source.sUnload(this);
                            System.out.println(number + " terminou de descarregar");

                            state = "stop";

                        }

                    }

                } else if (dServices) {

                    System.out.println(" Sim, no destino.");
                    state = "transit";
                    goToDestination();

                } else {

                    state = "stop";
                    System.out.println(" Nao.");
                    Thread.sleep((long)(10000));

                }

                Thread.sleep((long)(20000));
            
            } while (!(!services && close));     
            */ 
            

            do {

                if (contentores.size() > 0) {

                    state = "unload";

                    do {

                        Thread.sleep(5000);                

                        cco.suPermition(this);

                    } while (!permitionToUnload);

                    source.sUnload(this);

                    cDirection();

                } else {

                    cco.sServices(this);

                    if (services) {

                        state = "load";

                        if (dServices) {

                            goToDestination();

                        }

                        do {

                            Thread.sleep(5000);                

                            cco.slPermition(this);

                        } while (!permitionToLoad);

                        source.sLoad(this);

                        cDirection();

                    } else {

                        state = "stop";

                    }

                }

                Thread.sleep(20000);                

                cco.sClose(this);

            } while (!close);


        } catch (InterruptedException e) {

            e.printStackTrace();

        }
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");        
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===================================== " + number + " =====================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        System.out.println("===============================================================================");
        //System.exit(0);

    }

    public void cDirection () throws InterruptedException {

        System.out.println(number + " a mudar de direcao...");
        Thread.sleep((long)(100));
        Terminal temp = source;
        source = destination;
        destination = temp;
        System.out.println(number + " terminou de mudar de direcao.");


    }

    public void goToDestination () throws InterruptedException {

        cDirection();
        System.out.println(number + " em marcha de " + source.gName() + " para a estacao destino " + destination.gName() + "...");
        Thread.sleep((long)(5000));
        position = destination.gPosition();
        System.out.println(number + " chegou ao destino " + source.gName());

    }

    public int gNumber () {

        return number;

    }

    public Terminal gSource () {

        return source;
        
    }

    public Terminal gDestination () {

        return destination;

    }

    public int gmContentores () {

        return mContentores;

    }

    public Stack<Contentor> gContentores () {

        return contentores;

    }

    public boolean gServices () {

        return (sServices || dServices);

    }

    public boolean gsServices () {

        return sServices;

    }

    public boolean gdServices () {

        return dServices;

    }

    public boolean glPermition () {

        return permitionToLoad;

    }

    public boolean guPermition () {

        return permitionToUnload;

    }

    public boolean gwPermition () {

        return permitionToWait;

    }

    public Position gPosition () {

        return position;

    }

    public String gState () {

        return state;
        
    }

    public void sServices () {

        if (sServices || dServices)
            services = true;
        else
            services = false;

    }

    public void ssServices (boolean tf) {

        sServices = tf;

    }

    public void sdServices (boolean tf) {

        dServices = tf;

    }

    public void slPermition (boolean tf) {

        permitionToLoad = tf;

    }

    public void suPermition (boolean tf) {

        permitionToUnload = tf;

    }

    public void swPermition (boolean tf) {

        permitionToWait = tf;

    }

    public void sPosition (Position position) {

        this.position = position;

    }

    public void aContentor (Contentor contentor) {

        contentores.push(contentor);

    }

    public boolean isFull () {

        return contentores.size() == mContentores;

    }

    public boolean isEmpty () {

        return contentores.size() == 0;

    }

    public void sClose (boolean tf) {

        close = tf;

    }

}