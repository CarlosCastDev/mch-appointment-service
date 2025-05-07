package org.mch.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment extends PanacheEntity {

    // ID se maneja automáticamente con PanacheEntity

    // Identificador del médico (podría ser un FK en una solución más completa)
    public Long doctorId;

    // Identificador del paciente
    public Long patientId;

    // Fecha y hora de la cita
    public LocalDateTime appointmentDateTime;

    // Estado de la cita: por ejemplo, "SCHEDULED", "CANCELLED", "COMPLETED"
    public String status;

    // Notas adicionales (opcional)
    @Column(length = 500)
    public String notes;
}
