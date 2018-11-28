import java.util.*;

public class CCO {

    private BinarySemaphore sMutex;
    private ArrayList<Terminal> terminals;

    public CCO (ArrayList<Terminal> terminals) {

        sMutex = new BinarySemaphore(1);
        this.terminals = terminals;

    }

    /**
     * Define a permição do comboio para espera
     */
    private void setPermitionToWait (Train train) {

        train.setPermitionToWait(true);

    }

    /**
     * Define a permição do comboio para descarregar
     */
    private void setPermitionToUnload (Train train) {

        train.setPermitionToUnload(true);

    }

    /**
     * Define a permição do comboio para carregar
     */
    private void setPermitionToLoad (Train train) {

        train.setPermitionToLoad(true);

    }

    /**
     * Reserva a linha de descarregamento da estação origem do comboio (para ele próprio)
     */
    private void setUnloadRailwayReserve (Train train) {

        train.getSource().getUnloadRailway().setReserve(train);

    }

    /**
     * Reserva a linha de carregamento da estação origem do comboio (para ele próprio)
     */
    private void setLoadRailwayReserve (Train train) {

        train.getSource().getLoadRailway().setReserve(train);

    }

    private void setCloseOpenAllTerminal () {

        for (int i = 0; i < terminals.size(); i++)

            setCloseTerminal(terminals.get(i));

    }

    private void setCloseOpenTerminal (Terminal terminal) {

        if (terminal.getLoadContentores().size() == 0 && terminal.getLoadRailway().getReserve() == null && terminal.getUnloadRailway().getReserve() == null)

            terminal.setClose(true);

        else

            terminal.setClose(false);

    }    

}