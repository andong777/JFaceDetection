package struct;

import java.awt.image.BufferedImage;

import util.HistogramEQ;
import util.Utils;

public class IntegralImage {

    public int width, height;
    private final int[][] integral;

    // will do histogram equalization in init
    public IntegralImage(String path) {
        this(Utils.readBufferedImage(path));
    }

    public IntegralImage(BufferedImage img) {
        width = img.getWidth();
        height = img.getHeight();
        integral = new int[width][height];
//        BufferedImage equalized = HistogramEQ.histogramEqualization(img);
//        prepare(equalized);
        prepare(img);
    }

    private void prepare(BufferedImage img) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xff, g = (rgb >> 8) & 0xff, b = rgb & 0xff;
                integral[x][y] = (r + g + b) / 3;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 1; y < height; y++) {
                integral[x][y] += integral[x][y - 1];
            }
        }
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                integral[x][y] += integral[x - 1][y];
            }
        }
    }

    public int sumValue(Rect r) {
        int x = r.x, y = r.y,
                x2 = x + r.width - 1, y2 = y + r.height - 1;
        return integral[x2][y2] - (x > 0 ? integral[x - 1][y2] : 0)
                - (y > 0 ? integral[x2][y - 1] : 0)
                + (x > 0 && y > 0 ? integral[x - 1][y - 1] : 0);
    }

}
