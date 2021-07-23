/**
 * A toy example of how to use a PMML model in Flink
 */
package com.examples.flinkml;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.io.InputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A toy example of how to use a PMML model in Flink
 */
public final class StreamingJob {
    private static final Gson GSON =
            new GsonBuilder().serializeSpecialFloatingPointValues().create();

    private StreamingJob() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    public static void main(final String[] args) throws Exception {
        final JobConfig config = new JobConfig();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final FlinkKafkaConsumer<String> fishes = new FlinkKafkaConsumer<>(
            "fishes",
            new SimpleStringSchema(),
            config.kafka()
        );
        final FlinkKafkaProducer<String> predictions = new FlinkKafkaProducer<>(
            "weight-predictions",
            new SimpleStringSchema(),
            config.kafka()
        );
        fishes.assignTimestampsAndWatermarks(WatermarkStrategy.noWatermarks());

        final InputStream pmmlModel = StreamingJob.class.getResourceAsStream("/fish-weight-model.pmml");
        env.addSource(fishes)
           .name("Fish Observations")
           .map(json -> GSON.fromJson(json, FishObservation.class))
           .map(new FishWeightPredictor(pmmlModel))
           .map(p -> GSON.toJson(p))
           .addSink(predictions)
           .name("Predictions");


        env.execute("Fish Weight Predictions");
    }
}
