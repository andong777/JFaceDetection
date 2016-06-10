package learn;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import feature.Feature;
import util.TrainHelper;
import struct.IntegralImage;
import struct.Label;
import struct.Sample;
import struct.WeightedSample;
import util.Constant;
import util.Utils;

public class AdaBoost {

    private ArrayList<WeightedSample> samples;
    private ArrayList<Feature> features;

    private XStream xstream;

    public AdaBoost() {
        xstream = new XStream();
        Utils.makeXstreamAlias(xstream);

        initialize();
    }

    private void initialize() {
        samples = new ArrayList<>();
        ArrayList<Sample> unweightedSamples = TrainHelper.generateAllSamples();
        for (Sample us : unweightedSamples) {
            IntegralImage img = us.image;
            Label label = us.label;
            samples.add(new WeightedSample(img, label,
                    label == Label.FACE ? 1d / Constant.FACE_NUM : 1d / Constant.NONFACE_NUM));
        }

        features = TrainHelper.generateAllFeatures();
    }

    public WeightedWeakClassifier getNewClassifier() {
        normalizeWeights();

        double minErr = 1000000d;
        WeakClassifier chosen = null;
        ArrayList<Integer> correctOnes = null;
        for (Feature f: features) {
            WeakClassifier[] wkClfs = trainWeakClassifier(f);
            for (int i = 0; i < wkClfs.length; i++) {
                WeakClassifier wkCf = wkClfs[i];
                ArrayList<Integer> temCorrectOnes = new ArrayList<Integer>();
                double sumErr = 0d;
                for (int j = 0; j < samples.size(); j++) {
                    WeightedSample ws = samples.get(j);
                    if (wkCf.classify(ws.image, Constant.STD_RECT) != ws.label) {
                        sumErr += ws.weight;
                    } else {
                        temCorrectOnes.add(j);
                    }
                }
                if (sumErr < minErr) {
                    minErr = sumErr;
                    chosen = wkCf;
                    correctOnes = temCorrectOnes;
                }
            }
        }
        WeightedWeakClassifier wwc = new WeightedWeakClassifier(chosen, Math.log(1d / minErr));
        updateWeights(minErr, correctOnes);
        return wwc;
    }

    private WeakClassifier[] trainWeakClassifier(Feature f) {
        double faceSum = 0, nonfaceSum = 0;
        double faceWeight = 0, nonfaceWeight = 0;
        for (WeightedSample s : samples) {
            int val = f.getValue(s.image, f.shape);
            if (s.label == Label.FACE) {
                faceSum += s.weight * val;
                faceWeight += s.weight;
            } else {
                nonfaceSum += s.weight * val;
                nonfaceWeight += s.weight;
            }
        }
        double weightedFaceAvg = faceSum / faceWeight,
                weightedNonfaceAvg = nonfaceSum / nonfaceWeight;
        int threshold = (int)(weightedFaceAvg + weightedNonfaceAvg) / 2;
        WeakClassifier wkClf0 = new WeakClassifier(f, -1, threshold);
        WeakClassifier wkClf1 = new WeakClassifier(f, 1, threshold);
        return new WeakClassifier[]{wkClf0, wkClf1};
    }

    private void normalizeWeights() {
        double sum = 0d;
        for (WeightedSample s : samples) {
            sum += s.weight;
        }
        for (WeightedSample s : samples) {
            s.weight = s.weight / sum;
        }
    }

    private void updateWeights(double minErr, ArrayList<Integer> correctOnes) {
        for (int i : correctOnes) {
            WeightedSample ws = samples.get(i);
            ws.weight = ws.weight * (minErr / (1d - minErr));
        }
    }

}
