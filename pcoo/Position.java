public class Position {

    private int x, y;

    public Position(int x) {

        this.x = x;

        y = 1;

    }

    public Position(int x, int y) {

        this.x = x;
        this.y = y;

    }

    public void sX(int x) {

        this.x = x;

    }

    public int gX() {

        return x;

    }

    public void sY(int y) {

        this.y = y;

    }

    public int gY() {

        return y;

    }

    public double gDistance(Position position) {

        return Math.sqrt(Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2));

    }

}