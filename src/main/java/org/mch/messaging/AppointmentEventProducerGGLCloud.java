package org.mch.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import com.google.cloud.pubsub.v1.Publisher;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mch.dto.AppointmentEvent;
import org.mch.qualifiers.GCloudProducer;

import java.io.IOException;

@GCloudProducer
@ApplicationScoped
@Slf4j
public class AppointmentEventProducerGGLCloud implements AppointmentEventProducer {

    @ConfigProperty(name = "pubsub.project-id")
    String PROJECT_ID;
    @ConfigProperty(name = "pubsub.topic-id")
    String TOPIC_ID;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Publisher publisher;

    @PostConstruct
    void init() throws IOException {
        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID);
        publisher = Publisher.newBuilder(topicName).build();
    }

    @PreDestroy
    void shutdown() throws Exception {
        if (publisher != null) {
            publisher.shutdown();
        }
    }

    @Override
    public void publishEvent(AppointmentEvent event) {
        try {
            log.info("Project_id: {}", PROJECT_ID);
            log.info("topic_id: {}", TOPIC_ID);
            String json = objectMapper.writeValueAsString(event);
            ByteString data = ByteString.copyFromUtf8(json);
            PubsubMessage message = PubsubMessage.newBuilder().setData(data).build();

            publisher.publish(message);
            log.info("Mensaje appointment enviado a Pub/Sub: {}", json);

        } catch (Exception e) {
            log.error("Error al publicar evento en Pub/Sub", e);
        }
    }
}