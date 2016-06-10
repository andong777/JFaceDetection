package learn;

import struct.IntegralImage;
import struct.Label;
import struct.Rect;
import util.Constant;

import java.util.ArrayList;

public class FaceDetector {

    private IntegralImage img;
    private Classifier clf;

    private double distThreshold = 20;

    private static final double scaleFactor = 1.25d;
    private static final double moveFactor = 4d;
    private static final int scanTimes = 8;

    public FaceDetector(Classifier clf) {
        this.clf = clf;
    }

    public ArrayList<Rect> getFaces(String path) {
        img = new IntegralImage(path);

        ArrayList<Rect> ret = new ArrayList<Rect>();
        double scale = 1.0d;

        for (int t = 0; t < scanTimes; t++) {
            scale *= scaleFactor;
            double move = scale * moveFactor;

            int width = (int) Math.round(Constant.SIZE_X * scale);
            int height = (int) Math.round(Constant.SIZE_Y * scale);
            for (int i = 0; ; i++) {
                int x = (int) Math.round(move * i);
                if (x + width - 1 >= img.width) break;
                for (int j = 0; ; j++) {
                    int y = (int) Math.round(move * j);
                    if (y + height - 1 >= img.height) break;

                    Rect window = new Rect(x, y, width, height);
                    if (clf.classify(img, window) == Label.FACE) {
                        ret.add(window);
                    }
                }
            }
        }
        System.out.print(ret.size() + " -> ");
        combineFaces(ret);
        return ret;
//        return combineFaces(ret);
    }

    private ArrayList<Rect> combineFacesOld(ArrayList<Rect> faces) {
        ArrayList<Rect> ret = new ArrayList<Rect>();
        outer:
        for (int i = 0; i < faces.size(); i++) {
            Rect unchecked = faces.get(i);
            for (int j = 0; j < ret.size(); j++) {
                Rect checked = ret.get(j);
                double dist = unchecked.getCenter().getDistance(checked.getCenter());
                double threshold = (unchecked.width + checked.width) / 2;
                if (dist <= threshold) {
                    ret.set(j, checked.meanRect(unchecked));
                    continue outer;
                }
            }
            ret.add(unchecked);
        }
        System.out.println(ret.size());
        return ret;
    }

    private void combineFaces(ArrayList<Rect> faces) {
        outer:
        while (true) {
            boolean change = false;
            for (int i = 0; i < faces.size(); i++) {
                Rect fi = faces.get(i);
                for (int j = i + 1; j < faces.size(); j++) {
                    Rect fj = faces.get(j);
                    double dist = fj.getCenter().getDistance(fi.getCenter());
                    double threshold = (fi.width + fj.width) / 2;
                    if (dist <= threshold) {
                        Rect newRect = fi.meanRect(fj);
                        faces.remove(fi);
                        faces.remove(fj);
                        faces.add(newRect);
                        change = true;
                        continue outer;
                    }
                }
            }
            if (!change) break;;
        }
        System.out.println(faces.size());
    }

}
