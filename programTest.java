import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

import processing.core.*;
import processing.event.*;

import static java.lang.System.*;

public class programTest extends PApplet {
 
    public void settings () {
       
        fullScreen();
    
    }

    PImage img1, img2, img3;

    public void setup () {

        img1 = loadImage("img/container.png");
        img2 = loadImage("img/coal.png");
        img3 = loadImage("img/locomotive.png");

    }

    public void draw () {

        background(100);
        fill(255);

        textAlign(PConstants.CENTER);
        text("Gestão de terminais de carga/descarga", width/2, 30);


        int line = 0;
        int line2 = 0;
        int column = 0;
        int column2 = 0;

        for (int k = 0; k < cco.gTerminals().size(); k++) {


            line++;
            line2++;
            int lineM = max(line, line2);

            column = 0;
            column2 = 0;

            textAlign(PConstants.LEFT);

            text("Contentores disponíveis em " + cco.gTerminals().get(k).gName() + ": ", width/16, 60 + (40*lineM));
            text("Contentores descarregados em " + cco.gTerminals().get(k).gName() + ": ", 12*width/16, 60 + (40*lineM));

            line = lineM + 1;
            line2 = lineM + 1;

            text("A carregar", width/16+(40*column), 60 + (40*line));
            if (cco.gTerminals().get(k).glRailway().gTrain() != null) {
                image(img3, width/16+(40*column), 60 + (40*line));
                for (int f = 0; f < cco.gTerminals().get(k).glRailway().gTrain().gContentores().size(); f++)
                    image(img1, width/16+(40*++column), 60 + (40*line));

            }

            column = 0;
                            
            text("A descarregar", 12*width/16+(40*column2), 60 + (40*line2));
            if (cco.gTerminals().get(k).guRailway().gTrain() != null) {
                image(img3, 12*width/16+(40*column2), 60 + (40*line2));
                for (int f = 0; f < cco.gTerminals().get(k).guRailway().gTrain().gContentores().size(); f++)
                    image(img1, 12*width/16+(40*++column2), 60 + (40*line2));
            }

            column2 = 0;

            line++;
            line2++;
    
            for (int f = 0; f < cco.gTerminals().get(k).glContentores().size(); f++) {
                
                if (f%12 == 0 && f != 0) {

                    line++;
                    column = 0;

                }

                text(cco.gTerminals().get(k).glContentores().get(f).gNumber(), width/16+(40*column), 60 + (40*line));
                image(img1, width/16+(40*column), 60 + (40*line));
                column++;

            }

            for (int f = 0; f < cco.gTerminals().get(k).guContentores().size(); f++) {
                
                if (f%8 == 12 && f != 0) {

                    line2++;
                    column2 = 0;

                }
                
                image(img1, 12*width/16+(40*column2), 60 + (40*line2));
                text(cco.gTerminals().get(k).guContentores().get(f).gNumber(), 12*width/16+(40*column2), 60 + (40*line2));

                column2++;

            }
            
        }

    }

    public void mouseWheel(MouseEvent event) {
    
        float e = event.getCount();
        System.out.println(e);
    
    }
   
    static CCO cco;

    static int contentorNumber = 0;

    public static void main(String[] args) {

        PApplet.main(new String [] {programTest.class.getName()});

        try {

            cco = new CCO();
    
            Thread ccoThread = new Thread(cco, "cco");

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
            
            tGuimaraes.addlContentores(createContentores(tGuimaraes, 10, destinations));
            tBraga.addlContentores(createContentores(tBraga, 10, destinations));
            tPorto.addlContentores(createContentores(tPorto, 10, destinations));
            tPampilhosa.addlContentores(createContentores(tPampilhosa, 10, destinations));
            tCoimbra.addlContentores(createContentores(tCoimbra, 10, destinations));
            tEntroncamento.addlContentores(createContentores(tEntroncamento, 10, destinations));
            tSantarem.addlContentores(createContentores(tSantarem, 10, destinations));
            tLisboa.addlContentores(createContentores(tLisboa, 10, destinations));
            tSetubal.addlContentores(createContentores(tSetubal, 10, destinations));
            tFaro.addlContentores(createContentores(tFaro, 10, destinations));

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

            ccoThread.setDaemon(true);
            ccoThread.start();

            for(int f = 0; f < trains.size(); f++) {
        
                trains.get(f).setDaemon(true);
                trains.get(f).start();
        
            }

            ccoThread.join();

            for(int f = 0; f < trains.size(); f++)
                trains.get(f).join();

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


        } catch (InterruptedException e) {

            e.printStackTrace();

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