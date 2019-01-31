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

    // Definir o número do local.
    public void sNumber(int number) {

        assert number >= 0;

        this.number = number;

        assert gNumber() == number;

    }

    // Obter o número do local.
    public int gNumber() {

        return number;

    }

    // Definir o local como ocupado.
    public void sBusy(boolean busy) {

        assert (busy || !busy);

        this.busy = busy;

        assert ((gBusy()&& busy) || (!gBusy() && !busy));

    }

    // Obter o estado de ocupação do local.
    public boolean gBusy() {

        return busy;

    }

    // Definir o transporte que ocupa o local.
    public void sTransport(Transport transport) {

        this.transport = transport;

        assert gTransport() == transport;

    }

    // Obter o transporte neste local.
    public Transport gTransport() {

        return transport;

    }

    // Definir o local como reservado por um transporte.
    public void sReserve(Transport transport) {

        reserve = transport;

        assert gReserve() == transport;

    }

    // Obter o transporte para o qual o local está reservado.
    public Transport gReserve() {

        return reserve;

    }

    // Adiciona o transporte a este local, atualizando o estado de ocupação do local.
    public void aTransport(Transport transport) {

        assert transport != null;

        semaphoreTransport.acquire();
        sBusy(true);
        sTransport(transport);

        assert gTransport() == transport;

    }

    // Remove o transporte deste local, bem como a reserva e o estado de ocupação.
    public void rTransport() {

        assert gTransport() != null;

        sReserve(null);
        sTransport(null);
        sBusy(false);
        semaphoreTransport.release();

        assert gTransport() == null;

    }

}