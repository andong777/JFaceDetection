package learn;

import feature.Feature;
import struct.IntegralImage;
import struct.Label;
import struct.Rect;
import util.Constant;

public class WeakClassifier implements Classifier {

    private Feature feature;
    private int parity, threshold;

    public WeakClassifier(Feature feature, int parity, int threshold) {
        this.feature = feature;
        this.parity = parity;
        this.threshold = threshold;
    }

    private double getScale(Rect windowRect) {
        return (double) windowRect.width / (double) Constant.SIZE_X;
    }

    private Rect getWorkingRect(Rect windowRect) {
        Rect featureRect = feature.shape;
        double scale = getScale(windowRect);
        int x = (int) Math.floor(scale * featureRect.x) + windowRect.x;
        int y = (int) Math.floor(scale * featureRect.y) + windowRect.y;
        int w = (int) Math.floor(scale * featureRect.width);
        int h = (int) Math.floor(scale * featureRect.height);
        return new Rect(x, y, w, h);
    }

    @Override
    public Label classify(IntegralImage img, Rect windowRect) {
        double scale = getScale(windowRect);
        Rect workingRect = getWorkingRect(windowRect);
        int scaledValue = feature.getValue(img, workingRect);
        int scaledThreshold = (int) Math.round(threshold * scale * scale);
        if (parity * scaledValue > parity * scaledThreshold) {
            return Label.FACE;
        } else {
            return Label.NONFACE;
        }
    }

    @Override
    public String toString() {
        return "feature.Feature = " + feature.toString() + "\n" +
                "Parity = " + parity + "\n" +
                "Threshold = " + threshold;
    }

}
