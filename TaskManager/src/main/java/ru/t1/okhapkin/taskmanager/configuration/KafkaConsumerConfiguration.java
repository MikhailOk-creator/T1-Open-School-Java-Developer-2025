package ru.t1.okhapkin.taskmanager.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.okhapkin.taskmanager.entity.Task;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerConfiguration.class);

    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.consumer.group-id}")
    private String groupId;
    @Value("${t1.kafka.consumer.max.poll.records}")
    private String maxPollRecords;

    @Bean
    public ConsumerFactory<String, Task> consumerListenerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory<String, Task>(props);
        factory.setKeyDeserializer(new StringDeserializer());

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Task> kafkaListenerContainerFactory(@Qualifier("consumerListenerFactory") ConsumerFactory<String, Task> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Task> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners(((record, ex, deliveryAttempt) -> {
            log.error("RetryListeners massage = {}, offset = {} deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt);
        }));
        return handler;
    }
}
