package ru.t1.okhapkin.taskmanager.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.okhapkin.taskmanager.component.TaskProducer;
import ru.t1.okhapkin.taskmanager.dto.TaskKafkaDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value("${t1.kafka.bootstrap.server}")
    private String server;
    @Value("${t1.kafka.topic.tasks_update}")
    private String taskTopic;

    @Bean("task")
    public KafkaTemplate<String, TaskKafkaDTO> kafkaTemplate(ProducerFactory<String, TaskKafkaDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable", havingValue = "true", matchIfMissing = true)
    public TaskProducer producerTask(@Qualifier("task") KafkaTemplate<String, TaskKafkaDTO> template) {
        template.setDefaultTopic(taskTopic);
        return new TaskProducer(template);
    }

    @Bean
    public ProducerFactory<String, TaskKafkaDTO> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

}
