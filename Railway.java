import java.util.*;
import pt.ua.concurrent.*;

public class Railway {

    private BinarySemaphore semaphoreTrain;
    private int number;
    private boolean state;
    private Train train;

    public Railway (int number) {

        semaphoreTrain = new BinarySemaphore(1);
        this.number = number;
        state = true;
        train = null;

    }

    public int gNumber () {

        return number;

    }

    public boolean gState () {

        return state;

    }

    public Train gTrain () {

        return train;

    }

    public void sState (boolean tf) {

        state = tf;

    }

    public void sTrain (Train train) {

        this.train = train;

    }

    public void aTrain () {

        semaphoreTrain.acquire();
        state = false;

    }

    public void dTrain () {

        train = null;
        state = true;
        semaphoreTrain.release();

    }

}