/**
 * Something profound.
 */
package com.examples.flinkml;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

/**
 * Starting point for a Flink streaming job using the DataStream API.
 */
public final class StreamingJob {
    private StreamingJob() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    public static void main(final String[] args) throws Exception {
        final JobConfig config = new JobConfig();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<>(
            "fishes",
            new SimpleStringSchema(),
            config.kafka()
        );

        final DataStream<String> msgs = env.addSource(myConsumer).name("Source Topic");

        final FlinkKafkaProducer<String> enriched = new FlinkKafkaProducer<>(
            "weight-predictions",
            new SimpleStringSchema(),
            config.kafka()
        );

        msgs.addSink(enriched).name("Destination Topic");
        env.execute("Kafka Experiment");
    }
}