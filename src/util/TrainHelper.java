package util;

import com.thoughtworks.xstream.XStream;
import feature.*;
import learn.Classifier;
import learn.WeakClassifier;
import struct.IntegralImage;
import struct.Label;
import struct.Rect;
import struct.Sample;
import util.Constant;
import util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class TrainHelper {

    public static ArrayList<Feature> generateAllFeatures() {
        XStream xstream = new XStream();
        ArrayList<Feature> ret = new ArrayList<Feature>();
        PrintStream output = null;
        try {
            output = new PrintStream(new File(Constant.FEATURE_PATH));
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        for (int x = 0; x < Constant.SIZE_X; x++) {
            for (int y = 0; y < Constant.SIZE_Y; y++) {
                for (int w = 1; x + 2 * w - 1 < Constant.SIZE_X; w++) {
                    for (int h = 1; y + h - 1 < Constant.SIZE_Y; h++) {
                        Feature f = new FeatureA(new Rect(x, y, w, h));
                        ret.add(f);
                        output.println(xstream.toXML(f));
                        output.println();
                    }
                }
                for (int w = 1; x + w - 1 < Constant.SIZE_X; w++) {
                    for (int h = 1; y + 2 * h - 1 < Constant.SIZE_Y; h++) {
                        Feature f = new FeatureB(new Rect(x, y, w, h));
                        ret.add(f);
                        output.println(xstream.toXML(f));
                        output.println();
                    }
                }
                for (int w = 1; x + 3 * w - 1 < Constant.SIZE_X; w++) {
                    for (int h = 1; y + h - 1 < Constant.SIZE_Y; h++) {
                        Feature f = new FeatureC(new Rect(x, y, w, h));
                        ret.add(f);
                        output.println(xstream.toXML(f));
                        output.println();
                    }
                }
                for (int w = 1; x + w - 1 < Constant.SIZE_X; w++) {
                    for (int h = 1; y + 3 * h - 1 < Constant.SIZE_Y; h++) {
                        Feature f = new FeatureD(new Rect(x, y, w, h));
                        ret.add(f);
                        output.println(xstream.toXML(f));
                        output.println();
                    }
                }
                for (int w = 1; x + 2 * w - 1 < Constant.SIZE_X; w++) {
                    for (int h = 1; y + 2 * h - 1 < Constant.SIZE_Y; h++) {
                        Feature f = new FeatureE(new Rect(x, y, w, h));
                        ret.add(f);
                        output.println(xstream.toXML(f));
                        output.println();
                    }
                }
            }
        }
        output.close();
        System.out.println(ret.size());
        return ret;
    }

    public static ArrayList<Sample> generateAllSamples() {
        ArrayList<Sample> ret = new ArrayList<Sample>();
        File folder = new File(Constant.FACE_PATH);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(Constant.TRAINING_DATA_IMAGE_EXT)) {
                IntegralImage img = new IntegralImage(file.getPath());
                ret.add(new Sample(img, Label.FACE));
            }
        }
        folder = new File(Constant.NONFACE_PATH);
        listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(Constant.TRAINING_DATA_IMAGE_EXT)) {
                IntegralImage img = new IntegralImage(file.getPath());
                ret.add(new Sample(img, Label.NONFACE));
            }
        }
        return ret;
    }

    public static ArrayList<Classifier> generateWeakClassifiers() {
        XStream xstream = new XStream();
        ArrayList<Classifier> ret = new ArrayList<Classifier>();
        ArrayList<Feature> features = generateAllFeatures();
        ArrayList<Sample> samples = generateAllSamples();

        PrintStream output = null;
        try {
            output = new PrintStream(new File(Constant.WEAK_CLASSIFIERS_PATH));
        } catch (FileNotFoundException e) {
        }

        for (Feature f : features) {
            int faceSum = 0, nonfaceSum = 0;
            for (Sample s : samples) {
                int val = f.getValue(s.image, f.shape);
                if (s.label == Label.FACE) {
                    faceSum += val;
                } else {
                    nonfaceSum += val;
                }
            }
            int faceAvg = faceSum / Constant.FACE_NUM;
            int nonfaceAvg = nonfaceSum / Constant.NONFACE_NUM;
            int threshold = (faceAvg + nonfaceAvg) / 2;
            int parity = faceAvg > nonfaceAvg ? 1 : -1;
            Classifier cf = new WeakClassifier(f, parity, threshold);
            output.println(xstream.toXML(cf));
            output.println();
            ret.add(cf);
        }
        output.close();
        return ret;
    }

    public static void main(String[] args) {
        XStream xstream = new XStream();
        Utils.makeXstreamAlias(xstream);
        PrintStream output = null;
        try {
            output = new PrintStream(new File(Constant.TIME_PATH));
        } catch (FileNotFoundException e) {
        }

        long startTime = System.currentTimeMillis();
//        generateAllFeatures();
        generateWeakClassifiers();
        long timeElapsed = System.currentTimeMillis() - startTime;
        Utils.printTime(output, "Time for training weak classifiers", timeElapsed);
    }

}
