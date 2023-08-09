package com.example.demo.service.impl;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dto.request.CheckoutRequest;
import com.example.demo.entity.Checkin;
import com.example.demo.entity.Checkout;
import com.example.demo.entity.Employee;
import com.example.demo.repository.CheckoutRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CheckoutRepository checkoutRepository;

    @Override
    public String checkout(CheckoutRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(user.getUsername());
        if (!optionalEmployee.isPresent()) {
            throw new UsernameNotFoundException("Khong tim thay nhan vien");
        }
        Employee employee = optionalEmployee.get();
        int checkoutNumber = request.getCheckoutNumber();
        if (checkoutNumber == employee.getCheckin()) {
            Checkout checkout = new Checkout(LocalDate.now(), LocalTime.now(),employee);
            checkoutRepository.save(checkout);
            return "Checkout success";
        } else {
            return "Checkout Number wrong";
        }

    }
}
