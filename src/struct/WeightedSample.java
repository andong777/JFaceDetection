package struct;

public class WeightedSample extends Sample {

    public double weight;

    public WeightedSample(IntegralImage img, Label label, double weight) {
        super(img, label);
        this.weight = weight;
    }

}
