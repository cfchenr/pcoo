import java.util.*;
import java.io.*;

public class World implements Runnable {

    static int contentorNumber = 0;
    static int transportNumber = 0;
    static CCO cco;

    static String[] citys = { "Guimaraes", "Braga", "Porto", "Pampilhosa", "Coimbra", "Entroncamento", "Santarem",
            "Lisboa", "Setubal", "Faro" };

    public World() {

        try {

            sCCO(new CCO());

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    public void run() {

        try {

            Thread ccoThread = new Thread(cco, "cco");

            int numberInfrastructures = 0;

            while (numberInfrastructures <= 0)
                numberInfrastructures = (int) (Math.random() * 10);

            ArrayList<Infrastructure> infrastructures = new ArrayList<Infrastructure>();

            createInfrastructures(numberInfrastructures, infrastructures);

            for (int i = 0; i < infrastructures.size(); i++) {

                int numberContainers = (int) (Math.random() * 20);

                infrastructures.get(i)
                        .alContainers(createContainers(infrastructures.get(i), numberContainers, infrastructures));

            }

            ArrayList<Thread> transports = new ArrayList<Thread>();

            int numberTransports = 0;

            while (numberTransports <= 0)
                numberTransports = (int) (Math.random() * 10);

            numberTransports += numberInfrastructures;

            createTransports(numberTransports, transports, infrastructures);

            ccoThread.setDaemon(true);
            ccoThread.start();

            for (int f = 0; f < transports.size(); f++) {

                transports.get(f).setDaemon(true);
                transports.get(f).start();

            }

            ccoThread.join();

            for (int f = 0; f < transports.size(); f++)
                transports.get(f).join();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

    public static void sCCO(CCO cco1) {

        cco = cco1;

    }

    public CCO gCCO() {

        return cco;

    }

    public static void createInfrastructures(int numberInfrastructures, ArrayList<Infrastructure> infrastructures) {

        for (int i = 0; i < numberInfrastructures; i++) {

            boolean check = true;
            int cityId;

            do {
                
                check = true;
                cityId = (int) (Math.random() * citys.length);

                for (int j = 0; j < infrastructures.size(); j++)

                    if (infrastructures.get(j).equals(citys[cityId])) {

                        check = false;
                        break;

                    }

            } while (!check);

            int cityLocation = (int) (Math.random() * 500);
            int dec = (int) (Math.random() * 2);

            if (dec > 0.5)
                infrastructures.add(new Terminal(citys[cityId], cityLocation, cco));
            else
                infrastructures.add(new Dock(citys[cityId], cityLocation, cco));

        }

    }

    public static ArrayList<Container> createContainers(Infrastructure infrastructure, int size,
            ArrayList<Infrastructure> destinations) {

        ArrayList<Container> containers = new ArrayList<Container>();

        for (int j = 0; j < size; j++) {

            int destination = (int) (Math.random() * ((destinations.size() - 1 - 0) + 1) + 0);

            while (destinations.get(destination) == infrastructure)
                destination = (int) (Math.random() * ((destinations.size() - 1 - 0) + 1) + 0);

            containers.add(new Container(++contentorNumber, destinations.get(destination)));

        }

        return containers;

    }

    public static void createTransports(int numberTransports, ArrayList<Thread> transports,
            ArrayList<Infrastructure> infrastructures) {

        for (int i = 0; i < numberTransports; i++) {

            int cityId;
            int increment;

            do {

                do {

                    cityId = (int) (Math.random() * (infrastructures.size() - 1));
                    increment = (int) (Math.random() * 10);
                    while (increment >= infrastructures.size())
                        increment -= infrastructures.size();

                } while (cityId == increment);

            } while (cityId >= infrastructures.size() && increment >= infrastructures.size());

            try {

                int dec = (int) (Math.random() * 2);

                if (dec > 0.5)

                    transports.add(new Thread(new Truck(++transportNumber, infrastructures.get(cityId),
                            infrastructures.get(increment), cco), Integer.toString(transportNumber)));

                else {

                    if (infrastructures.get(cityId).gType() == "Terminal")

                        transports.add(new Thread(new Train(++transportNumber, infrastructures.get(cityId),
                                infrastructures.get(increment), cco), Integer.toString(transportNumber)));

                    else if (infrastructures.get(cityId).gType() == "Dock")

                        transports.add(new Thread(new Ship(++transportNumber, infrastructures.get(cityId),
                                infrastructures.get(increment), cco), Integer.toString(transportNumber)));

                }

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

        }

    }

}