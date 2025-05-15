package org.mch.messaging;

import org.mch.dto.AppointmentEvent;

public interface AppointmentEventProducer {
    void publishEvent(AppointmentEvent event);
}
