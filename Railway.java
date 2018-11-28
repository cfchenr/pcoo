import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class Railway {

    private BinarySemaphore semaphoreTrain;
    private int number;
    private boolean busy;
    private Train train;
    private Train reserve;

    public Railway (int number) {

        semaphoreTrain = new BinarySemaphore(1);
        this.number = number;
        busy = false;
        train = null;
        reserve = null;

    }

    public int gNumber () {

        return number;

    }

    public boolean isBusy () {

        return busy;

    }

    public Train gTrain () {

        return train;

    }

    public void sReserve (Train train) {

        reserve = train;

    }

    public Train gReserve () {

        return reserve;

    }

    public void aTrain (Train train) {

        semaphoreTrain.acquire();
        busy = true;
        this.train = train;

    }

    public void dTrain () {

        reserve = null;
        train = null;
        busy = false;
        semaphoreTrain.release();

    }

}