package main;

import com.thoughtworks.xstream.XStream;
import learn.AdaBoost;
import learn.FaceDetector;
import learn.StrongClassifier;
import learn.WeightedWeakClassifier;
import struct.Rect;
import util.Constant;
import util.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        XStream xstream = new XStream();
        Utils.makeXstreamAlias(xstream);
        StrongClassifier clf = new StrongClassifier();

        // adaboost and write to xml
//        File strongClfFile = new File(Constant.STRONG_CLASSIFIER_WEAK_CLASSIFIERS_PATH);
//        if (strongClfFile.exists()) {
//            System.out.println("!!!WARNING!!!\n" +
//                    "Strong classifier exists!\n" +
//                    "Make sure it has been backed up and then delete it manually.");
//            return;
//        }
//
//        AdaBoost boost = new AdaBoost();
//        int T = 200;
//        PrintStream output = null;
//        try {
//            output = new PrintStream(new File(Constant.STRONG_CLASSIFIER_WEAK_CLASSIFIERS_PATH));
//        } catch (FileNotFoundException e) {
//            System.out.println(e);
//        }
//        for (int i = 0; i < T; i++) {
//            long startTime = System.currentTimeMillis();
//            System.out.print("iteration " + i + "... ");
//            WeightedWeakClassifier wwc = boost.getNewClassifier();
//            output.println(xstream.toXML(wwc));
//            output.println();
//            clf.addClassifier(wwc);
//            long timeElapsed = System.currentTimeMillis() - startTime;
//            System.out.println((timeElapsed / 1000) + " s.");
//        }
//        output.close();

        // read strong clf from xml
        clf.removeAllClassifiers(); // in case
        ArrayList<Object> uncastedClassifiers =
                Utils.readXMLFile(xstream, Constant.STRONG_CLASSIFIER_WEAK_CLASSIFIERS_PATH);
        for (Object o : uncastedClassifiers) {
            clf.addClassifier((WeightedWeakClassifier) o);
        }

        // begin testing
        FaceDetector detector = new FaceDetector(clf);
        File folder = new File(Constant.TESTING_DATA_PATH);
        ArrayList<File> listOfFiles = Utils.listFilesRecursively(folder);

        // test single image
//        File testFile = new File(Constant.TESTING_DATA_PATH + "/gpripe.gif");
//        ArrayList<Rect> testFaces = detector.getFaces(testFile.getPath());
//        try {
//            File imageFile = new File(testFile.getPath());
//            BufferedImage img = ImageIO.read(imageFile);
//            Graphics2D graph = img.createGraphics();
//            graph.setColor(Color.BLACK);
//            for (Rect face: testFaces) {
//                graph.draw(new Rectangle(face.x, face.y, face.width, face.height));
//            }
//            graph.dispose();
//            ImageIO.write(img, "jpg", new File(Constant.TESTING_RESULTS_PATH + testFile.getName()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        for (File file: listOfFiles) {
            if (!file.isFile() || !file.getName().endsWith(Constant.TESTING_DATA_IMAGE_EXT))   continue;
            System.out.println(file.getPath());
            ArrayList<Rect> faces = detector.getFaces(file.getPath());
            try {
                File imageFile = new File(file.getPath());
                BufferedImage img = ImageIO.read(imageFile);
                Graphics2D graph = img.createGraphics();
                graph.setColor(Color.BLUE);
                for (Rect face: faces) {
                    graph.draw(new Rectangle(face.x, face.y, face.width, face.height));
                }
                graph.dispose();
                ImageIO.write(img, "jpg", new File(Constant.TESTING_RESULTS_PATH + file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
