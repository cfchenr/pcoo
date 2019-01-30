public class Ship extends Transport implements Runnable {

    public Ship(int number, Infrastructure source, Infrastructure destination, CCO cco) throws InterruptedException {

        super(number, source, destination, cco);

        smContainers((int) (Math.random() * ((90 - 40) + 1) + 40));
        sVelocity((int) (Math.random() * ((180 - 110) + 1) + 110));

        sType("Ship");

    }

}