import pt.ua.concurrent.*;
import java.util.*;
import static java.lang.System.*;

public class programTest {

    static Terminal tGuimaraes = new Terminal("Guimaraes", new Railway (1), new Railway (2));
    static Terminal tBraga = new Terminal("Braga", new Railway (1), new Railway (2));
    static Terminal tPorto = new Terminal("Porto", new Railway (1), new Railway (2));
    static Terminal tPampilhosa = new Terminal("Pampilhosa", new Railway (1), new Railway (2));
    static Terminal tCoimbra = new Terminal("Coimbra", new Railway (1), new Railway (2));
    static Terminal tEntroncamento = new Terminal("Entroncamento", new Railway (1), new Railway (2));
    static Terminal tSantarem = new Terminal("Santarem", new Railway (1), new Railway (2));
    static Terminal tLisboa = new Terminal("Lisboa", new Railway (1), new Railway (2));
    static Terminal tSetubal = new Terminal("Setubal", new Railway (1), new Railway (2));
    static Terminal tFaro = new Terminal("Faro", new Railway (1), new Railway (2));

    static int contentorNumber = 0;

    public static void main(String[] args) throws InterruptedException {

        ArrayList<Terminal> terminals = new ArrayList<Terminal>();
        terminals.add(tGuimaraes);
        terminals.add(tBraga);
        terminals.add(tPorto);
        terminals.add(tPampilhosa);
        terminals.add(tCoimbra);
        terminals.add(tEntroncamento);
        terminals.add(tLisboa);
        terminals.add(tSetubal);
        terminals.add(tFaro);
    
        BinarySemaphore sMutex = new BinarySemaphore(1);
        CCO cco = new CCO(sMutex, terminals);
        
        tGuimaraes.addlContentores(createContentores(tGuimaraes, 20));
        tBraga.addlContentores(createContentores(tBraga, 20));
        tPorto.addlContentores(createContentores(tPorto, 20));
        tPampilhosa.addlContentores(createContentores(tPampilhosa, 20));
        tCoimbra.addlContentores(createContentores(tCoimbra, 20));
        tEntroncamento.addlContentores(createContentores(tEntroncamento, 20));
        tSantarem.addlContentores(createContentores(tSantarem, 20));
        tLisboa.addlContentores(createContentores(tLisboa, 20));
        tSetubal.addlContentores(createContentores(tSetubal, 20));
        tFaro.addlContentores(createContentores(tFaro, 20));

        Thread [] train = new Thread [9];

        train[0] = new Thread(new Train(501, tGuimaraes, tBraga, 10, cco), "501");
        train[1] = new Thread(new Train(502, tBraga, tPorto, 10, cco), "502");
        train[2] = new Thread(new Train(503, tPorto, tPampilhosa, 10, cco), "503");
        train[3] = new Thread(new Train(504, tPampilhosa, tCoimbra, 10, cco), "504");
        train[4] = new Thread(new Train(505, tCoimbra, tEntroncamento, 10, cco), "505");
        train[5] = new Thread(new Train(506, tEntroncamento, tSantarem, 10, cco), "506");
        train[6] = new Thread(new Train(507, tSantarem, tLisboa, 10, cco), "507");
        train[7] = new Thread(new Train(508, tLisboa, tSetubal, 10, cco), "508");
        train[8] = new Thread(new Train(509, tSetubal, tFaro, 10, cco), "509");
        
        for (int k = 0; k < cco.gTerminals().size(); k++) {
            System.out.print(cco.gTerminals().get(k).gName() + " contentores to load: ");
            for (int f = 0; f < cco.gTerminals().get(k).glContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).glContentores().get(f).gNumber() + " ");
            System.out.println();
            System.out.print(cco.gTerminals().get(k).gName() + " contentores unloaded: ");
            for (int f = 0; f < cco.gTerminals().get(k).guContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).guContentores().get(f).gNumber() + " ");
            System.out.println();
        }

        for(int f = 0; f < train.length; f++) {
	
			train[f].setDaemon(true);
			train[f].start();
	
        }
            
		for(int f = 0; f < train.length; f++)
            train[f].join();


        for (int k = 0; k < cco.gTerminals().size(); k++) {
            System.out.print(cco.gTerminals().get(k).gName() + " contentores to load: ");
            for (int f = 0; f < cco.gTerminals().get(k).glContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).glContentores().get(f).gNumber() + " ");
            System.out.println();
            System.out.print(cco.gTerminals().get(k).gName() + " contentores unloaded: ");
            for (int f = 0; f < cco.gTerminals().get(k).guContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).guContentores().get(f).gNumber() + " ");
            System.out.println();
        }

    }

    public static ArrayList<Contentor> createContentores (Terminal terminal, int size) {

        ArrayList<Contentor> contentors = new ArrayList<Contentor>();

        Terminal [] destinations = new Terminal [10];
        destinations [0] = tGuimaraes;
        destinations [1] = tBraga;
        destinations [2] = tPorto;
        destinations [3] = tPampilhosa;
        destinations [4] = tCoimbra;
        destinations [5] = tEntroncamento;
        destinations [6] = tSantarem;
        destinations [7] = tLisboa;
        destinations [8] = tSetubal;
        destinations [9] = tFaro;

        for (int j = 0; j < size; j++) {
            int destination = (int)(Math.random()*((9 - 0)+1)+0);
            while (destinations[destination] == terminal)
                destination = (int)(Math.random()*((9 - 0)+1)+0);            

            contentors.add(new Contentor(++contentorNumber, destinations[destination]));
        }

        return contentors;

    }

}