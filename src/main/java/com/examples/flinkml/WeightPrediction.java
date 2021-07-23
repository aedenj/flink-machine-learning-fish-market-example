package com.examples.flinkml;

/**
 * The weight prediction from the model.
 */
public class WeightPrediction extends FishObservation {
    private final double weight;

    public WeightPrediction(final double length, final String species, final double weight) {
        super(length, species);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

}
