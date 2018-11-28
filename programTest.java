import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

import static java.lang.System.*;

public class programTest {

    static int contentorNumber = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        
        CCO cco = new CCO();

        Terminal tGuimaraes = new Terminal("Guimaraes", 0, cco);
        Terminal tBraga = new Terminal("Braga", 15, cco);
        Terminal tPorto = new Terminal("Porto", 45, cco);
        Terminal tPampilhosa = new Terminal("Pampilhosa", 110, cco);
        Terminal tCoimbra = new Terminal("Coimbra", 160, cco);
        Terminal tEntroncamento = new Terminal("Entroncamento", 220, cco);
        Terminal tSantarem = new Terminal("Santarem", 280, cco);
        Terminal tLisboa = new Terminal("Lisboa", 360, cco);
        Terminal tSetubal = new Terminal("Setubal", 400, cco);
        Terminal tFaro = new Terminal("Faro", 510, cco);

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
        
        tGuimaraes.addlContentores(createContentores(tGuimaraes, 1, destinations));
        tBraga.addlContentores(createContentores(tBraga, 1, destinations));
        tPorto.addlContentores(createContentores(tPorto, 1, destinations));
        tPampilhosa.addlContentores(createContentores(tPampilhosa, 1, destinations));
        tCoimbra.addlContentores(createContentores(tCoimbra, 1, destinations));
        tEntroncamento.addlContentores(createContentores(tEntroncamento, 1, destinations));
        tSantarem.addlContentores(createContentores(tSantarem, 1, destinations));
        tLisboa.addlContentores(createContentores(tLisboa, 1, destinations));
        tSetubal.addlContentores(createContentores(tSetubal, 1, destinations));
        tFaro.addlContentores(createContentores(tFaro, 1, destinations));

        ArrayList<Thread> trains = new ArrayList<Thread>();
        trains.add(new Thread(new Train(501, tGuimaraes, tBraga, cco), "501"));
        trains.add(new Thread(new Train(502, tBraga, tPorto, cco), "502"));
        trains.add(new Thread(new Train(503, tPorto, tPampilhosa, cco), "503"));
        trains.add(new Thread(new Train(504, tPampilhosa, tCoimbra, cco), "504"));
        trains.add(new Thread(new Train(505, tCoimbra, tEntroncamento, cco), "505"));
        trains.add(new Thread(new Train(506, tEntroncamento, tSantarem, cco), "506"));
        trains.add(new Thread(new Train(507, tSantarem, tLisboa, cco), "507"));
        trains.add(new Thread(new Train(508, tLisboa, tSetubal, cco), "508"));
        trains.add(new Thread(new Train(509, tSetubal, tFaro, cco), "509"));
        trains.add(new Thread(new Train(510, tGuimaraes, tPorto, cco), "510"));
        trains.add(new Thread(new Train(511, tBraga, tPampilhosa, cco), "511"));
        trains.add(new Thread(new Train(512, tPorto, tCoimbra, cco), "512"));
        trains.add(new Thread(new Train(513, tPampilhosa, tEntroncamento, cco), "513"));
        trains.add(new Thread(new Train(514, tCoimbra, tSantarem, cco), "514"));
        trains.add(new Thread(new Train(515, tEntroncamento, tLisboa, cco), "515"));
        trains.add(new Thread(new Train(516, tSantarem, tSetubal, cco), "516"));
        trains.add(new Thread(new Train(517, tLisboa, tFaro, cco), "517"));
        trains.add(new Thread(new Train(518, tSetubal, tGuimaraes, cco), "518"));   
       
        for (int k = 0; k < cco.gTerminals().size(); k++) {

            System.out.print(cco.gTerminals().get(k).gName() + " contentores to load: ");
            
            for (int f = 0; f < cco.gTerminals().get(k).glContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).glContentores().get(f).gNumber() + " [" + cco.gTerminals().get(k).glContentores().get(f).gDestination().gName() + "]");
            
            System.out.println();
            System.out.print(cco.gTerminals().get(k).gName() + " contentores unloaded: ");

            for (int f = 0; f < cco.gTerminals().get(k).guContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).guContentores().get(f).gNumber() + " ");
            
                System.out.println();

        }

        for(int f = 0; f < trains.size(); f++) {
	
			trains.get(f).setDaemon(true);
			trains.get(f).start();
	
        }
        
        //System.out.println("All start");

		for(int f = 0; f < trains.size(); f++)
            trains.get(f).join();

        //System.out.println("All join");

        for (int k = 0; k < cco.gTerminals().size(); k++) {
            System.out.print(cco.gTerminals().get(k).gName() + " contentores to load: ");
            for (int f = 0; f < cco.gTerminals().get(k).glContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).glContentores().get(f).gNumber() + " [" + cco.gTerminals().get(k).glContentores().get(f).gDestination().gName() + "]");
            System.out.println();
            System.out.print(cco.gTerminals().get(k).gName() + " contentores unloaded: ");
            for (int f = 0; f < cco.gTerminals().get(k).guContentores().size(); f++) 
                System.out.print(" " + cco.gTerminals().get(k).guContentores().get(f).gNumber());
            System.out.println();
        }

    }

    public static ArrayList<Contentor> createContentores (Terminal terminal, int size, Terminal [] destinations) {

        ArrayList<Contentor> contentors = new ArrayList<Contentor>();

        for (int j = 0; j < size; j++) {
            int destination = (int)(Math.random()*((destinations.length-1 - 0)+1)+0);
            while (destinations[destination] == terminal)
                destination = (int)(Math.random()*((destinations.length-1 - 0)+1)+0);            

            contentors.add(new Contentor(++contentorNumber, destinations[destination]));
        }

        return contentors;

    }

}