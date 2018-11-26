import java.util.*;
import pt.ua.concurrent.*;

public class Contentor {

    private int number;
    private Terminal destination;

    public Contentor (int number, Terminal destination) {

        this.number = number;
        this.destination = destination;

        System.out.println("Create new contentor: " + number + " with destination " + destination.gName());

    }

    public int gNumber () {

        return number;

    }

    public Terminal gDestination () {

        return destination;

    }

}