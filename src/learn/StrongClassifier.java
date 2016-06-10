package learn;

import struct.IntegralImage;
import struct.Label;
import struct.Rect;

import java.util.ArrayList;

public class StrongClassifier implements Classifier {

    private ArrayList<WeightedWeakClassifier> wwClassifiers;
    private double sumWeights;
    private double threshold;

    public StrongClassifier() {
        wwClassifiers = new ArrayList<>();
        sumWeights = 0d;
        threshold = 0d;
    }

    public void addClassifier(WeightedWeakClassifier wwc) {
        wwClassifiers.add(wwc);
        sumWeights += wwc.weight;
        threshold = sumWeights * 0.5;
    }

    public void removeAllClassifiers() {
        wwClassifiers.clear();
        sumWeights = 0d;
        threshold = 0d;
    }

    @Override
    public Label classify(IntegralImage img, Rect windowRect) {
        double sum = 0d;
        for (WeightedWeakClassifier wwc: wwClassifiers) {
            sum += wwc.classify(img, windowRect) == Label.FACE ? wwc.weight : 0d;
        }
        if (sum > threshold/* * 0.82*/ * 1.025) {
            return Label.FACE;
        } else {
            return Label.NONFACE;
        }
    }

}
