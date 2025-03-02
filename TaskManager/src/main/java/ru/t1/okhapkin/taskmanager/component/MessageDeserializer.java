package ru.t1.okhapkin.taskmanager.component;

import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class MessageDeserializer<T> extends JsonDeserializer<T> {

    private final Logger log = LoggerFactory.getLogger(MessageDeserializer.class);

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try {
            return super.deserialize(topic, headers, data);
        } catch (Exception e) {
            log.error("An error occurred during deserialization of the changes: {}", e);
            return null;
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception e) {
            log.error("An error occurred during deserialization of the changes: {}", e);
            return null;
        }
    }
}
