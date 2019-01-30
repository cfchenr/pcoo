import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

import processing.core.*;
import processing.event.*;

import static java.lang.System.*;

public class programTest extends PApplet {

    static World world = new World();

    static Thread threadWorld = new Thread(world);
    static CCO cco = world.gCCO();

    PImage img1, img2, img3;
    ArrayList<Button> terminalButton = new ArrayList<Button>();

    int value = 100;

    public void settings() {

        fullScreen();

    }

    public void draw() {

        background(value);

        textAlign(PConstants.CENTER);
        text("Centro de Comandos e Operações (CCO)", width / 2, height / 36);

        textAlign(PConstants.LEFT);
        text("Transportes em marcha", width / 34 + 5, height / 14 - 5);

        rectMode(CORNER);
        noFill();
        rect(width / 34, height / 14, (7 * width) / 34 - 5, (12 * height) / 14 - 5);

        int line = 1;
        for (int i = 0; i < cco.gTransports().size(); i++)
            if (cco.gTransports().get(i).gState() == "in transit")
                text(cco.gTransports().get(i).gNumber() + " ("
                        + cco.gTransports().get(i).gType() + "): "
                        + cco.gTransports().get(i).gSource().gName() + " -> "
                        + cco.gTransports().get(i).gDestination().gName(), width / 34 + 5,
                        height / 14 + (height / 38) * line++);

        text("Infraestruturas", 8 * width / 34 + 5, height / 14 - 5);

        line = 0;
        for (int i = 0; i < cco.gInfrastructures().size(); i++) {

            if (i % 3 == 0 && i > 0)
                line++;

            rect(8 * width / 34 + (i % 3) * (8 * width / 34), height / 14 + line * (3 * height / 14),
                    8 * width / 34 - 5, (3 * height) / 14 - 5);

            terminalButton.add(new Button(8 * width / 34 + (i % 3) * (8 * width / 34) + 5,
                    height / 14 + line * (3 * height / 14) + (height / 38),
                    cco.gInfrastructures().get(i).gName() + " (" + cco.gInfrastructures().get(i).gType() + ")"));
            terminalButton.get(i).draw();

            text("Transportes a carregar", 8 * width / 34 + (i % 3) * (8 * width / 34) + 5,
                    height / 14 + line * (3 * height / 14) + 2 * (height / 38));
            if (cco.gInfrastructures().get(i).glSpot().gTransport() != null)
                text(cco.gInfrastructures().get(i).glSpot().gTransport().gNumber() + " ("
                        + cco.gInfrastructures().get(i).glSpot().gTransport().gType() + ")",
                        8 * width / 34 + (i % 3) * (8 * width / 34) + 5,
                        height / 14 + line * (3 * height / 14) + 3 * (height / 38));

            text("Transportes a descarregar", 8 * width / 34 + (i % 3) * (8 * width / 34) + 5,
                    height / 14 + line * (3 * height / 14) + 4 * (height / 38));
            if (cco.gInfrastructures().get(i).guSpot().gTransport() != null)
                text(cco.gInfrastructures().get(i).guSpot().gTransport().gNumber() + " ("
                        + cco.gInfrastructures().get(i).guSpot().gTransport().gType() + ")",
                        8 * width / 34 + (i % 3) * (8 * width / 34) + 5,
                        height / 14 + line * (3 * height / 14) + 5 * (height / 38));

            int columnInfrastructure = 0;
            int lineInfrastructure = 1;
            text("Transportes estacionadas ou em espera", 8 * width / 34 + (i % 3) * (8 * width / 34) + 5,
                    height / 14 + line * (3 * height / 14) + 6 * (height / 38));
            for (int j = 0; j < cco.gTransports().size(); j++)
                if (cco.gTransports().get(j).gState() != "in transit"
                        && cco.gTransports().get(j).gPosition() == cco.gInfrastructures().get(i).gPosition()
                        && cco.gInfrastructures().get(i).glSpot().gTransport() != cco.gTransports().get(j)
                        && cco.gInfrastructures().get(i).guSpot().gTransport() != cco.gTransports().get(j))
                    text(cco.gTransports().get(j).gNumber() + " (" + cco.gTransports().get(j).gType() + ")",
                            8 * width / 34 + (i % 3) * (8 * width / 34) + 5 + columnInfrastructure++ * (width / 34),
                            height / 14 + line * (3 * height / 14) + 7 * (height / 38));

            if (columnInfrastructure % 6 == 0 && columnInfrastructure != 0) {
                columnInfrastructure = 0;
                lineInfrastructure++;
            }

        }

        for (int i = 0; i < terminalButton.size(); i++)
            if (terminalButton.get(i).gDisplay()) {

                background(value);
                rect(0, 0, width, height);

                textAlign(PConstants.CENTER);
                text(terminalButton.get(i).gName(), width / 2, height / 36);

                Infrastructure temp = cco.gInfrastructures().get(0);

                for (int j = 0; j < cco.gInfrastructures().size(); j++)
                    if (cco.gInfrastructures().get(j).gName() == terminalButton.get(i).gName())
                        temp = cco.gInfrastructures().get(j);

                textAlign(PConstants.LEFT);
                text("Contentores para carregar", width / 34 + 5, height / 14 - 5);

                line = 1;
                int column = 1;
                for (int j = 0; j < temp.glContainers().size(); j++) {

                    if (j > 0 && j % 20 == 0)
                        column++;

                    text(temp.glContainers().get(j).gNumber() + ": "
                            + temp.glContainers().get(j).gDestination().gName(), column * (width / 34 + 5),
                            height / 14 + (height / 38) * line++);

                }

                text("Contentores descarregados", 17 * width / 34 + 5, height / 14 - 5);

                line = 1;
                column = 1;
                for (int j = 0; j < temp.guContainers().size(); j++) {

                    if (j > 0 && j % 20 == 0)
                        column++;

                    text(temp.guContainers().get(j).gNumber(), column * (17 * width / 34 + 5),
                            height / 14 + (height / 38) * line++);

                }

            }

    }

    public void mouseClicked() {

        for (int i = 0; i < terminalButton.size(); i++)
            if (terminalButton.get(i).over())
                terminalButton.get(i).sDisplay(true);
            else
                terminalButton.get(i).sDisplay(false);

    }

    class Button {

        int x, y;
        String label;
        boolean display = false;

        Button(int x, int y, String label) {

            this.x = x;
            this.y = y;
            this.label = label;

        }

        void draw() {

            text(label, x, y);

        }

        boolean over() {

            if (mouseX >= x && mouseY >= y - 22 && mouseX <= x + textWidth(label) && mouseY <= y)
                return true;

            return false;

        }

        void sDisplay(boolean tf) {

            display = tf;

        }

        boolean gDisplay() {

            return display;

        }

        String gName() {

            return label;

        }

    }

    public static void main(String[] args) {

        threadWorld.start();

        PApplet.main(new String[] { programTest.class.getName() });

    }

}