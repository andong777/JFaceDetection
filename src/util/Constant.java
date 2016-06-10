package util;

import struct.Rect;

public class Constant {

    public static final String FEATURE_PATH = "trainingResults/features.xml";
    public static final String WEAK_CLASSIFIERS_PATH = "trainingResults/weakClassifiers.xml";
    public static final String TIME_PATH = "trainingResults/time.txt";
    public static final String FACE_PATH = "trainingData/faces/";
    public static final String NONFACE_PATH = "trainingData/nonfaces/";
    public static final String TESTING_DATA_PATH = "testingData/newtest";
    public static final String TESTING_RESULTS_PATH = "testingResults/";
    public static final String STRONG_CLASSIFIER_WEAK_CLASSIFIERS_PATH = "trainingResults/strongClassifier.xml";

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;
    public static final Rect STD_RECT = new Rect(0, 0, SIZE_X, SIZE_Y);

    public static final int FACE_NUM = 2706;
    public static final int NONFACE_NUM = 4381;

    public static final String TRAINING_DATA_IMAGE_EXT = ".bmp";
    public static final String TESTING_DATA_IMAGE_EXT = ".gif";

}
