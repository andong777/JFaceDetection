package learn;

import feature.Feature;
import struct.IntegralImage;
import struct.Label;
import struct.Rect;

public class WeightedWeakClassifier implements Classifier {

    public WeakClassifier classifier;
    public double weight;

    public WeightedWeakClassifier(WeakClassifier clf, double weight) {
        this.classifier = clf;
        this.weight = weight;
    }

    @Override
    public Label classify(IntegralImage img, Rect windowRect) {
        return classifier.classify(img, windowRect);
    }
}
