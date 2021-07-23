package com.examples.flinkml;

import org.apache.flink.api.common.functions.MapFunction;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Our model for fish weight prediction
 **/
public class FishWeightPredictor implements MapFunction<FishObservation, WeightPrediction> {
    private final Evaluator evaluator;

    public FishWeightPredictor(final InputStream pmmlModel) throws JAXBException, SAXException {
        evaluator = new LoadingModelEvaluatorBuilder()
                .load(pmmlModel)
                .build();
    }

    public Map<String, ?> predict(final Map<String, ?> observation) {
        final Map<FieldName, FieldValue> features = extractFeatures(observation);
        final Map<FieldName, ?> results = evaluator.evaluate(features);

        return EvaluatorUtil.decodeAll(results);
    }

    private Map<FieldName, FieldValue> extractFeatures(final Map<String, ?> observation) {
        final Map<FieldName, FieldValue> features = new LinkedHashMap<>();
        final List<InputField> inputs = evaluator.getInputFields();
        for (InputField field : inputs) {
            final FieldName inputName = field.getName();
            final Object rawValue = observation.get(inputName.getValue());
            final FieldValue inputValue = field.prepare(rawValue);

            features.put(inputName, inputValue);
        }

        return features;
    }

    @Override
    public WeightPrediction map(final FishObservation observation) throws Exception {
        final Map<String, Object> predictors = new HashMap<>();
        predictors.put("Length3", observation.getLength());
        predictors.put("Species", observation.getSpecies());

        final Map<String, ?> result = this.predict(predictors);

        return new WeightPrediction(observation.getLength(), observation.getSpecies(), (double) result.get("Weight"));
    }
}
