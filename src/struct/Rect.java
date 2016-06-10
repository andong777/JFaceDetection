package struct;

public class Rect {

    public int x, y, width, height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Rect clone() {
        return new Rect(x, y, width, height);
    }

    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }

    public Rect meanRect(Rect aPoint) {
        int newWidth = (width + aPoint.width) / 2;
        int newHeight = (height + aPoint.height) / 2;
        int newX = (x + aPoint.x) / 2;
        int newY = (y + aPoint.y) / 2;
        return new Rect(newX, newY, newWidth, newHeight);
    }

    public boolean contains(Rect r) {
        int x = r.x, y = r.y,
                x2 = x + r.width - 1, y2 = y + r.height - 1;
        return contains(x, y) && contains(x2, y2);
    }

    private boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public String toString() {
        return "x = " + x + " y = " + y + " width = " + width + " height = " + height;
    }

}
