import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class Spot {

    private BinarySemaphore semaphoreTransport;
    private int number;
    private boolean busy;
    private Transport train;
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

    public Transport sTransport(Transport transport) {

        return train;

    }

    public Transport gTransport() {

        return train;

    }

    public void sReserve(Transport train) {

        reserve = train;

    }

    public Transport gReserve() {

        return reserve;

    }

    public void aTransport(Transport train) {

        semaphoreTransport.acquire();
        busy = true;
        this.train = train;

    }

    public void dTransport() {

        reserve = null;
        train = null;
        busy = false;
        semaphoreTransport.release();

    }

}