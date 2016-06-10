package feature;

import struct.IntegralImage;
import struct.Rect;

public abstract class Feature {

    public char type;
    public final Rect shape;

    public Feature(Rect r) {
        shape = r;
    }

    @Override
    public String toString() {
        return type + " " + shape.toString();
    }

    public abstract int getValue(IntegralImage img, Rect workingRect);

}
