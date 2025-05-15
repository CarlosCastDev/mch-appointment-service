package org.mch.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.mch.dto.AppointmentEvent;
import org.mch.qualifiers.RabbitProducer;

@RabbitProducer
@ApplicationScoped
@Slf4j
public class AppointmentEventProducerRabbitMQ implements AppointmentEventProducer {

    @Channel("appointment-events-out")
    Emitter<AppointmentEvent> emitter;

    @Override
    public void publishEvent(AppointmentEvent event) {
        emitter.send(event);
        log.info("Mensaje appoinment enviado");
    }
}
