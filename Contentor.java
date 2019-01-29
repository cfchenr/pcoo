import java.util.*;
import pt.ua.concurrent.*;

public class Contentor {

    private int number;
    private Terminal destination;
    private Transport transport;

    public Contentor (int number, Terminal destination) {

        sNumber(number);
        sDestination(destination);
        sTransport(null);

    }

    public void sNumber (int number) {

        this.number = number;

    }

    public int gNumber () {

        return number;

    }

    public void sDestination (Terminal destination) {

        this.destination = destination;

    }

    public Terminal gDestination () {

        return destination;

    }

    public void sTransport (Transport transport) {

        this.transport = transport;

    }

    public Transport gTransport () {

        return transport;

    }

}