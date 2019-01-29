import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

import processing.core.*;
import processing.event.*;

import static java.lang.System.*;

public class programTest extends PApplet {
 
    static CCO cco;
    static int contentorNumber = 0;

    PImage img1, img2, img3;
    ArrayList<Button> terminalButton = new ArrayList<Button>();
    
    int value = 100;

    public void settings () {
       
        fullScreen();
    
    }

    public void draw () {

        background(value);

        textAlign(PConstants.CENTER);
        text("Centro de Comandos e Operações (CCO)", width/2, height/36);

        textAlign(PConstants.LEFT);
        text("Máquinas em marcha", width/34 + 5, height/14 - 5);

        rectMode(CORNER);
        noFill();
        rect(width/34, height/14, (7*width)/34 - 5, (12*height)/14 - 5);

        int line = 1;
        for (int i = 0; i < cco.gTransports().size(); i++)
            if (cco.gTransports().get(i).gState() == "in transit")
                text( cco.gTransports().get(i).gNumber() +": " + cco.gTransports().get(i).gSource().gName() + " -> " + cco.gTransports().get(i).gDestination().gName(), width/34 + 5, height/14 + (height/38)*line++);

        text("Terminais", 8*width/34 + 5, height/14 - 5);

        line = 0;
        for (int i = 0; i < cco.gTerminals().size(); i++) {

            if (i%3 == 0 && i > 0) 
                line++;

            rect(8*width/34 + (i%3)*(8*width/34), height/14 + line*(3*height/14), 8*width/34 - 5, (3*height)/14 - 5);

            terminalButton.add(new Button(8*width/34 + (i%3)*(8*width/34) + 5, height/14 + line*(3*height/14) + (height/38), cco.gTerminals().get(i).gName()));
            terminalButton.get(i).draw();
            
            text("Máquinas a carregar", 8*width/34 + (i%3)*(8*width/34) + 5, height/14 + line*(3*height/14) + 2*(height/38));          
            if (cco.gTerminals().get(i).glRailway().gTransport() != null)
                text(cco.gTerminals().get(i).glRailway().gTransport().gNumber(),  8*width/34 + (i%3)*(8*width/34) + 5, height/14 + line*(3*height/14) + 3*(height/38));

            text("Máquinas a descarregar", 8*width/34 + (i%3)*(8*width/34) + 5, height/14 + line*(3*height/14) + 4*(height/38));
            if (cco.gTerminals().get(i).guRailway().gTransport() != null)
                text(cco.gTerminals().get(i).guRailway().gTransport().gNumber(),  8*width/34 + (i%3)*(8*width/34) + 5, height/14 + line*(3*height/14) + 5*(height/38));

            int columnTerminal = 0;
            int lineTerminal = 1;
            text("Máquinas estacionadas ou em espera", 8*width/34 + (i%3)*(8*width/34) + 5, height/14 + line*(3*height/14) + 6*(height/38));
            for (int j = 0; j < cco.gTransports().size(); j++)
                if (cco.gTransports().get(j).gState() != "in transit" && cco.gTransports().get(j).gPosition() == cco.gTerminals().get(i).gPosition() && cco.gTerminals().get(i).glRailway().gTransport() != cco.gTransports().get(j) && cco.gTerminals().get(i).guRailway().gTransport() != cco.gTransports().get(j))
                    text(cco.gTransports().get(j).gNumber(),  8*width/34 + (i%3)*(8*width/34) + 5 + columnTerminal++*(width/34), height/14 + line*(3*height/14) + 7*(height/38));

                if (columnTerminal % 6 == 0 && columnTerminal != 0) {
                    columnTerminal = 0;
                    lineTerminal++;
                }
        
        }

        for (int i = 0; i < terminalButton.size(); i++)
            if (terminalButton.get(i).gDisplay()) {

                background(value);
                rect(0,0,width,height);

                textAlign(PConstants.CENTER);
                text(terminalButton.get(i).gName(), width/2, height/36);

                Terminal temp = cco.gTerminals().get(0);

                for (int j = 0; j < cco.gTerminals().size(); j++)
                    if (cco.gTerminals().get(j).gName() == terminalButton.get(i).gName())
                        temp = cco.gTerminals().get(j);

                textAlign(PConstants.LEFT);
                text("Contentores para carregar", width/34 + 5, height/14 - 5);
                
                line = 1;
                int column = 1;
                for (int j = 0; j < temp.glContentores().size(); j++) {

                    if (j > 0 && j%20 == 0)
                        column++;

                    text(temp.glContentores().get(j).gNumber() + ": " + temp.glContentores().get(j).gDestination().gName(), column*(width/34 + 5), height/14 + (height/38)*line++);

                }

                text("Contentores descarregados", 17*width/34 + 5, height/14 - 5);

                line = 1;
                column = 1;
                for (int j = 0; j < temp.guContentores().size(); j++) {

                    if (j > 0 && j%20 == 0)
                        column++;

                    text(temp.guContentores().get(j).gNumber(), column*(17*width/34 + 5), height/14 + (height/38)*line++);

                }


            }


    }

    public void mouseClicked () {

        for (int i = 0; i < terminalButton.size(); i++)
            if (terminalButton.get(i).over())
                terminalButton.get(i).sDisplay(true);
            else
                terminalButton.get(i).sDisplay(false);

    }

    class Button {

        int x,y;
        String label;
        boolean display = false;
        
        Button(int x, int y, String label) {
        
            this.x = x;
            this.y = y;
            this.label = label;
        
        }

        void draw () {

            text(label, x, y);

        }

        boolean over () {

            if(mouseX >= x && mouseY >= y - 22 && mouseX <= x + textWidth(label) && mouseY <= y)
                return true;

            return false;

        }
        
        void sDisplay (boolean tf) {

            display = tf;

        }

        boolean gDisplay () {

            return display;

        }

        String gName () {

            return label;

        }

    }

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

            ccoThread.setDaemon(true);
            ccoThread.start();

            for(int f = 0; f < trains.size(); f++) {
        
                trains.get(f).setDaemon(true);
                trains.get(f).start();
        
            }

            ccoThread.join();

            for(int f = 0; f < trains.size(); f++)
                trains.get(f).join();

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