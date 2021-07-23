package com.examples.flinkml;


/**
 * The relevant predictors for our model.
 **/
public class FishObservation {
    private final double length;
    private final String species;

    public FishObservation(final double length, final String species) {
        this.length = length;
        this.species = species;
    }

    public double getLength() {
        return length;
    }
    public String getSpecies() {
        return species;
    }
}
