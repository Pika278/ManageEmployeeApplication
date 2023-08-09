package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.repository.CheckinRepository;
import com.example.demo.repository.CheckoutRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class ScheduledTask {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CheckinRepository checkinRepository;
    @Autowired
    private CheckoutRepository checkoutRepository;
    @Autowired
    private EmailService emailService;


    private static final Logger logger = (Logger) LoggerFactory.getLogger(ScheduledTask.class);

    @Scheduled(cron = "0 0 18 ? * MON-SAT")
//    15 * * * * ?
    public void checkEmployeeCheckin() {
        Long id = 15L;
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            throw new UsernameNotFoundException("Khong tim thay nhan vien");
        }
        Employee employee = optionalEmployee.get();
        LocalDate localDate = LocalDate.now();
        LocalTime checkinTime = checkinRepository.checkCheckedIn(id);
        LocalTime checkoutTime = checkoutRepository.checkCheckedOut(id);
        if (checkinTime == null) {
            String to = employee.getEmail();
            String subject = "Quên checkin nè";
            String body = "Hôm nay bạn đã quên checkin";
            emailService.sendMail(to, subject, body);
            logger.info("Send email to employee forgot checkin");
        }
        if (checkoutTime == null) {
            String to = employee.getEmail();
            String subject = "Quên checkout nè";
            String body = "Hôm nay bạn đã quên checkout";
            emailService.sendMail(to, subject, body);
            logger.info("Send email to employee forgot checkout");
        }
    }
}
