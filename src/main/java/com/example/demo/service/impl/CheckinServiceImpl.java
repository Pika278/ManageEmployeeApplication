package com.example.demo.service.impl;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dto.request.CheckinRequest;
import com.example.demo.entity.Checkin;
import com.example.demo.entity.Employee;
import com.example.demo.repository.CheckinRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.CheckinService;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class CheckinServiceImpl implements CheckinService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CheckinRepository checkinRepository;

    @Override
    public String checkin(CheckinRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(user.getUsername());
        if (!optionalEmployee.isPresent()) {
            throw new UsernameNotFoundException("Khong tim thay nhan vien");
        }
        Employee employee = optionalEmployee.get();
        int checkinNumber = request.getCheckinNumber();
        if (checkinNumber == employee.getCheckin()) {
            Checkin checkin = new Checkin(LocalDate.now(), LocalTime.now(), employee);
            checkinRepository.save(checkin);
            return "Checkin success";
        } else {
            return "Checkin Number wrong";
        }

    }

}
