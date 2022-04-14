package com.krisfrell.controller;

import com.google.gson.Gson;
import com.krisfrell.entity.*;
import com.krisfrell.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {
    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private MastersRepository mastersRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/salon")
    public List<Salon> getSalons() {
        return salonRepository.findAll();
    }

    @GetMapping("/salon/{id}/service")
    public List<Service> getServices(@PathVariable("id") long id) {
        return servicesRepository.findServicesBySalonsId(id);
    }

    @GetMapping("/salon/{salonId}/service/{serviceId}/masters")
    public List<Master> getMasters(@PathVariable("salonId") long salonId, @PathVariable("serviceId") long serviceId) {
        return mastersRepository.findMastersBySalonIdAndServicesId(salonId, serviceId);
    }

    @GetMapping("/master/{masterId}/appointment")
    public List<Appointment> getAppointments(@PathVariable("masterId") long masterId) {
        return appointmentRepository.findAppointmentsByMasterId(masterId);
    }

    @GetMapping("/user/{userId}/appointment")
    public List<String> getUserAppointments(@PathVariable("userId") long userId) {
        List<Appointment> appointmentsByUserId = appointmentRepository.findAppointmentsByUserId(userId);
        List<String> result = new ArrayList<>();
        for (Appointment appointment : appointmentsByUserId) {
            Map<String, Object> data = new HashMap();
            data.put("id", appointment.getId());
            data.put("datetime", appointment.getDateTime());
            String salonName = salonRepository.findById(appointment.getSalon()).get().getSalon();
            String serviceName = servicesRepository.findById(appointment.getService()).get().getService();
            String masterName = mastersRepository.findById(appointment.getMasterId()).get().getMaster();
            data.put("salonName", salonName);
            data.put("serviceName", serviceName);
            data.put("masterName", masterName);
            Gson gson = new Gson();
            String json = gson.toJson(data);
            result.add(json);
        }
        return result;
    }

    @GetMapping("/user")
    public User getCurrentUser(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return userRepository.findByEmail(email);
    }

    @PostMapping("/appointment")
    public void createAppointment(@RequestBody Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @GetMapping("/salon/{id}")
    public Optional<Salon> getSalonById(@PathVariable("id") long id) {
        return salonRepository.findById(id);
    }

    @GetMapping("/service/{id}")
    public Optional<Service> getServiceById(@PathVariable("id") long id) {
        return servicesRepository.findById(id);
    }

    @GetMapping("/master/{id}")
    public Optional<Master> getMasterById(@PathVariable("id") long id) {
        return mastersRepository.findById(id);
    }

    @GetMapping("/names/{id}")
    public String getNamesByAppointment(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        Map<String, String> names = new HashMap();
        if (appointment.isPresent()) {
            String salonName = salonRepository.findById(appointment.get().getSalon()).get().getSalon();
            String serviceName = servicesRepository.findById(appointment.get().getService()).get().getService();
            String masterName = mastersRepository.findById(appointment.get().getMasterId()).get().getMaster();
            names.put("salonName", salonName);
            names.put("serviceName", serviceName);
            names.put("masterName", masterName);
        }
        Gson gson = new Gson();
        return gson.toJson(names);
    }

    @DeleteMapping("/remove/{id}")
    public void removeAppointment(@PathVariable("id") long id) {
        appointmentRepository.deleteById(id);
    }
}
