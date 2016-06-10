package feature;

import struct.IntegralImage;
import struct.Rect;

public class FeatureC extends Feature {

    public FeatureC(Rect shape) {
        super(shape);
        type = 'C';
    }

    @Override
    public int getValue(IntegralImage img, Rect workingRect) {
        int x = workingRect.x, y = workingRect.y,
                w = workingRect.width, h = workingRect.height;
        Rect workingRect1 = new Rect(x, y, w, h);
        Rect workingRect2 = new Rect(x + 2 * w, y, w, h);
        Rect workingRect3 = new Rect(x + w, y, w, h);
        return img.sumValue(workingRect1) + img.sumValue(workingRect2)
                - img.sumValue(workingRect3);
    }

}
