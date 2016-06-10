package learn;

import struct.IntegralImage;
import struct.Label;
import struct.Rect;

public interface Classifier {

  Label classify(IntegralImage img, Rect windowRect);
  
}
