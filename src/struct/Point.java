package struct;

public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance(Point aPoint) {
        return Math.sqrt(Math.pow(aPoint.x - x, 2.0d) + Math.pow(aPoint.y - y, 2.0d));
    }

}
