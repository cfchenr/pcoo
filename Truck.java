public class Truck extends Transport implements Runnable {

    public Truck(int number, Infrastructure source, CCO cco) throws InterruptedException {

        super(number, source, cco);

        smContainers((int) (Math.random() * ((2 - 1) + 1) + 2));
        sVelocity((int) (Math.random() * ((90 - 40) + 1) + 40));

        sType("Truck");

    }

}