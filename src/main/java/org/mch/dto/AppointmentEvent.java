package org.mch.dto;

import lombok.Data;
import org.mch.entity.Appointment;

import java.time.LocalDateTime;

@Data
public class AppointmentEvent {
    private String appointmentId;
    private Long patientId;
    private String doctorId;
    private String status; // Ejemplo: "CREATED", "UPDATED", "CANCELLED"
    private LocalDateTime appointmentDate;

    public static AppointmentEvent fromEntity(Appointment appointment) {
        AppointmentEvent event = new AppointmentEvent();
        // Convertimos el ID de Long a String, o puedes mantenerlo como Long si lo prefieres
        event.setAppointmentId(appointment.id != null ? appointment.id.toString() : null);
        event.setDoctorId(appointment.doctorId != null ? appointment.doctorId.toString() : null);
        event.setPatientId(appointment.patientId != null ? appointment.patientId : null);
        event.setStatus(appointment.status);
        event.setAppointmentDate(appointment.appointmentDateTime);
        return event;
    }
}

