public class Train extends Transport {

    public Train (int number, Terminal source, Terminal destination, CCO cco) throws InterruptedException {

        super(number, source, destination, cco);

        smContentores((int)(Math.random()*((40 - 15) + 1) + 15));
        sVelocity((int)(Math.random()*((180 - 110) + 1) + 110));

    }

}