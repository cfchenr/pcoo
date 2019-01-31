public class Position {

    private int x, y;

    public Position(int x) {

        sX(x);

        sY(1);

    }

    public Position(int x, int y) {

        sX(x);
        sY(y);

    }

    public void sX(int x) {

        assert x > 0;

        this.x = x;

        assert gX() == x;

    }

    public int gX() {

        return x;

    }

    public void sY(int y) {

        assert y >= 0;

        this.y = y;

        assert gY() == y;

    }

    public int gY() {

        return y;

    }

    public double gDistance(Position position) {

        assert position != null;

        assert ((this == position) || (Math.sqrt(Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2)) > 0));

        return Math.sqrt(Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2));

    }

}