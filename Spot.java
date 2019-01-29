import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class Spot {

    private BinarySemaphore semaphoreTransport;
    private int number;
    private boolean busy;
    private Transport transport;
    private Transport reserve;

    public Spot(int number) {

        semaphoreTransport = new BinarySemaphore(1);
        sNumber(number);
        sBusy(false);
        sTransport(null);
        sReserve(null);

    }

    public void sNumber(int number) {

        this.number = number;

    }

    public int gNumber() {

        return number;

    }

    public void sBusy(boolean busy) {

        this.busy = busy;

    }

    public boolean gBusy() {

        return busy;

    }

    public void sTransport(Transport transport) {

        this.transport = transport;

    }

    public Transport gTransport() {

        return transport;

    }

    public void sReserve(Transport transport) {

        reserve = transport;

    }

    public Transport gReserve() {

        return reserve;

    }

    public void aTransport(Transport transport) {

        semaphoreTransport.acquire();
        busy = true;
        this.transport = transport;

    }

    public void rTransport() {

        reserve = null;
        transport = null;
        busy = false;
        semaphoreTransport.release();

    }

}