package org.apache.flink.streaming.connectors.kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;
import org.apache.flink.streaming.connectors.kafka.config.OffsetCommitMode;
import org.apache.flink.streaming.connectors.kafka.internals.AbstractFetcher;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.streaming.connectors.kafka.internals.SpecificKafkaFetcher;
import org.apache.flink.util.SerializedValue;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class SpecificFlinkKafkaConsumer<T> extends FlinkKafkaConsumer<T> {
    private long stopConsumingTimestamp;

    public SpecificFlinkKafkaConsumer(
            String topic, DeserializationSchema<T> valueDeserializer, Properties props) {
        super(topic, valueDeserializer, props);
    }

    public SpecificFlinkKafkaConsumer(
            String topic, KafkaDeserializationSchema<T> deserializer, Properties props) {
        super(topic, deserializer, props);
    }

    public SpecificFlinkKafkaConsumer(
            List<String> topics, DeserializationSchema<T> deserializer, Properties props) {
        super(topics, deserializer, props);
    }

    public SpecificFlinkKafkaConsumer(
            List<String> topics, KafkaDeserializationSchema<T> deserializer, Properties props) {
        super(topics, deserializer, props);
    }

    public SpecificFlinkKafkaConsumer(
            Pattern subscriptionPattern,
            DeserializationSchema<T> valueDeserializer,
            Properties props) {
        super(subscriptionPattern, valueDeserializer, props);
    }

    public SpecificFlinkKafkaConsumer(
            Pattern subscriptionPattern,
            KafkaDeserializationSchema<T> deserializer,
            Properties props) {
        super(subscriptionPattern, deserializer, props);
    }

    private void setStopConsumingTimestamp(long stopConsumingTimestamp) {
        this.stopConsumingTimestamp = stopConsumingTimestamp;
    }

    // 指定时间范围
    public FlinkKafkaConsumerBase<T> setIntervalFromTimestamp(
            long startupOffsetsTimestamp, long stopConsumingTimestamp) {
        setStopConsumingTimestamp(stopConsumingTimestamp);
        if (startupOffsetsTimestamp > stopConsumingTimestamp) {
            throw new IllegalArgumentException(
                    "The start consuming time " + startupOffsetsTimestamp + "exceeds the end time");
        } else {
            return super.setStartFromTimestamp(startupOffsetsTimestamp);
        }
    }

    @Override
    protected AbstractFetcher<T, ?> createFetcher(
            SourceContext<T> sourceContext,
            Map<KafkaTopicPartition, Long> assignedPartitionsWithInitialOffsets,
            SerializedValue<WatermarkStrategy<T>> watermarkStrategy,
            StreamingRuntimeContext runtimeContext,
            OffsetCommitMode offsetCommitMode,
            MetricGroup consumerMetricGroup,
            boolean useMetrics)
            throws Exception {
        return new SpecificKafkaFetcher<>(
                sourceContext,
                assignedPartitionsWithInitialOffsets,
                watermarkStrategy,
                runtimeContext.getProcessingTimeService(),
                runtimeContext.getExecutionConfig().getAutoWatermarkInterval(),
                runtimeContext.getUserCodeClassLoader(),
                runtimeContext.getTaskNameWithSubtasks(),
                deserializer,
                properties,
                pollTimeout,
                runtimeContext.getMetricGroup(),
                consumerMetricGroup,
                useMetrics,
                stopConsumingTimestamp);
    }
}
