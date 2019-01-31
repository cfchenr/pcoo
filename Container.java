import java.util.*;
import pt.ua.concurrent.*;

public class Container {

    private int number;
    private Infrastructure destination;
    private Transport transport;

    public Container(int number, Infrastructure destination) {

        sNumber(number);
        sDestination(destination);
        sTransport(null);

    }

    // Define o número deste contentore.
    private void sNumber(int number) {

        assert number >= 0;

        this.number = number;

        assert gNumber() == number;

    }

    // Retorna o número deste contentore.
    public int gNumber() {

        return number;

    }

    // Define o destino deste contentore.
    private void sDestination(Infrastructure destination) {

        assert destination != null;

        this.destination = destination;

        assert gDestination() == destination;

    }

    // Retorna o destino deste contentore.
    public Infrastructure gDestination() {

        return destination;

    }

    // Define o @transport para este contentore.
    public void sTransport(Transport transport) {

        this.transport = transport;

        assert gTransport() == transport;

    }

    // Retorna o transporte associado a este contentore.
    public Transport gTransport() {

        return transport;

    }

}