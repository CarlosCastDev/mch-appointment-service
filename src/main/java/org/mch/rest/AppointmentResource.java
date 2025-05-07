package org.mch.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.mch.dto.AppointmentEvent;
import org.mch.entity.Appointment;
import org.mch.messaging.AppointmentEventProducer;

import java.util.List;

@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentResource {

    @Inject
    AppointmentEventProducer eventProducer;

    @GET
    public List<Appointment> getAllAppointments() {
        return Appointment.listAll();
    }

    @GET
    @Path("{id}")
    public Appointment getAppointment(@PathParam("id") Long id) {
        Appointment appointment = Appointment.findById(id);
        if (appointment == null) {
            throw new NotFoundException("Appointment not found");
        }
        return appointment;
    }

    @POST
    @Transactional
    public Response createAppointment(Appointment appointment, @Context UriInfo uriInfo) {
        appointment.persist();
        if (appointment.isPersistent()) {
            AppointmentEvent event = AppointmentEvent.fromEntity(appointment);
            // Actualizamos el estado del evento, en este caso "CREATED"
            event.setStatus("CREATED");
            // Publicamos el evento para que otros servicios (ej. Medical Records) lo consuman.
            eventProducer.publishEvent(event);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(appointment.id.toString());
            return Response.created(builder.build()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Appointment updateAppointment(@PathParam("id") Long id, Appointment appointmentData) {
        Appointment appointment = Appointment.findById(id);
        if (appointment == null) {
            throw new NotFoundException("Appointment not found");
        }
        appointment.doctorId = appointmentData.doctorId;
        appointment.patientId = appointmentData.patientId;
        appointment.appointmentDateTime = appointmentData.appointmentDateTime;
        appointment.status = appointmentData.status;
        appointment.notes = appointmentData.notes;
        return appointment;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteAppointment(@PathParam("id") Long id) {
        Appointment appointment = Appointment.findById(id);
        if (appointment == null) {
            throw new NotFoundException("Appointment not found");
        }
        appointment.delete();
        return Response.noContent().build();
    }
}

