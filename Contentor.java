import java.util.*;
import pt.ua.concurrent.*;

public class Contentor {

    private int number;
    private Terminal destination;
    private Train train;

    public Contentor (int number, Terminal destination) {

        this.number = number;
        this.destination = destination;
        train = null;

    }

    public int gNumber () {

        return number;

    }

    public Terminal gDestination () {

        return destination;

    }

    public Train gTrain () {

        return train;

    }

    public void sTrain (Train train) {

        this.train = train;

    }

}