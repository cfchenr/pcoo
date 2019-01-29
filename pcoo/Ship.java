public class Ship extends Transport implements Runnable {

    public Ship(int number, Dock source, Dock destination, CCO cco) throws InterruptedException {

        super(number, source, destination, cco);

        smContainers((int) (Math.random() * ((40 - 15) + 1) + 15));
        sVelocity((int) (Math.random() * ((180 - 110) + 1) + 110));

    }

}